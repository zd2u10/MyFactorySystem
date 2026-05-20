package com.example.app.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

// 在庫状況を管理するクラス
// どのロットが、どの産地で、現時点でどれだけあるか保持

@Data
public class InventoryStock {
	private Long id;
	private Long materialId; //原材料マスタへの参照ID
	private String lotNumber; // 仕入れ先から付与されたロット番号
	private String origin; // 原料の産地
	private BigDecimal quantity; // 現在の在庫量(単位はマスタに準拠)
	private LocalDate expiryDate; // 賞味期限または使用期限
	private LocalDate arrivalDate; // 原料の入荷日
	private boolean inspected; // 検品済み判定 
	private LocalDateTime createdAt; // システム登録日
}
