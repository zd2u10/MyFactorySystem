package com.example.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemStockDto {
	// 識別用
	private Long itemId;
	private String itemName;
	private String salesUnit;

	// 集計・状態(一覧表示用)
	private BigDecimal totalQuantity;
	private BigDecimal minStock;

	//--- 追加：編集画面（edit.html）で詳細を表示するための拡張 ---
	private Long stockId; // 特定ロットを操作するために必要
	private String lotNumber; // どのロットか
	private LocalDate productionDate; // 製造日（「仮製造中」の管理用）
	private BigDecimal reservedQuantity; // 出荷引き当て数（案A採用のため必須）

	// 計算ロジック：出荷可能在庫 = 実在庫 - 引き当て
	public BigDecimal getAvailableQuantity() {
		if (totalQuantity == null)
			return BigDecimal.ZERO;
		BigDecimal reserved = (reservedQuantity == null) ? BigDecimal.ZERO : reservedQuantity;
		return totalQuantity.subtract(reserved);
	}
}
