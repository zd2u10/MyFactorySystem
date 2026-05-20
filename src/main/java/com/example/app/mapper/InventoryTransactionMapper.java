package com.example.app.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.InventoryTransaction;

@Mapper
public interface InventoryTransactionMapper {

	// 入出庫の履歴を1件登録
	void insert(InventoryTransaction transaction);
}