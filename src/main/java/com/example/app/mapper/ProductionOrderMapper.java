package com.example.app.mapper;

import java.math.BigDecimal;
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

	// 指定itemの「製造中(MANUFACTURING)」の合計予定数量（二重生成防止用）
	BigDecimal sumManufacturingQuantityByItemId(@Param("itemId") Long itemId);
}
