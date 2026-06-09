package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Recipe;
import com.example.app.dto.RecipeForm;

@Mapper
public interface RecipeMapper {

	List<Recipe> findByItemId(Long itemId);

	void insert(
			@Param("itemId") Long itemId,
			@Param("detail") RecipeForm detail);
}