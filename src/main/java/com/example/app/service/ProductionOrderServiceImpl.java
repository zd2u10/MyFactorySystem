package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.app.domain.ProductionOrder;
import com.example.app.mapper.ProductionOrderMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductionOrderServiceImpl implements ProductionOrderService {

	private final ProductionOrderMapper productionOrderMapper;

	@Override
	public List<ProductionOrder> findManufacturingOrdersByItemId(Long itemId) {
		return productionOrderMapper.findManufacturingOrdersByItemId(itemId);
	}

	@Override
	public ProductionOrder findByLotNumber(String lotNumber) {
		return productionOrderMapper.findByLotNumber(lotNumber);
	}
}
