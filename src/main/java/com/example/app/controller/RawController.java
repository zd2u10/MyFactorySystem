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

import com.example.app.domain.Material;
import com.example.app.dto.MaterialForm;
import com.example.app.service.MaterialService;

import lombok.RequiredArgsConstructor;

// ビューへの操作を担うクラス
@Controller
@RequiredArgsConstructor
@RequestMapping("/raw")
public class RawController {
	private final MaterialService materialService;

	@GetMapping("/list")
	public String list(Model model) {
		// 原料("RAW")のみを取得するように修正
		model.addAttribute("materialList", materialService.getMaterialsByType("RAW"));
		model.addAttribute("currentPage", "list");
		return "raw/list";
	}

	// 登録画面
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		// フォーム作成時にあらかじめタイプを"RAW"にセットしておく
		MaterialForm form = new MaterialForm();
		form.setMaterialType("RAW");
		model.addAttribute("rawForm", form);
		model.addAttribute("currentPage", "register");
		return "raw/register";
	}

	// 登録処理を実行
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("rawForm") MaterialForm form,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			// エラー時はフォームを保持して画面へ戻す
			model.addAttribute("currentPage", "register");
			return "raw/register"; // エラーがあれば登録画面へ戻す
		}
		// 変換処理をServiceに任せる
		materialService.registerMaterialFromForm(form);
		// 成功メッセージ
		redirectAttributes.addFlashAttribute("message", "原料情報を登録しました");
		redirectAttributes.addFlashAttribute("msgType", "register");
		return "redirect:/raw/list";
	}

	// 編集
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		Material material = materialService.getMaterialById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid material Id:" + id));

		// DTOを作成し、Entityからコピー
		MaterialForm form = new MaterialForm();
		form.copyFrom(material);

		model.addAttribute("rawForm", form);
		model.addAttribute("id", id);
		model.addAttribute("currentPage", "list");
		return "raw/edit";
	}

	// 編集処理
	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id,
			@Valid @ModelAttribute("rawForm") MaterialForm form,
			BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "raw/edit";
		}

		// FormをEntityに変換
		Material material = form.toEntity();

		// Service経由で更新を実行
		materialService.updateMaterial(material);
		// 成功メッセージ
		redirectAttributes.addFlashAttribute("message", "原料情報を更新しました");
		redirectAttributes.addFlashAttribute("msgType", "update");
		return "redirect:/raw/list";
	}

	// 理論削除処理
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id,
			RedirectAttributes redirectAttributes) {
		materialService.logicalDelete(id);
		redirectAttributes.addFlashAttribute("message", "原料情報を削除しました");
		redirectAttributes.addFlashAttribute("msgType", "delete");
		return "redirect:/raw/list";
	}

	//削除済み一覧表示用
	@GetMapping("/deleted")
	public String showDeletedList(Model model) {
		model.addAttribute("materialList", materialService.findDeletedMaterialsByType("RAW"));
		model.addAttribute("currentPage", "deleted");
		return "raw/deleted";
	}

	//復旧処理用
	@PostMapping("/deleted/{id}")
	public String restore(@PathVariable Long id,
			RedirectAttributes redirectAttributes) {
		materialService.restore(id);
		// 成功メッセージ
		redirectAttributes.addFlashAttribute("message", "原料情報を復旧しました");
		redirectAttributes.addFlashAttribute("msgType", "restore");
		return "redirect:/raw/deleted";
	}
}