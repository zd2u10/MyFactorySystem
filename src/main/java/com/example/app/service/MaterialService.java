package com.example.app.service;

import java.util.List;
import java.util.Optional;

import com.example.app.domain.Material;
import com.example.app.dto.MaterialForm;

public interface MaterialService {

	// 原料・添加物の一覧
	List<Material> getAllMaterials();

	// IDで1件取得
	Optional<Material> getMaterialById(Long id);

	// 原料・添加物の登録
	void registerMaterial(Material material);

	// 編集・更新
	void updateMaterial(Material material);

	// 理論削除
	void logicalDelete(Long id);

	// 理論削除された一覧取得
	List<Material> findDeletedMaterials();

	// 復旧用
	void restore(Long id);

	void registerMaterialFromForm(MaterialForm form);
}
