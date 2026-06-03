package com.example.app.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Item;
import com.example.app.dto.ItemForm;
import com.example.app.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

	private final ItemService itemService;

	// 1.一覧表示
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("itemList", itemService.getAllItems());
		model.addAttribute("currentPage", "list");
		return "items/list";
	}

	// 2.登録画面の表示
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("itemForm", new ItemForm());
		model.addAttribute("currentPage", "register");
		return "items/register";
	}

	// 3. 登録処理
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("itemForm") ItemForm form,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			// エラー時はフォームを保持して画面へ戻す
			model.addAttribute("currentPage", "register");
			return "items/register";
		}
		// 入力項目をformに一任
		itemService.registerItemFromForm(form);
		// 成功メッセージ
		redirectAttributes.addFlashAttribute("message", "商品を登録しました");
		return "redirect:/items/list";
	}

	// 4.編集画面表示
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		// 1. データ取得（フラットに記述）
		Item item = itemService.getItemById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));

		// 2. 詰め替え
		ItemForm form = new ItemForm();
		form.copyFrom(item);

		// 3. モデルへのセット
		model.addAttribute("itemForm", form);
		model.addAttribute("id", id);
		model.addAttribute("currentPage", "list");
		return "items/edit";
	}

	// 5.更新処理
	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id,
			@Valid @ModelAttribute("itemForm") ItemForm form,
			BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "items/edit";
		}

		// FormをEntityに変換
		Item item = form.toEntity();

		// Service経由で更新を実行
		itemService.updateItem(item);
		// 成功メッセージ
		redirectAttributes.addFlashAttribute("message", "更新しました");
		return "redirect:/item/list";
	}

	// 6.論理削除処理
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id,
			RedirectAttributes redirectAttributes) {
		itemService.logicalDelete(id);
		redirectAttributes.addFlashAttribute("message", "削除しました");
		return "redirect:/items/list";
	}

	// 7.削除一覧表示
	@GetMapping("/deleted")
	public String showDeletedItem(Model model) {
		model.addAttribute("itemList", itemService.findDeletedItem());
		model.addAttribute("currentPage", "deleted");
		return "items/deleted";
	}

	//8. 復元処理
	@PostMapping("/deleted/{id}")
	public String restore(@PathVariable Long id,
			RedirectAttributes redirectAttributes) {
		itemService.restoreItem(id);
		// 成功メッセージ
		redirectAttributes.addFlashAttribute("message", "復旧しました");
		return "redirect:/items/deleted";
	}

}
