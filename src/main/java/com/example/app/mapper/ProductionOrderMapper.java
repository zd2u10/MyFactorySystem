package com.example.app.mapper;

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
}
