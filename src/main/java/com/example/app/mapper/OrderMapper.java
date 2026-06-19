package com.example.app.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Order;
import com.example.app.domain.OrderItem;
import com.example.app.dto.OrderItemDetailDto;
import com.example.app.dto.OrderListDto;

@Mapper
public interface OrderMapper {

	// 受注ヘッダー登録
	void insertOrder(Order order);

	// 受注明細登録
	void insertOrderItem(OrderItem orderItem);

	// 一覧/履歴取得（statusがnullなら全件、'OPEN'なら未出荷のみ）
	List<OrderListDto> findOrderList(@Param("status") String status);

	// 受注ヘッダー1件取得
	Order findOrderById(@Param("id") Long id);

	// 受注明細一覧取得（商品名付き）
	List<OrderItemDetailDto> findOrderItemsByOrderId(@Param("orderId") Long orderId);

	// 指定itemの未出荷受注（status='OPEN'）の合計数量（製造予定自動生成の判定に使用）
	BigDecimal sumOpenQuantityByItemId(@Param("itemId") Long itemId);

	// 受注ステータス更新（出荷済/キャンセルなど）
	void updateStatus(@Param("orderId") Long orderId, @Param("newStatus") String newStatus);
}

