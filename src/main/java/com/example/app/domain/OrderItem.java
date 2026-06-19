package com.example.app.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderItem {
	private Long id;
	private Long orderId; // ordersテーブルのIDと紐づけ
	private Long itemId; // itemsテーブルのIDと紐づけ
	private BigDecimal quantity; // 受注数量
	private BigDecimal shippedQuantity; // 出荷済数量（出荷機能実装まで未使用、DB側でDEFAULT 0）
	private LocalDateTime createdAt;
}
