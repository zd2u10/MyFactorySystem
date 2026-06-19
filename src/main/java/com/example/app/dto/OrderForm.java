package com.example.app.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class OrderForm {
	private String customerName; // 注文者名

	// 商品行（画面側でJSにより動的に追加される。items[0], items[1]... という名前でバインドされる）
	private List<OrderItemForm> items = new ArrayList<>();
}
