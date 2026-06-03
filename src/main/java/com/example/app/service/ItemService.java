package com.example.app.service;

import java.util.List;
import java.util.Optional;

import com.example.app.domain.Item;
import com.example.app.dto.ItemForm;

public interface ItemService {
	//一覧取得
	List<Item> getAllItems();

	// 1件取得
	Optional<Item> getItemById(Long id);

	// 登録（DTOから変換して登録）
	void registerItem(Item item);

	// 更新（DTOから変換して更新）
	void updateItem(Item item);

	// 論理削除
	void logicalDelete(Long id);

	// 削除一覧
	List<Item> findDeletedItem();

	//復元処理
	void restoreItem(Long id);

	void registerItemFromForm(ItemForm form);
}
