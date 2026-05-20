package com.example.app.domain;

import java.math.BigDecimal;

import lombok.Data;

// 原料・添加物の基本情報クラス

@Data
public class Material {
	private Long id;
	private String name;
	private String unit; // 数量
	private BigDecimal netWeight; // 1つあたりの賞味量 
}
