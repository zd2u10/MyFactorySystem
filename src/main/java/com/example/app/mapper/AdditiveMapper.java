package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Additive;

@Mapper
public interface AdditiveMapper {

	// 一覧
	List<Additive> findAll();

	// 削除一覧
	List<Additive> findDeletedAdditives();

	// ID1件
	Additive findById(Long id);

	// 登録
	void insert(Additive additive);

	// 更新
	void update(Additive additive);

	// 理論削除
	void logicalDelete(Long id);

	// 復旧
	void restore(Long id);
}
