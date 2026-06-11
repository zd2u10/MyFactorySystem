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

	// 在庫一覧表示
	@Override
	public List<InventoryStock> getStocksByType(String materialType) {
		return inventoryStockMapper.findAllStocksWithMaterialType(materialType);
	}

	// 入荷メソッド
	@Override
	@Transactional
	public void receiveMaterial(
			Long materialId,
			String lotNumber,
			String origin,
			BigDecimal netWeight,
			BigDecimal quantity,
			LocalDate expiryDate,
			LocalDate arrivalDate,
			boolean isInspected,
			String note) {

		// 1. 在庫の登録・加算（同一 material + lot + origin なら upsert）
		InventoryStock stock = new InventoryStock();
		stock.setMaterialId(materialId);
		stock.setLotNumber(lotNumber);
		// 空文字はnullに統一（DBのUNIQUE制約と合わせる）
		stock.setOrigin((origin != null && !origin.isBlank()) ? origin.trim() : null);
		stock.setNetWeight(netWeight); // 荷姿(内容量)をセット
		stock.setQuantity(quantity); // 総重量
		stock.setExpiryDate(expiryDate);
		stock.setArrivalDate(arrivalDate);
		stock.setInspected(isInspected);

		inventoryStockMapper.upsertStock(stock);

		// 2. 入荷履歴を inventory_transactions に記録
		//    stock.getId() は upsertStock の useGeneratedKeys で設定される
		InventoryTransaction transaction = new InventoryTransaction();
		transaction.setStockId(stock.getId());
		transaction.setTransactionType("IN");
		transaction.setQuantityChange(quantity); // 入荷はプラス値
		transaction.setTransactionDate(arrivalDate);
		transaction.setCreatedAt(LocalDateTime.now());
		transaction.setNote(note != null && !note.isBlank()
				? note
				: "入荷: lot=" + lotNumber);

		inventoryTransactionMapper.insert(transaction);
	}

	// 消費メソッド
	@Override
	@Transactional
	public void consumeMaterial(
			Long materialId,
			BigDecimal quantity,
			LocalDate productionDate,
			String transactionType,
			String productCode,
			String productNumber,
			String note) {

		// FIFO: 賞味期限昇順 → 入荷日昇順 で在庫を取得
		List<InventoryStock> stocks = inventoryStockMapper.findByMaterialIdOrderByCreatedAtAsc(materialId);

		BigDecimal remaining = quantity;

		for (InventoryStock stock : stocks) {
			if (remaining.compareTo(BigDecimal.ZERO) <= 0)
				break;

			BigDecimal available = stock.getAvailableQuantity();
			if (available.compareTo(BigDecimal.ZERO) <= 0)
				continue;

			BigDecimal consume = remaining.min(available);

			// 実在庫を減算
			stock.setQuantity(stock.getQuantity().subtract(consume));
			inventoryStockMapper.updateQuantity(stock);

			// 履歴を記録
			InventoryTransaction tx = new InventoryTransaction();
			tx.setStockId(stock.getId());
			tx.setTransactionType(transactionType);
			tx.setQuantityChange(consume.negate()); // 消費はマイナス
			tx.setTransactionDate(productionDate);
			tx.setProductCode(productCode);
			tx.setProductNumber(productNumber);
			tx.setNote(note);
			tx.setCreatedAt(LocalDateTime.now());
			inventoryTransactionMapper.insert(tx);

			remaining = remaining.subtract(consume);
		}

		if (remaining.compareTo(BigDecimal.ZERO) > 0) {
			throw new RuntimeException("在庫が不足しています。不足分: " + remaining);
		}
	}
}
