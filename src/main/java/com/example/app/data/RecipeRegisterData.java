package com.example.app.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;

import com.example.app.dto.RecipeForm;

import lombok.Data;

@Data
public class RecipeRegisterData {

	private Long itemId;

	@Valid
	private List<RecipeForm> recipeList = new ArrayList<>();

	// 加水量（レシピ全体で1つ）
	private BigDecimal minWaterAmount;
	private BigDecimal maxWaterAmount;

	// 加水率の上下限（レシピ登録時に設定）
	private BigDecimal minHydrationRate;
	private BigDecimal maxHydrationRate;
}
