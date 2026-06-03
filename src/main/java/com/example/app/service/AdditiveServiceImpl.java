package com.example.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Additive;
import com.example.app.dto.AdditiveForm;
import com.example.app.mapper.AdditiveMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdditiveServiceImpl implements AdditiveService {

	private final AdditiveMapper additiveMapper;

	@Override
	public List<Additive> getAllAdditives() {
		return additiveMapper.findAll();
	}

	@Override
	public Optional<Additive> getAdditiveById(Long id) {
		return Optional.ofNullable(additiveMapper.findById(id));
	}

	@Override
	public void registerAdditive(Additive additive) {
		additiveMapper.insert(additive);
	}

	@Override
	public void updateAdditive(Additive additive) {
		additiveMapper.update(additive);
	}

	@Override
	public void logicalDelete(Long id) {
		additiveMapper.logicalDelete(id);
	}

	@Override
	public List<Additive> findDeletedAdditives() {
		return additiveMapper.findDeletedAdditives();
	}

	@Override
	public void restore(Long id) {
		additiveMapper.restore(id);
	}

	@Override
	public void registerAdditiveFromForm(AdditiveForm form) {
		additiveMapper.insert(form.toEntity());

	}

}
