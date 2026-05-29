package com.example.app.dto;

import jakarta.validation.constraints.NotBlank;

import com.example.app.domain.Additive;

import lombok.Data;

@Data
public class AdditiveForm {

	private Long id;

	@NotBlank(message = "名前を入力して下さい")
	private String name;

	@NotBlank(message = "単位を入力して下さい")
	private String unit; // 数量

	// Entityへの変換
	public Additive toEntity() {
		Additive additive = new Additive();
		additive.setId(this.id);
		additive.setName(this.name);
		additive.setUnit(this.unit);
		return additive;
	}

	// Entityからのコピー
	public void copyFrom(Additive additive) {
		this.id = additive.getId();
		this.name = additive.getName();
		this.unit = additive.getUnit();
	}

}
