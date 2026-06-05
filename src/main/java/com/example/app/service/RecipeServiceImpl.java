package com.example.app.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.data.RecipeRegisterData;
import com.example.app.domain.Item;
import com.example.app.domain.Recipe;
import com.example.app.mapper.ItemMapper;
import com.example.app.mapper.RecipeMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeServiceImpl implements RecipeService {

	private final RecipeMapper recipeMapper;
	private final ItemMapper itemMapper;

	@Override
	public List<Recipe> findByItemId(Long itemId) {
		return recipeMapper.findByItemId(itemId);
	}

	@Override
	public void registerRecipe(RecipeRegisterData data) {
		data.getRecipeList().forEach(detail -> recipeMapper.insert(data.getItemId(), detail));
	}

	@Override
	public String calculateWaterRange(Long itemId, List<Recipe> recipes) {
		// 1. Optionalの処理を修正
		Item item = itemMapper.findById(itemId)
				.orElseThrow(() -> new RuntimeException("該当する商品が見つかりません: " + itemId));

		// 2. 粉体(is_powder = true) の合計重量を算出
		// ※ Recipeクラスに boolean isPoder フィールドと getter が必要
		BigDecimal totalPowder = recipes.stream()
				.filter(r -> Boolean.TRUE.equals(r.getIsPowder()))
				.map(Recipe::getQuantity)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		// 3. 計算処理
		BigDecimal minRate = item.getMinHydrationRate().divide(new BigDecimal("100"));
		BigDecimal maxRate = item.getMaxHydrationRate().divide(new BigDecimal("100"));

		BigDecimal minWater = totalPowder.multiply(minRate);
		BigDecimal maxWater = totalPowder.multiply(maxRate);

		return String.format("%d%%～%d%%： %dml～%dml",
				item.getMinHydrationRate().intValue(),
				item.getMaxHydrationRate().intValue(),
				minWater.intValue(),
				maxWater.intValue());
	}

}
