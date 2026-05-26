package com.example.app.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Material;

// Materialテーブルへのmapping

@Mapper
public interface MaterialMapper {
	// 原料・添加物の一覧表示
	List<Material> findAll();

	// IDごとにマテリアルを1件取得
	Optional<Material> findById(Long id);

	// 原料・添加物の追加
	void insert(Material material);

	// 編集・更新
	void update(Material material);

	// 理論削除
	void logicalDelete(Long id);

	// 理論削除済み一覧取得
	List<Material> findDeletedMaterials();

	// 削除済みを復旧する
	void restore(Long id);
}
