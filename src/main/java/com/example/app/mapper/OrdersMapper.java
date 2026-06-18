package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Orders;

@Mapper
public interface OrdersMapper {

	// 一覧表示
	List<Orders> findAll();

	// 注文idで１件取得
}
