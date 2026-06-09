package com.example.app.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Recipe {
	private Long id;
	private Long itemId; // idと紐づく製品名
	private Long materialId; // idと紐づく原料名
	private String origin; // 産地
	private BigDecimal quantity; // 使用量

	// 結合先の情報(一覧表示などで商品名や原料名を表示するため)
	private Item item;

	private String materialName;
	private String unit;
	private Boolean isPowder;
}
