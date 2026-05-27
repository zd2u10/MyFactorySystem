package com.example.app.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Item;

@Mapper
public interface ItemMapper {
	// 商品一覧取得
	List<Item> findAll();

	// idから1件取得
	Optional<Item> findById(Long id);

	// 商品をリストに追加
	void insert(Item item);

	// 商品情報を更新
	void update(Item item);

	// 商品の理論削除
	void logicalDelete(Long id);

	// 削除済み一覧の
	List<Item> findDeletedItem();

	// 理論削除からの復旧
	void restore(Long id);

}
