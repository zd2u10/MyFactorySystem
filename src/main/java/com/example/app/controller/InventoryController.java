package com.example.app.controller;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.InventoryStock;
import com.example.app.dto.ArrivalForm;
import com.example.app.service.InventoryService;
import com.example.app.service.MaterialService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

	private final InventoryService inventoryService;
	private final MaterialService materialService;

	// 在庫一覧ページ
	@GetMapping("/list")
	public String lis(@RequestParam(name = "type", defaultValue = "RAW") String type, Model model) {
		// 材料タイプに応じた在庫一覧を取得(service経由)
		List<InventoryStock> stocks = inventoryService.getStocksByType(type);

		model.addAttribute("stocks", stocks);
		model.addAttribute("currentType", type); // ボタンのactive切り替え用
		return "inventory/list";
	}

	// 入荷登録ページ表示
	@GetMapping("/arrival")
	public String showOperationPage(Model model) {
		model.addAttribute("arrivalForm", new ArrivalForm());
		// 全材料リスト（論理削除されていないもの）をセレクトボックス用に渡す
		model.addAttribute("rawList", materialService.getMaterialsByType("RAW"));
		model.addAttribute("additiveList", materialService.getMaterialsByType("ADDITIVE"));
		return "inventory/arrival";
	}

	// 入荷登録処理
	@PostMapping("/arrival")
	public String operation(
			@Valid @ModelAttribute ArrivalForm form,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			// バリデーションエラー時はフォームを保持して画面に戻す
			model.addAttribute("rawList", materialService.getMaterialsByType("RAW"));
			model.addAttribute("additiveList", materialService.getMaterialsByType("ADDITIVE"));
			return "inventory/arrival";
		}

		// Controller側で総重量(quantity)を安全に計算する
		BigDecimal totalQuantity = form.getNetWeight().multiply(form.getPackageCount());

		// 備考欄にに姿情報を追記する
		String autoNote = String.format("荷姿：%sg/ml x %s袋. %s",
				form.getNetWeight(),
				form.getPackageCount(),
				form.getNote() != null ? form.getNote() : "");

		// ServiceへnetWeightとtotalQuantityの両方を渡す
		inventoryService.receiveMaterial(
				form.getMaterialId(),
				form.getLotNumber(),
				form.getOrigin(), // 産地（nullまたは空文字も許容）
				form.getNetWeight(), // 1袋の内容量
				totalQuantity, // 計算後の総重量
				form.getExpiryDate(),
				form.getArrivalDate(),
				form.isInspected(),
				form.getNote()); // 荷姿情報を記録した備考

		redirectAttributes.addFlashAttribute("message", "入荷登録が完了しました");
		redirectAttributes.addFlashAttribute("msgType", "register");
		return "redirect:/inventory/list";
	}
}
