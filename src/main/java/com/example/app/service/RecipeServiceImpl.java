package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.data.RecipeRegisterData;
import com.example.app.domain.Recipe;
import com.example.app.dto.RecipeForm;
import com.example.app.mapper.RecipeMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeServiceImpl implements RecipeService {

	private final RecipeMapper recipeMapper;

	@Override
	public List<Recipe> findByItemId(Long itemId) {
		List<Recipe> recipes = recipeMapper.findByItemId(itemId);
		// 各レシピ行の使用可能産地リストをロード
		recipes.forEach(r -> r.setAllowedOrigins(recipeMapper.findAllowedOriginsByRecipeId(r.getId())));
		return recipes;
	}

	@Override
	public void registerRecipe(RecipeRegisterData data) {
		for (RecipeForm detail : data.getRecipeList()) {
			// 1. レシピ行を登録（useGeneratedKeys でdetail.idに採番値が入る）
			recipeMapper.insert(
					data.getItemId(),
					data.getMinWaterAmount(),
					data.getMaxWaterAmount(),
					data.getMinHydrationRate(),
					data.getMaxHydrationRate(),
					detail);

			// 2. 産地指定がある場合のみrecipe_originsに登録
			if (detail.getAllowedOrigins() != null) {
				for (String origin : detail.getAllowedOrigins()) {
					if (origin != null && !origin.isBlank()) {
						recipeMapper.insertOrigin(detail.getId(), origin);
					}
				}
			}
		}
	}

	@Override
	public String calculateWaterRange(Long itemId, List<Recipe> recipes) {
		// 加水率はrecipesから取得（全行で同じ値のはずなので先頭行を使用）
		Recipe first = recipes.stream()
				.filter(r -> r.getMinHydrationRate() != null && r.getMaxHydrationRate() != null)
				.findFirst()
				.orElse(null);

		if (first == null) {
			return "加水率未設定";
		}

		// DBに保存されている率と量をそのまま文字列にして返す
		return String.format("%d%%～%d%%： %dml～%dml",
				first.getMinHydrationRate().intValue(),
				first.getMaxHydrationRate().intValue(),
				first.getMinWaterAmount() != null ? first.getMinWaterAmount().intValue() : 0,
				first.getMaxWaterAmount() != null ? first.getMaxWaterAmount().intValue() : 0);
	}

}
