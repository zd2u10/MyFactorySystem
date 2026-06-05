package com.example.app.data;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

// レシピ登録画面全体を受け取るクラス
@Data
public class RecipeRegisterData {

	// どの商品のレシピか
	private Long itemId;

	// 画面から送られてくる複数行の素材リスト
	private List<RecipeDetailData> recipeList = new ArrayList<>();
}