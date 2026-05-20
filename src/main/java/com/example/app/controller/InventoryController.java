package com.example.app.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

	private final InventoryService inventoryService;

	/*
	 * 原料消費処理
	 * POST /api/inventory/consume?materialId=1&quantity=10.5&productionDate=2026-05-20
	 */

	@PostMapping("/consume")
	public String consume(
			@RequestParam Long materialId,
			@RequestParam BigDecimal quantity,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate productionDate) {
		try {
			inventoryService.consumeMaterial(materialId, quantity, productionDate);
			return "消費処理が正常に完了しました。";
		} catch (RuntimeException e) {
			// 在庫不足エラーなどをキャッチして画面に表示
			return "エラーが発生しました" + e.getMessage();
		}
	}

}