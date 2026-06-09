package com.example.app.service;

import java.util.List;
import java.util.Optional;

import com.example.app.domain.Material;
import com.example.app.dto.MaterialForm;

public interface MaterialService {

	// 有効な原料・添加物の一覧
	List<Material> getMaterialsByType(String materialType);

	// 理論削除された一覧取得
	List<Material> findDeletedMaterialsByType(String materialType);

	// IDで1件取得
	Optional<Material> getMaterialById(Long id);

	// 原料・添加物の登録
	void registerMaterial(Material material);

	// 編集・更新
	void updateMaterial(Material material);

	// 理論削除
	void logicalDelete(Long id);

	// 復旧用
	void restore(Long id);

	void registerMaterialFromForm(MaterialForm form);
}
