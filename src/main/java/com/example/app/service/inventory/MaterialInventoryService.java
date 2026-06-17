package com.example.app.service.inventory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.app.domain.InventoryStock;
import com.example.app.dto.TransactionListDto;

public interface MaterialInventoryService {

	// --- 在庫確認 ---
	List<InventoryStock> getStocksByType(String materialType);

	/**
	 * 原材料を入荷し、在庫に加算する
	 * 同一 material_id + lot_number + origin のレコードが存在すれば数量を加算（upsert）
	 *
	 * @param materialId  材料ID
	 * @param lotNumber   ロット番号（必須）
	 * @param origin      産地（任意・null可）
	 * @param quantity    入荷量
	 * @param expiryDate  賞味期限（任意・null可）
	 * @param arrivalDate 入荷日（必須）
	 * @param isInspected 検品済みフラグ
	 * @param note        備考（inventory_transactionsに記録）
	 */

	// --- 入庫係 ---
	void receiveMaterial(
			Long materialId,
			String lotNumber,
			String origin,
			BigDecimal netWeight,
			BigDecimal quantity,
			LocalDate expiryDate,
			LocalDate arrivalDate,
			boolean isInspected,
			String note);

	/**
	 * 製造に必要な原材料を在庫から消費する（FIFO）
	 *
	 * @param materialId      材料ID
	 * @param quantity        消費量
	 * @param transactionDate  消費(製造)日
	 * @param transactionType PRODUCTION / DISPOSAL
	 * @param productCode     製品コード
	 * @param productNumber   製造ロット番号
	 * @param note            備考
	 */

	// --- 出庫・消費 ---
	// 消費
	void consumeMaterial(
			Long materialId,
			BigDecimal quantity,
			LocalDate transactionDate,
			String transactionType,
			String productCode,
			String productNumber,
			String note);

	// 履歴一覧を取得するメソッド
	List<TransactionListDto> getTransactions(String materialType, String transactionType);

}
