package com.example.app.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class InventoryStock {
	private Long id;
	private String materialName;
	private Long materialId;
	private String lotNumber;
	private String origin; // 産地（未入力可、nullも許容）
	private BigDecimal netWeight;
	private BigDecimal quantity; // 実在庫
	private BigDecimal reservedQuantity; // 仮消費ロック量（製造中）
	private LocalDate expiryDate;
	private LocalDate arrivalDate;
	private boolean inspected;
	private LocalDateTime createdAt;

	/**
	 * 実際に使用可能な在庫量
	 * 製造リスト判定・在庫確認はこの値を基準にする
	 */
	public BigDecimal getAvailableQuantity() {
		BigDecimal reserved = reservedQuantity != null ? reservedQuantity : BigDecimal.ZERO;
		return quantity.subtract(reserved);
	}
}
