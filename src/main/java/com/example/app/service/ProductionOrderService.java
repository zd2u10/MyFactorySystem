package com.example.app.service;

import java.time.LocalDate;
import java.util.List;

import com.example.app.domain.ProductionOrder;

public interface ProductionOrderService {
	// 製造中のオーダー一覧を取得
	List<ProductionOrder> findManufacturingOrdersByItemId(Long itemId);

	// ロット番号からオーダーを1件取得
	ProductionOrder findByLotNumber(String lotNumber);

	// 有効在庫が適正在庫を下回っていれば、不足分の製造予定(DRAFT)を自動生成する
	void checkAndGenerateForItem(Long itemId);

	// ----------------- 予定パズル用 ------------------------

	// パズルボード表示用の予定一覧を取得
	List<ProductionOrder> findSchedulingOrders();

	// 仮置き用：パズルを移動した時（日付だけ更新）
	void updateScheduledDateOnly(Long orderId, LocalDate scheduledDate);

	// 確定用：画面で「確定」ボタンを押した時（ロット発番）
	String confirmSchedule(Long orderId);

}
