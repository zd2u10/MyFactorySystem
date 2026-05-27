package com.example.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Material;
import com.example.app.dto.MaterialForm;
import com.example.app.mapper.MaterialMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialServiceImpl implements MaterialService {

	private final MaterialMapper materialMapper;

	// 一覧
	@Override
	public List<Material> getAllMaterials() {
		return materialMapper.findAll();
	}

	// 登録
	@Override
	@Transactional
	public void registerMaterial(Material material) {
		materialMapper.insert(material);
	}

	// 検索
	@Override
	public Optional<Material> getMaterialById(Long id) {
		return materialMapper.findById(id);
	}

	// 編集・更新
	@Override
	public void updateMaterial(Material material) {
		materialMapper.update(material);
	}

	// 理論削除
	@Override
	public void logicalDelete(Long id) {
		materialMapper.logicalDelete(id);
	}

	// 理論削除された一覧取得
	@Override
	public List<Material> findDeletedMaterials() {
		return materialMapper.findDeletedMaterials();
	}

	// 削除されたものを復元
	@Override
	public void restore(Long id) {
		materialMapper.restore(id);
	}

	@Override
	public void registerMaterialFromForm(MaterialForm form) {
		materialMapper.insert(form.toEntity());
	}

}
