package com.example.app.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemForm {
	private Long itemId;
	private BigDecimal quantity;
}
