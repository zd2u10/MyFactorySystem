package com.example.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Item;
import com.example.app.dto.ItemForm;
import com.example.app.mapper.ItemMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemserviceImpl implements ItemService {
	private final ItemMapper itemMapper;

	// 一覧取得
	@Override
	public List<Item> getAllItems() {
		return itemMapper.findAll();
	}

	// ID情報を1件取得
	@Override
	public Optional<Item> getItemById(Long id) {
		return itemMapper.findById(id);
	}

	// 登録
	@Override
	public void registerItem(ItemForm form) {
		itemMapper.insert(form.toEntity());
	}

	// 更新
	@Override
	public void updateItem(Long id, ItemForm form) {
		Item item = form.toEntity();
		item.setId(id);
		itemMapper.update(item);
	}

	// 論理削除
	@Override
	public void logicalDelete(Long id) {
		itemMapper.logicalDelete(id);
	}

	// 削除一覧
	@Override
	public List<Item> findDeletedItem() {
		return itemMapper.findDeletedItem();
	}

	//復元処理
	@Override
	public void restoreItem(Long id) {
		itemMapper.restore(id);
	}

}
