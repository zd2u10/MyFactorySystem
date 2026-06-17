package com.example.app.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.app.service.ItemStockService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/inventory/item")
@RequiredArgsConstructor
public class ItemStockController {

	private final ItemStockService itemStockService;

	@GetMapping("/list")
	public String list(Model model) {

		model.addAttribute("stockList", itemStockService.getAllItemStock());
		return "inventory/item/list";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long itemId, Model model) {
		// 1. 製品情報の取得 (既存のItemServiceなどを使用想定)
		// Item item = itemService.findById(itemId);
		// model.addAttribute("item", item);

		// 2. この製品の現在のロット別在庫一覧を取得 (ItemStockServiceに追加想定)
		// List<ItemStock> currentStocks = itemStockService.findStocksByItemId(itemId);
		// model.addAttribute("currentStocks", currentStocks);

		// 3. 過去3日分のロット番号を自動生成 (ルール: YYYYMMDD-製品ID)
		List<String> selectableLots = new ArrayList<>();
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		for (int i = 0; i < 3; i++) {
			String dateStr = today.minusDays(i).format(formatter);
			selectableLots.add(dateStr + "-" + itemId);
		}
		model.addAttribute("selectableLots", selectableLots);

		return "inventory/item/edit";
	}

}
