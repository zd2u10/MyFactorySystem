package com.example.app.service;

import java.util.List;
import java.util.Optional;

import com.example.app.domain.Additive;
import com.example.app.dto.AdditiveForm;

public interface AdditiveService {

	List<Additive> getAllAdditives();

	Optional<Additive> getAdditiveById(Long id);

	void registerAdditive(Additive additive);

	void updateAdditive(Additive additive);

	void logicalDelete(Long id);

	List<Additive> findDeletedAdditives();

	void restore(Long id);

	void registerAdditiveFromForm(AdditiveForm form);
}
