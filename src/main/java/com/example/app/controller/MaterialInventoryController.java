package com.example.app.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.domain.InventoryStock;
import com.example.app.dto.InventoryStockViewDto;
import com.example.app.service.inventory.MaterialInventoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inventory/material")
public class MaterialInventoryController {

	private final MaterialInventoryService inventoryService;

	@GetMapping("/list")
	public String list(@RequestParam(name = "type", defaultValue = "RAW") String type, Model model) {
		List<InventoryStock> stocks = inventoryService.getStocksByType(type);
		// DTOのリストに変換(Stream)
		List<InventoryStockViewDto> viewDtos = stocks.stream()
				.map(InventoryStockViewDto::new)
				.toList();

		model.addAttribute("stocks", viewDtos);
		model.addAttribute("currentType", type); // ボタンのactive切り替え用
		model.addAttribute("currentPage", "material");
		return "inventory/material/list";
	}
}