package com.example.app.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.ItemStock;
import com.example.app.dto.ItemStockDto;

@Mapper
public interface ItemStockMapper {
	// 一覧取得
	List<ItemStockDto> findAllItemStock();

	//ロット番号と製品IDで既存の在庫を探す
	ItemStock findStockByItemIdAndLotNumber(@Param("itemId") Long itemId, @Param("lotNumber") String lotNumber);

	// ロット一覧取得用
	List<ItemStock> findStocksByItemId(@Param("itemId") Long itemId);

	// 在庫IDで単一の在庫を探す
	ItemStock findById(@Param("id") Long id);

	// 新規ロットの追加
	void insertStock(ItemStock itemStock);

	// 既存ロットの数量を加算する
	void addQuantity(@Param("id") Long id, @Param("addAmount") BigDecimal addAmount);

	// 既存ロットの数量を減算・上書きする
	void updateQuantity(@Param("id") Long id, @Param("newQuantity") BigDecimal newQuantity);

}
