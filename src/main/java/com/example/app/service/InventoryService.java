package com.example.app.service;

import java.math.BigDecimal;
import java.time.LocalDate;

// 在庫管理に関するロジックを提供するインターフェース

public interface InventoryService {

	/**
	 * 原材料を入荷し、在庫に加算
	 * @param materialId 材料ID
	 * @param lotNumber ロット番号
	 * @param origin 産地
	 * @param quantity 入荷量
	 * @param expiryDate 賞味期限
	 * @param arrivalDate 入荷日
	 * @param isInspected 検品判定
	 */

	void receiveMaterial(Long materialId, String lotNumber,
			String origin, BigDecimal quantity, LocalDate expiryDate,
			LocalDate arrivalDate, boolean isInspected);

	/**
	 * 製造に必要な原材料を在庫から消費する。
	 * @param materialId 材料ID
	 * @param requiredQuantity 消費が必要な合計量
	 * @param productionDate 製造日（履歴記録用）
	 */

	void consumeMaterial(Long materialId, BigDecimal quantity, LocalDate productionDate,
			String transactionType, String productCode, String productNumber, String note);

}
