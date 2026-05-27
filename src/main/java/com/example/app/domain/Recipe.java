package com.example.app.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Recipe {
	private Long id;
	private Long itemId;
	private Long materialId;
	private BigDecimal quantity;
}
