package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.dto.InventoryForm;
import com.example.app.mapper.InventoryStockMapper;
import com.example.app.service.InventoryService;
import com.example.app.service.MaterialService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

	private final InventoryService inventoryService;
	private final MaterialService materialService;
	private final InventoryStockMapper inventoryStockMapper;

	// 在庫確認ページを表示
	@GetMapping("/list")
	public String list(@RequestParam(name = "type", defaultValue = "RAW") String type, Model model) {
		// それぞれのタイプに応じてデータを取得
		model.addAttribute("stock", inventoryStockMapper.findAllStocksWithMaterialType("type"));
		model.addAttribute("currentType", type); // 画面でボタンを光らせるため
		return "inventory/list";
	}

	// 在庫操作ページ表示用
	@GetMapping("/operation")
	public String showOperationPage(Model model) {
		model.addAttribute("inventoryForm", new InventoryForm());
		// 原料"RAW"の一覧を取得して画面に渡す
		model.addAttribute("rawStock", materialService.getMaterialsByType("RAW"));
		// 添加物"ADDITIVE"の一覧を取得して画面に渡す
		model.addAttribute("additiveStock", materialService.getMaterialsByType("ADDITIVE"));

		return "inventory/operation";
	}

	// 入荷記録処理用
	@PostMapping("/operation")
	public String operation(@ModelAttribute InventoryForm form, Model model, RedirectAttributes redirectAttributes) {
		inventoryService.receiveMaterial(
				form.getMaterialId(),
				form.getLotNumber(),
				"デフォルト",
				form.getQuantity(),
				null,
				form.getTransactionDate(),
				true);
		redirectAttributes.addFlashAttribute("message", "入荷登録が完了しました");
		return "redirect:/inventory/operation";
	}

}
