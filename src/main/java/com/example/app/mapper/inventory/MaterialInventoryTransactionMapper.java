package com.example.app.mapper.inventory;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.InventoryTransaction;
import com.example.app.dto.TransactionListDto;

@Mapper
public interface MaterialInventoryTransactionMapper {

	// 入出庫の履歴を1件登録
	void insert(InventoryTransaction transaction);

	// 一覧取得
	List<TransactionListDto> findTransactions(
			@Param("materialType") String materialType,
			@Param("transactionType") String transactionType);
}