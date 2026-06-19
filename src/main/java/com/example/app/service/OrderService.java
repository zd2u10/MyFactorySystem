package com.example.app.service;

import java.util.List;

import com.example.app.domain.Order;
import com.example.app.dto.OrderForm;
import com.example.app.dto.OrderItemDetailDto;
import com.example.app.dto.OrderListDto;

public interface OrderService {
	// 一覧/履歴取得（statusがnullなら全件、'OPEN'なら未出荷のみ）
	List<OrderListDto> getOrderList(String status);

	// 受注登録（ヘッダー＋明細をまとめて登録）
	void createOrder(OrderForm form);

	// 受注ヘッダー1件取得
	Order getOrderById(Long id);

	// 受注明細一覧取得
	List<OrderItemDetailDto> getOrderItems(Long orderId);

	// ステータス更新
	void updateOrderStatus(Long OrderId, String newStatus);
}
