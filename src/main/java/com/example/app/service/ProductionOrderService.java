package com.example.app.service;

import java.util.List;

import com.example.app.domain.ProductionOrder;

public interface ProductionOrderService {
	// 製造中のオーダー一覧を取得
	List<ProductionOrder> findManufacturingOrdersByItemId(Long itemId);

	// ロット番号からオーダーを1件取得
	ProductionOrder findByLotNumber(String lotNumber);
}
