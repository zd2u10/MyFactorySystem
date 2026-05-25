package com.example.app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.InventoryStock;
import com.example.app.domain.InventoryTransaction;
import com.example.app.mapper.InventoryStockMapper;
import com.example.app.mapper.InventoryTransactionMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

	private final InventoryStockMapper inventoryStockMapper;
	private final InventoryTransactionMapper inventoryTransactionMapper;

	@Override
	@Transactional
	public void receiveMaterial(Long materialId, String lotNumber, String origin, BigDecimal quantity,
			LocalDate expiryDate, LocalDate arrivalDate, boolean isInspected) {

		// 1.在庫の登録・更新
		InventoryStock stock = new InventoryStock();
		stock.setMaterialId(materialId);
		stock.setLotNumber(lotNumber);
		stock.setOrigin(origin);
		stock.setQuantity(quantity);
		stock.setExpiryDate(expiryDate);
		stock.setArrivalDate(arrivalDate);
		stock.setInspected(isInspected);

		inventoryStockMapper.upsertStock(stock);

		// 2. 入荷履歴の記録
		InventoryTransaction transaction = new InventoryTransaction();
		transaction.setStockId(stock.getId());
		transaction.setTransactionType("IN");
		transaction.setQuantityChange(quantity);
		transaction.setTransactionDate(arrivalDate);
		transaction.setCreatedAt(LocalDateTime.now());
		transaction.setNote("入荷: " + lotNumber);

		inventoryTransactionMapper.insert(transaction);
	}

	@Override
	@Transactional
	public void consumeMaterial(Long materialId, BigDecimal quantity, LocalDate productionDate,
			String transactionType, String productCode, String productNumber, String note) {
		// 1. 在庫取得(入荷順または賞味期限の古い順)
		List<InventoryStock> stocks = inventoryStockMapper.findByMaterialIdOrderByCreatedAtAsc(materialId);

		// 2. 引数の quantity を「計算上の残量(remainingQuantity)」として扱う
		BigDecimal remainingQuantity = quantity;

		for (InventoryStock stock : stocks) {
			// 消費に対して在庫があるかどうか
			if (remainingQuantity.compareTo(BigDecimal.ZERO) <= 0) {
				break;
			}

			// このロットから消費に引き当て可能な数量を算出
			BigDecimal consumeFromThisLot = remainingQuantity.min(stock.getQuantity());

			// 在庫を減らす (MapperのupdateQuantityが必要)
			stock.setQuantity(stock.getQuantity().subtract(consumeFromThisLot));
			inventoryStockMapper.updateQuantity(stock);

			// 履歴の記録項目
			// 履歴の記録
			InventoryTransaction transaction = new InventoryTransaction();
			transaction.setStockId(stock.getId());
			transaction.setTransactionType(transactionType);
			transaction.setQuantityChange(consumeFromThisLot.negate());
			transaction.setTransactionDate(productionDate);
			transaction.setProductCode(productCode);
			transaction.setProductNumber(productNumber);
			transaction.setNote(note);
			transaction.setCreatedAt(LocalDateTime.now());

			// 記録処理
			inventoryTransactionMapper.insert(transaction);

			// 残りの必要量を減らす
			remainingQuantity = remainingQuantity.subtract(consumeFromThisLot);
		}

		// brake(消費が在庫を上回っていた場合)エラーを投げる
		if (remainingQuantity.compareTo(BigDecimal.ZERO) > 0) {
			throw new RuntimeException("在庫が不足しています。不足分: " + remainingQuantity);
		}
	}
}
