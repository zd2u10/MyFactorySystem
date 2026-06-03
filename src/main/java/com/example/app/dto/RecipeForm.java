package com.example.app.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import com.example.app.domain.Recipe;

import lombok.Data;

@Data
public class RecipeForm {

	private Long id;

	@NotNull(message = "製品を選択してください")
	private Long itemId;

	@NotNull(message = "原料を選択してください")
	private Long materialId;

	@NotNull(message = "使用量を入力してください")
	@DecimalMin(value = "0.01", message = "使用量は0.01以上で入力してください")
	private BigDecimal quantity;

	// EntityからFormへ値をコピーするメソッド
	public void copyFrom(Recipe recipe) {
		this.id = recipe.getId();
		this.itemId = recipe.getItemId();
		this.materialId = recipe.getMaterialId();
		this.quantity = recipe.getQuantity();
	}

	// FormからEntityへ変換するメソッド
	public Recipe toEntity() {
		Recipe recipe = new Recipe();
		recipe.setId(this.id);
		recipe.setItemId(this.itemId);
		recipe.setMaterialId(this.materialId);
		recipe.setQuantity(this.quantity);
		return recipe;
	}
}