package com.example.app.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import com.example.app.domain.Material;

import lombok.Data;

// 入力フォーム(画面)とのやり取りを担当するクラス

@Data
public class MaterialForm {
	private Long id;

	@NotBlank(message = "名前を入力して下さい")
	private String name;

	@NotBlank(message = "単位を入力して下さい")
	private String unit; // 数量

	@NotNull(message = "重量を入力して下さい")
	@PositiveOrZero(message = "重量は0以上の数値を入力してください")
	private BigDecimal netWeight; // 1つあたりの賞味量

	// Entityへの変換
	public Material toEntity() {
		Material material = new Material();
		material.setId(this.id);
		material.setName(this.name);
		material.setUnit(this.unit);
		material.setNetWeight(this.netWeight);
		return material;
	}

	// Entityからのコピー
	public void copyFrom(Material material) {
		this.id = material.getId();
		this.name = material.getName();
		this.unit = material.getUnit();
		this.netWeight = material.getNetWeight();
	}

}
