package com.example.app.service;

import java.util.List;

import com.example.app.data.RecipeRegisterData;
import com.example.app.domain.Recipe; // 必要に応じて作成してください

public interface RecipeService {
	// 特定商品のレシピ一覧を取得
	List<Recipe> findByItemId(Long itemId);

	// レシピ登録（複数の行をまとめて保存）
	void registerRecipe(RecipeRegisterData data);

	// 加水率計算用メソッド
	String calculateWaterRange(Long itemId, List<Recipe> recipes);
}
