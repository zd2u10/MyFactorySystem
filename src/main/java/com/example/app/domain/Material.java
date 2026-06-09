package com.example.app.domain;

import lombok.Data;

// 原料・添加物の基本情報クラス

@Data
public class Material {
	private Long id;
	private String name;
	private String unit; // 単位
	private Boolean isPowder;
	private String materialType;

	public Boolean getIsPowder() {
		return this.isPowder;
	}
}
