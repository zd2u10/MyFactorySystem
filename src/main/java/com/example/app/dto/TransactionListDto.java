package com.example.app.dto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;

import lombok.Data;

@Data
public class TransactionListDto {

	private LocalDate transactionDate; // 操作日
	private String transactionType; // IN(消費) / PRODUCTION(製造) / DISPOSAL(廃棄)
	private String materialName; // 材料名
	private String lotNumber; // ロット番号
	private BigDecimal quantityChange; // 増減値
	private String unit;
	private String quantityStr; // 表示用
	// 製品情報を表示するためのフィールド
	private String productCode; // ここにItemのIDを入れる
	private String productName;
	private String productNumber; // 製造ロットナンバー
	private String note; // 備考

	// 不要な0を表示させないメソッド
	public void setupDisplayFields() {
		// 1. フォーマットの準備 (3桁カンマ区切り+単位付け)
		DecimalFormat df = new DecimalFormat("#,###");
		String unitStr = (this.unit != null) ? " " + this.unit : "";

		// 2. フォーマットを運用
		this.quantityStr = (this.quantityChange != null)
				? df.format(this.quantityChange) + unitStr
				: "-";
	}
}
