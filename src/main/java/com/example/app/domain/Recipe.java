package com.example.app.domain;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class Recipe {
	private Long id;
	private Long itemId;
	private Long materialId;
	private BigDecimal quantity;

	private BigDecimal minWaterAmount;
	private BigDecimal maxWaterAmount;

	private BigDecimal minHydrationRate;
	private BigDecimal maxHydrationRate;

	// 結合先の情報（一覧表示用）
	private String materialName;
	private String unit;
	private Boolean isPowder;

	// 使用可能産地リスト（recipe_originsから取得）
	// 空リスト = 産地不問
	private List<String> allowedOrigins;
}
