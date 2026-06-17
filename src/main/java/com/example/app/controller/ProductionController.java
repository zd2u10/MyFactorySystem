/*
package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.service.ItemStockService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductionController {

	private final ItemStockService itemStockService;
	@PostMapping("/production/complete") // このようなURLを探してください
	public String completeProduction(@ModelAttribute("order") ProductionOrder order) {

		// 1. 製造オーダーのステータスを「完了」にする処理
		// ...

		// 2. ★ここで在庫に反映させる（今回追加したコード）
		itemStockService.addStock(
				order.getItemId(),
				order.getLotNumber(),
				order.getQuantity(),
				order.getProductionDate() // オーダーの日付を在庫に渡す
		);

		return "redirect:/production/list";
	}
}
*/