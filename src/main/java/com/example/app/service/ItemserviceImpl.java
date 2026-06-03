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
public class ItemServiceImpl implements ItemService {
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
	public void registerItem(Item item) {
		itemMapper.insert(item);
	}

	// 更新
	@Override
	public void updateItem(Item item) {
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

	@Override
	public void registerItemFromForm(ItemForm form) {
		itemMapper.insert(form.toEntity());
	}

}
