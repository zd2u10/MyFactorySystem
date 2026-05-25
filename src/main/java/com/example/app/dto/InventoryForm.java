package com.example.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

// ビュー側とのやり取りをするクラス

@Data
public class InventoryForm {
	private String transactionType;
	private Long materialId;
	private BigDecimal quantity;
	private LocalDate transactionDate;
	private String lotNumber;
	private LocalDate arrivalDate;
	private String productCode;
	private String productNumber;
	private String note;
}
