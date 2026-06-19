package com.example.app.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Order {
	private Long id; // 注文番号
	private LocalDate orderDate; // 受注日（自動付与）
	private String customerName; // 注文者名（自由入力、ユーザー管理機能なし）
	private String status; // OPEN / SHIPPED / CANCELLED
	private LocalDateTime createdAt;
}
