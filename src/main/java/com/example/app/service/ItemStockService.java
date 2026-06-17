package com.example.app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.app.dto.ItemStockDto;

public interface ItemStockService {
	// 全製品の在庫状況(合計)を取得
	List<ItemStockDto> getAllItemStock();

	//入庫処理（追加）
	void addStock(Long itemId, String lotNumber, BigDecimal quantity, LocalDate productionDate);

	// 出庫・調整処理（減少）
	void reduceStock(Long stockId, BigDecimal reduceQuantity, String transactionType);
}
