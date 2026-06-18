package com.example.app.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ProductionOrder {
	private Long id;
	private Long itemId;
	private String lotNumber;
	private BigDecimal quantity;
	private LocalDate productionDate; // 仮製造中(計画時)になった日
	private String status; // "MANUFACTURING"(製造中), "COMPLETED"(完了)
}
