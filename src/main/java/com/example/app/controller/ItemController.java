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
		return "items/list";
	}

	// 2.登録画面の表示
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("itemForm", new ItemForm());
		return "items/register";
	}

	// 3. 登録処理
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("itemForm") ItemForm form,
			BindingResult result) {
		// バリデーションエラーがあれば登録画面に返す
		if (result.hasErrors()) {
			return "items/register";
		}

		itemService.registerItem(form);
		return "redirect:/items/list";
	}

	// 4.編集画面表示
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		itemService.getItemById(id).ifPresentOrElse(
				item -> {
					// EntityをFormに詰め替える
					ItemForm form = new ItemForm();
					form.copyFrom(item); // 詰め替えのロジックはItemFormが持つ
					model.addAttribute("itemForm", form);
					model.addAttribute("id", id);
				},
				() -> {
					throw new IllegalArgumentException("Invalid item Id:" + id);
				});
		return "items/edit";
	}

	// 5.更新処理
	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id,
			@Valid @ModelAttribute("itemForm") ItemForm form,
			BindingResult result) {
		if (result.hasErrors()) {
			return "items/edit";
		}
		itemService.updateItem(id, form);
		return "redirect:/items/list";
	}

	// 6.論理削除処理
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		itemService.logicalDelete(id);
		return "redirect:/items/list";
	}

	// 7.削除一覧表示
	@GetMapping("/deleted")
	public String showDeletedItem(Model model) {
		model.addAttribute("itemList", itemService.findDeletedItem());
		return "items/deleted";
	}

	//8. 復元処理
	@PostMapping("/deleted/{id}")
	public String restore(@PathVariable Long id) {
		itemService.restoreItem(id);
		return "redirect:/items/list";
	}

}
