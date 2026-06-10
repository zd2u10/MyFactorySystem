package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Recipe;
import com.example.app.dto.RecipeForm;

@Mapper
public interface RecipeMapper {

	// レシピ一覧（allowedOriginsは別クエリでロード）
	List<Recipe> findByItemId(Long itemId);

	// レシピ1件のallowedOriginsを取得
	List<String> findAllowedOriginsByRecipeId(Long recipeId);

	// レシピ行の登録
	void insert(
			@Param("itemId") Long itemId,
			@Param("minWater") java.math.BigDecimal minWater,
			@Param("maxWater") java.math.BigDecimal maxWater,
			@Param("minRate") java.math.BigDecimal minRate,
			@Param("maxRate") java.math.BigDecimal maxRate,
			@Param("detail") RecipeForm detail);

	// recipe_origins の登録（1件ずつ）
	void insertOrigin(
			@Param("recipeId") Long recipeId,
			@Param("origin") String origin);

	// 登録時に採番されたIDを取得するため useGeneratedKeys を XML側で設定
}
