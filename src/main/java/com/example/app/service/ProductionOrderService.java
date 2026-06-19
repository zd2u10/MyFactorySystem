package com.example.app.service;

import java.util.List;

import com.example.app.domain.ProductionOrder;

public interface ProductionOrderService {
	// 製造中のオーダー一覧を取得
	List<ProductionOrder> findManufacturingOrdersByItemId(Long itemId);

	// ロット番号からオーダーを1件取得
	ProductionOrder findByLotNumber(String lotNumber);

	// 有効在庫が適正在庫を下回っていれば、不足分の製造予定(MANUFACTURING)を自動生成する
	void checkAndGenerateForItem(Long itemId);
}
