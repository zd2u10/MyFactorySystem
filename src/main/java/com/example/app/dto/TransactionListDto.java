package com.example.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class TransactionListDto {

	private LocalDate transactionDate; // 操作日
	private String transactionType; // IN(消費) / PRODUCTION(製造) / DISPOSAL(廃棄)
	private String materialName; // 材料名
	private String lotNumber; // ロット番号
	private BigDecimal quantityChange; // 増減値

	// 製品情報を表示するためのフィールド
	private String productCode; // ここにItemのIDを入れる
	private String productName;
	private String productNumber; // 製造ロットナンバー
	private String note; // 備考
}
