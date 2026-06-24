package com.example.app.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.ProductionOrder;

@Mapper
public interface ProductionOrderMapper {

	// 特定の製品で、現在「製造中(MANUFACTURING)」ステータスのオーダーを全件取得
	List<ProductionOrder> findManufacturingOrdersByItemId(@Param("itemId") Long itemId);

	// ロット番号をキーに製造データ１件取得
	ProductionOrder findByLotNumber(@Param("lotNumber") String lotNumber);

	// 製造予定の新規登録（受注からの自動生成・手動追加の両方で使用）
	void insertProductionOrder(ProductionOrder productionOrder);

	// 自動発行されている予定の計算
	BigDecimal sumManufacturingQuantityByItemId(@Param("itemId") Long itemId);

	// ---------------- パズル画面用 -----------------

	// パズルボードに乗せる全予定(DRAFT と PLANNING)を取得
	List<ProductionOrder> findSchedulingOrders();

	// 1.IDでオーダー1件を取得(状態確認用)
	ProductionOrder findById(@Param("id") Long id);

	// 2.仮置き用：予定日(scheduledDate)だけを更新
	// wasOverdue: 移動前に「期限切れ」だった場合はtrueを渡し、履歴として残す（一度trueになったら以後falseに戻らない）
	void updateScheduledDate(@Param("id") Long id, @Param("scheduledDate") LocalDate scheduledDate,
			@Param("wasOverdue") boolean wasOverdue);

	//3.確定：予定日、ステータス、確定したロット番号を更新
	void updateScheduleAndLot(@Param("id") Long id,
			@Param("scheduledDate") LocalDate scheduledDate,
			@Param("status") String status,
			@Param("lotNumber") String lotNumber);
}
