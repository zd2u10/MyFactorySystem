package com.example.app.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import com.example.app.domain.Recipe;

import lombok.Data;

// 入力フォームとのやり取りを担当
@Data
public class RecipeForm {

	private Long id;
	@NotNull(message = "製品を選択してください")

	private Long itemId;

	@NotNull(message = "原料を選択してください")
	private Long materialId;

	private String origin = "未設定";

	@NotNull(message = "使用量を入力してください")
	@DecimalMin(value = "1", message = "使用量は1以上で入力してください")
	private BigDecimal quantity;

	// Domainへ変換するメソッド
	public Recipe toDomain() {
		Recipe recipe = new Recipe();
		recipe.setId(this.id);
		recipe.setItemId(this.itemId);
		recipe.setMaterialId(this.materialId);
		recipe.setOrigin(this.origin);
		recipe.setQuantity(this.quantity);
		return recipe;
	}

	// Domainからのコピー
	public void copyFrom(Recipe recipe) {
		this.id = recipe.getId();
		this.itemId = recipe.getItemId();
		this.materialId = recipe.getMaterialId();
		this.origin = recipe.getOrigin();
		this.quantity = recipe.getQuantity();
	}

}