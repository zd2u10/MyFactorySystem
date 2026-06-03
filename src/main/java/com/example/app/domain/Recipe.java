package com.example.app.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Recipe {
	private Long id;
	private Long itemId; // idと紐づく製品名
	private Long materialId; // idと紐づく原料名
	private BigDecimal quantity; // 使用量
}
