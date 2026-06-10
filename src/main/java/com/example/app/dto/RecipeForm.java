package com.example.app.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import com.example.app.domain.Recipe;

import lombok.Data;

@Data
public class RecipeForm {

    private Long id;

    @NotNull(message = "原料を選択してください")
    private Long materialId;

    @NotNull(message = "使用量を入力してください")
    @DecimalMin(value = "1", message = "使用量は1以上で入力してください")
    private BigDecimal quantity;

    // 使用可能産地（複数選択可、空 = 産地不問）
    private List<String> allowedOrigins;

    public Recipe toDomain() {
        Recipe recipe = new Recipe();
        recipe.setId(this.id);
        recipe.setMaterialId(this.materialId);
        recipe.setQuantity(this.quantity);
        recipe.setAllowedOrigins(this.allowedOrigins);
        return recipe;
    }

    public void copyFrom(Recipe recipe) {
        this.id = recipe.getId();
        this.materialId = recipe.getMaterialId();
        this.quantity = recipe.getQuantity();
        this.allowedOrigins = recipe.getAllowedOrigins();
    }
}
