package com.example.app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.ItemStock;
import com.example.app.dto.ItemStockDto;
import com.example.app.mapper.ItemStockMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemStockServiceImpl implements ItemStockService {

	private final ItemStockMapper itemStockMapper;

	// 全てをリスト化
	@Override
	public List<ItemStockDto> getAllItemStock() {
		// ここで複雑な加工が必要なら処理を追加
		return itemStockMapper.findAllItemStock();
	}

	// ロット一覧
	@Override
	public List<ItemStock> findStocksByItemId(Long itemId) {
		return itemStockMapper.findStocksByItemId(itemId);
	}

	@Override
	public ItemStock getItemById(Long itemId) {
		return itemStockMapper.findById(itemId);
	}

	@Override
	public BigDecimal calculateTotalAvailable(Long itemId) {
		List<ItemStock> stocks = itemStockMapper.findStocksByItemId(itemId);
		return stocks.stream()
				.map(s -> s.getQuantity().subtract(s.getReservedQuantity()))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	// 入庫処理（追加）
	@Override
	@Transactional
	public void addStock(Long itemId, String lotNumber, BigDecimal quantity, LocalDate productionDate) {
		// 同一日・同ロットのデータが既にあるかチェック
		ItemStock existingStock = itemStockMapper.findStockByItemIdAndLotNumber(itemId, lotNumber);

		if (existingStock != null) {
			// 既に存在する場合は数量を加算
			itemStockMapper.addQuantity(existingStock.getId(), quantity);
		} else {
			// 存在しない場合は新規登録
			ItemStock newStock = new ItemStock();
			newStock.setItemId(itemId);
			newStock.setLotNumber(lotNumber);
			newStock.setQuantity(quantity);
			newStock.setProductionDate(productionDate);
			itemStockMapper.insertStock(newStock);
		}
	}

	// 出庫・調整処理（減少）
	@Override
	@Transactional
	public void reduceStock(Long stockId, BigDecimal reduceQuantity, String transactionType) {
		ItemStock stock = itemStockMapper.findById(stockId);

		if (stock == null) {
			throw new RuntimeException("対象の在庫データが見つかりません。");
		}
		// ★ガード機能 : 現在の実在庫から出庫数を引いてマイナスになる場合はエラーにする
		if (stock.getQuantity().compareTo(reduceQuantity) < 0) {
			throw new RuntimeException("在庫が不足しています！[現在庫: \" + stock.getQuantity() + \" / 要求数: \" + reduceQuantity + \"]");
		}

		// 現在の数量から減算
		BigDecimal newQuantity = stock.getQuantity().subtract(reduceQuantity);
		itemStockMapper.updateQuantity(stockId, newQuantity);

		// TODO: 将来、製品の履歴テーブル(item_transactions)を作成した際、
		// ここで transactionType と reduceQuantity を用いて履歴INSERT処理を追加する
	}

}
