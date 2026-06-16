package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.InventoryTransaction;
import com.example.app.dto.TransactionListDto;

@Mapper
public interface InventoryTransactionMapper {

	// 入出庫の履歴を1件登録
	void insert(InventoryTransaction transaction);

	// 一覧取得
	List<TransactionListDto> findAll();

}