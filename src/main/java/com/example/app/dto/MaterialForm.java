package com.example.app.dto;

import jakarta.validation.constraints.NotBlank;

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

	private Boolean isPowder = false;

	private String materialType;

	// Entityへの変換
	public Material toEntity() {
		Material material = new Material();
		material.setId(this.id);
		material.setName(this.name);
		material.setUnit(this.unit);
		material.setIsPowder(this.isPowder);
		material.setMaterialType(this.materialType);
		return material;
	}

	// Entityからのコピー
	public void copyFrom(Material material) {
		this.id = material.getId();
		this.name = material.getName();
		this.unit = material.getUnit();
		this.isPowder = material.getIsPowder();
		this.materialType = material.getMaterialType();
	}

}
