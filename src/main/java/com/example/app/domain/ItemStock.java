package com.example.app.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ItemStock {
	private Long id;
	private Long itemId; // itemsテーブルのIDと紐づけ
	private String lotNumber; // ロット番号
	private BigDecimal quantity; // 在庫数(decimal 12,3)
	private BigDecimal minStock; // 最低在庫数(12, 3)
	private LocalDate productionDate; // 製造日
	private LocalDateTime createdAt; // 記録日
	private BigDecimal reservedQuantity; //仮用 出荷引き当て

	//出荷可能在庫を計算するメソッド
	public BigDecimal getAvailableQuantity() {
		return this.quantity.subtract(this.reservedQuantity);
	}
}
