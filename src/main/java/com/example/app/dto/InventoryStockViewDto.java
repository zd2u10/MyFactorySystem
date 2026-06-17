package com.example.app.dto;

import java.text.DecimalFormat;
import java.time.LocalDate;

import com.example.app.domain.InventoryStock;

import lombok.Data;

@Data
public class InventoryStockViewDto {
	// 既存のフィールドをそのまま持たせる
	private Long id;
	private String materialName;
	private String origin;
	private String unit;
	private LocalDate expiryDate;
	private String lotNumber;

	// ★ 表示専用のフィールド（文字列型）
	private String netWeightStr;
	private String quantityStr;

	// EntityからDTOへ変換するコンストラクタ
	public InventoryStockViewDto(InventoryStock stock) {
		this.id = stock.getId();
		this.materialName = stock.getMaterialName();
		this.origin = stock.getOrigin();
		this.lotNumber = stock.getLotNumber();
		this.unit = stock.getUnit();
		this.expiryDate = stock.getExpiryDate();

		// 1. 単位が存在する場合は、数値の後ろに付ける用の文字列を用意する（例: " g"）
		String unitStr = (this.unit != null) ? " " + this.unit : "";

		// 2.. フォーマットの準備 (3桁カンマ区切り)
		DecimalFormat df = new DecimalFormat("#,###");

		// 3. df.fromat()を試用して成型
		this.netWeightStr = stock.getNetWeight() != null
				? df.format(stock.getNetWeight()) + unitStr
				: "-";

		this.quantityStr = stock.getQuantity() != null
				? df.format(stock.getQuantity()) + unitStr
				: "-";
	}
}
