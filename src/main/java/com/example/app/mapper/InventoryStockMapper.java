package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.InventoryStock;

// inventory_stock に対応したmapper

@Mapper
public interface InventoryStockMapper {
	// 同じ原料・産地・ロットの在庫があれば増加、なければ追加
	void upsertStock(InventoryStock stock);

	// 消費時に「古い順」に在庫を取得するための検索
	// 消費時の引き当てに使用
	List<InventoryStock> findByMaterialIdOrderByCreatedAtAsc(Long materialId);

	// 指定したロットの数量を更新
	void updateQuantity(InventoryStock stock);
}
