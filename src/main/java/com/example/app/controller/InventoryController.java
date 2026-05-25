package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.dto.InventoryForm;
import com.example.app.service.InventoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

	private final InventoryService inventoryService;

	// 在庫操作ページ表示用
	@GetMapping("/operation")
	public String showOperationPage(Model model) {
		model.addAttribute("inventoryForm", new InventoryForm());

		// 将来的にDBから原料リストを取得して画面に渡す
		// list = inventoryService.getAllMaterials();
		// model.addAttribute("materialList", list);

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
