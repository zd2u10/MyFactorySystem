package com.example.app.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.app.domain.Recipe;

public interface RecipeService {

	// 加水率・量を算出するメソッド
	public String calculateWaterRange(Long itemId, List<Recipe> recipes) {
   // 1. 固形原料（米粉、α米など）の合計重量を算出
   BigDecimal totalSolidWeight = recipes.stream()
       .filter(r -> r.getIsSolid()) // 固形フラグまたはIDで判定
       .map(Recipe::getQuantity)
       .reduce(BigDecimal.ZERO, BigDecimal::add);

   // 2. 加水率（%）を取得（DBから）
   BigDecimal minRate = product.getMinHydrationRate().divide(new BigDecimal("100"));
   BigDecimal maxRate = product.getMaxHydrationRate().divide(new BigDecimal("100"));

   // 3. 加水量を算出
   BigDecimal minWater = totalSolidWeight.multiply(minRate);
   BigDecimal maxWater = totalSolidWeight.multiply(maxRate);

   return String.format("%d%%～%d%%： %dml～%dml", 
                        product.getMinHydrationRate().intValue(), 
                        product.getMaxHydrationRate().intValue(),
                        minWater.intValue(), 
                        maxWater.intValue());
}
}
