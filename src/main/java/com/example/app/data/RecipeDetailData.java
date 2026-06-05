package com.example.app.data;

import java.math.BigDecimal;

import lombok.Data;

// 1行分（1つの材料）のデータを表すクラス
@Data
public class RecipeDetailData {
	private Long materialId;
	private String origin;
	private BigDecimal quantity;
}