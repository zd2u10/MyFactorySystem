package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Material;
import com.example.app.mapper.MaterialMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

	private final MaterialMapper materialMapper;

	@Override
	public List<Material> getAllMaterials() {
		return materialMapper.findAll();
	}

	@Override
	@Transactional
	public void registerMaterial(Material material) {
		materialMapper.insert(material);
	}

}
