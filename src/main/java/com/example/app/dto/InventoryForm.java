package com.example.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

// ビュー側とのやり取りをするクラス

@Data
public class InventoryForm {
	private String transactionType; // "IN"入荷
	private Long materialId; // 材料ID
	private BigDecimal quantity; // 入荷数量
	private LocalDate transactionDate; // 処理された日
	private String lotNumber; // ロット番号
	private LocalDate arrivalDate; // 入荷日
	private String productCode;
	private String productNumber;
	private String note;
	private String origin; // 産地
	private LocalDate expiryDate; // 賞味期限

}
