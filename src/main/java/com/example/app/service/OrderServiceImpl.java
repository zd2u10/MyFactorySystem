package com.example.app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Order;
import com.example.app.domain.OrderItem;
import com.example.app.dto.OrderForm;
import com.example.app.dto.OrderItemDetailDto;
import com.example.app.dto.OrderItemForm;
import com.example.app.dto.OrderListDto;
import com.example.app.mapper.OrderMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderMapper orderMapper;
	private final ProductionOrderService productionOrderService;

	@Override
	public List<OrderListDto> getOrderList(String status) {
		return orderMapper.findOrderList(status);
	}

	@Override
	public Order getOrderById(Long id) {
		return orderMapper.findOrderById(id);
	}

	@Override
	public List<OrderItemDetailDto> getOrderItems(Long orderId) {
		return orderMapper.findOrderItemsByOrderId(orderId);
	}

	// 受注登録（ヘッダー＋明細を1トランザクションで登録し、不足していれば製造予定も自動生成する）
	@Override
	@Transactional
	public void createOrder(OrderForm form) {
		if (form.getCustomerName() == null || form.getCustomerName().isBlank()) {
			throw new RuntimeException("注文者名を入力してください。");
		}

		// 商品が選択されていない行・数量未入力の行は無視する
		List<OrderItemForm> validItems = form.getItems().stream()
				.filter(i -> i.getItemId() != null
						&& i.getQuantity() != null
						&& i.getQuantity().compareTo(BigDecimal.ZERO) > 0)
				.collect(Collectors.toList());

		if (validItems.isEmpty()) {
			throw new RuntimeException("商品を1件以上、数量とともに入力してください。");
		}

		Order order = new Order();
		order.setOrderDate(LocalDate.now());
		order.setCustomerName(form.getCustomerName());
		order.setStatus("OPEN");
		orderMapper.insertOrder(order); // useGeneratedKeysによりorder.getId()が埋まる

		for (OrderItemForm itemForm : validItems) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderId(order.getId());
			orderItem.setItemId(itemForm.getItemId());
			orderItem.setQuantity(itemForm.getQuantity());
			orderMapper.insertOrderItem(orderItem);
		}

		// 受注により有効在庫が減るため、対象itemごとに製造予定の自動生成判定を行う
		// (同じ注文内に同じ商品が複数行あっても1回だけ判定すればよいのでdistinct)
		validItems.stream()
				.map(OrderItemForm::getItemId)
				.distinct()
				.forEach(productionOrderService::checkAndGenerateForItem);
	}

	@Override
	@Transactional
	public void updateOrderStatus(Long orderId, String newStatus) {
		orderMapper.updateStatus(orderId, newStatus);
	}
}

