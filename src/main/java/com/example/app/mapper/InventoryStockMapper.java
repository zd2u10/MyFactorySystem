package com.example.app.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.InventoryStock;

@Mapper
public interface InventoryStockMapper {

	// 入荷登録（同一 material_id + lot_number + origin なら数量加算）
	void upsertStock(InventoryStock stock);

	/**
	 * レシピ登録画面用：材料に紐づく在庫の産地一覧を重複なしで取得
	 * origin が null のものは除外（産地未設定在庫は産地不問扱いのため選択肢に出さない）
	 */
	List<String> findDistinctOriginsByMaterialId(@Param("materialId") Long materialId);

	/**
	 * 製造時の在庫自動選択
	 * - recipe_originsに産地登録あり → その産地のみ対象
	 * - recipe_originsが0件（allowedOrigins = null or 空）→ 全産地対象
	 * - 賞味期限の古い順、次に入荷日の古い順（FIFO）
	 * - available(= quantity - reserved_quantity) > 0 のもののみ
	 */
	List<InventoryStock> findAvailableStocksForProduction(
			@Param("materialId") Long materialId,
			@Param("allowedOrigins") List<String> allowedOrigins);

	// 消費時に使うFIFO取得（既存・互換維持）
	List<InventoryStock> findByMaterialIdOrderByCreatedAtAsc(Long materialId);

	// 実在庫を更新（本消費・廃棄時）
	void updateQuantity(InventoryStock stock);

	// 仮消費ロック量を更新（製造開始・完了・廃棄時）
	void updateReservedQuantity(
			@Param("id") Long id,
			@Param("delta") BigDecimal delta // 正=ロック増加、負=ロック解除
	);

	/**
	 * 原料タイプ（RAW/ADDITIVE）で在庫一覧を取得
	 */
	List<InventoryStock> findAllStocksWithMaterialType(@Param("materialType") String materialType);
}
