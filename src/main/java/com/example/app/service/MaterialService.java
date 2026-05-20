package com.example.app.service;

import java.util.List;

import com.example.app.domain.Material;

public interface MaterialService {

	// 原料・添加物の一覧
	List<Material> getAllMaterials();

	// 原料・添加物の登録
	void registerMaterial(Material material);
}
