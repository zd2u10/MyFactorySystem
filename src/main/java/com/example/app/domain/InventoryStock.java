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

	private String unit;

	/**
	 * 実際に使用可能な在庫量
	 * 製造リスト判定・在庫確認はこの値を基準にする
	 */
	public BigDecimal getAvailableQuantity() {
		BigDecimal reserved = reservedQuantity != null ? reservedQuantity : BigDecimal.ZERO;
		return quantity.subtract(reserved);
	}

	/**
	 * 表示用フォーマット（末尾の不要な0を削除）
	 */
	// 荷姿用
	public String getFormattedNetWeight() {
		if (netWeight == null) {
			return null;
		}
		// stripTrailingZeros()で末尾の0を消し、toPlainString()で指数表記を防ぐ
		return this.netWeight.stripTrailingZeros().toPlainString();
	}

	// 在庫数用
	public String getFormattedQuantity() {
		if (this.quantity == null) {
			return null;
		}
		return this.quantity.stripTrailingZeros().toPlainString();
	}
}
