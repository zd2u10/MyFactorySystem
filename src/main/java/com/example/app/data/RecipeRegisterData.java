package com.example.app.data;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;

import com.example.app.dto.RecipeForm;

import lombok.Data;

// レシピ登録画面全体を受け取るクラス
@Data
public class RecipeRegisterData {

	// どの商品のレシピか
	private Long itemId;

	// 画面から送られてくる複数行の素材リスト
	@Valid
	private List<RecipeForm> recipeList = new ArrayList<>();

	private java.math.BigDecimal waterAmount;
}