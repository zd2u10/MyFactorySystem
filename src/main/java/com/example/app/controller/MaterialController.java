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

import com.example.app.domain.Material;
import com.example.app.dto.MaterialForm;
import com.example.app.service.MaterialService;

import lombok.RequiredArgsConstructor;

// ビューへの操作を担うクラス
@Controller
@RequiredArgsConstructor
@RequestMapping("/material")
public class MaterialController {
	private final MaterialService materialService;

	@GetMapping("/list")
	public String list(Model model) {
		// 一覧
		model.addAttribute("materialList", materialService.getAllMaterials());
		model.addAttribute("currentPage", "list");
		return "material/list";
	}

	// 登録画面
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("material", new Material());
		model.addAttribute("currentPage", "register");
		return "material/register";
	}

	// 登録処理を実行
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("materialForm") MaterialForm form, BindingResult result) {
		if (result.hasErrors()) {
			return "material/register"; // エラーがあれば登録画面へ戻す
		}
		// 変換処理をServiceに任せる
		materialService.registerMaterialFromForm(form);
		return "redirect:/material/list";
	}

	// 編集
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		Material material = materialService.getMaterialById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid material Id:" + id));

		// DTOを作成し、Entityからコピー
		MaterialForm form = new MaterialForm();
		form.copyFrom(material);

		model.addAttribute("materialForm", form);
		model.addAttribute("id", id);
		model.addAttribute("currentPage", "list");
		return "material/edit";
	}

	// 編集処理
	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id,
			@Valid @ModelAttribute("materialForm") MaterialForm form,
			BindingResult result) {
		if (result.hasErrors()) {
			return "material/edit";
		}

		// FormをEntityに変換
		Material material = form.toEntity();

		// Service経由で更新を実行
		materialService.updateMaterial(material);

		return "redirect:/material/list";
	}

	// 理論削除処理
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		materialService.logicalDelete(id);
		return "redirect:/material/list";
	}

	//削除済み一覧表示用
	@GetMapping("/deleted")
	public String showDeletedList(Model model) {
		model.addAttribute("materialList", materialService.findDeletedMaterials());
		model.addAttribute("currentPage", "deleted");
		return "material/deleted";
	}

	//復旧処理用
	@PostMapping("/deleted/{id}")
	public String restore(@PathVariable Long id) {
		materialService.restore(id);
		return "redirect:/material/deleted";
	}
}