package com.example.app.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDetailDto {
	private Long id;
	private Long itemId;
	private String itemName;
	private String salesUnit;
	private BigDecimal quantity;
	private BigDecimal shippedQuantity;
}
