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

@Controller
@RequiredArgsConstructor
@RequestMapping("/additive")
public class AdditiveController {
	private final MaterialService materialService;

	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("additiveList", materialService.getMaterialsByType("ADDITIVE"));
		model.addAttribute("currentPage", "list");
		return "additive/list";
	}

	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		MaterialForm form = new MaterialForm();
		form.setMaterialType("ADDITIVE");
		model.addAttribute("additiveForm", form);
		model.addAttribute("currentPage", "register");
		return "additive/register";
	}

	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("additiveForm") MaterialForm form,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			// エラー時はフォームを保持して画面へ戻す
			model.addAttribute("currentPage", "register");
			return "additive/register";
		}
		//変換処理をServiceに任せる
		materialService.registerMaterialFromForm(form);
		// 成功メッセージ
		redirectAttributes.addFlashAttribute("message", "添加物を登録しました");
		redirectAttributes.addFlashAttribute("msgType", "register");
		return "redirect:/additive/list";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		Material material = materialService.getMaterialById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid additive Id:" + id));

		MaterialForm form = new MaterialForm();
		form.copyFrom(material);

		model.addAttribute("additiveForm", form);
		model.addAttribute("id", id);
		model.addAttribute("currentPage", "list");
		return "additive/edit";
	}

	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id,
			@Valid @ModelAttribute("additiveForm") MaterialForm form,
			BindingResult result, RedirectAttributes redirectAttributes) {
		System.out.println("★受信したID: " + id);
		System.out.println("★デバッグ: materialType = " + form.getMaterialType());
		if (result.hasErrors()) {
			return "additive/edit";
		}

		// FormをEntityに変換
		Material material = form.toEntity();
		// Service経由で更新を実行
		materialService.updateMaterial(material);

		// 成功メッセージ
		redirectAttributes.addFlashAttribute("message", "添加物情報を更新しました");
		redirectAttributes.addFlashAttribute("msgType", "update");
		return "redirect:/additive/list";
	}

	//理論削除処理
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id,
			RedirectAttributes redirectAttributes) {
		materialService.logicalDelete(id);
		redirectAttributes.addFlashAttribute("message", "添加物情報を削除しました");
		redirectAttributes.addFlashAttribute("msgType", "delete");
		return "redirect:/additive/list";
	}

	//削除済み一覧表示用
	@GetMapping("/deleted")
	public String showDeletedList(Model model) {
		model.addAttribute("additiveList", materialService.findDeletedMaterialsByType("ADDITIVE"));
		model.addAttribute("currentPage", "deleted");
		return "additive/deleted";
	}

	//復旧処理用
	@PostMapping("/deleted/{id}")
	public String restore(@PathVariable Long id,
			RedirectAttributes redirectAttributes) {
		materialService.restore(id);
		// 成功メッセージ
		redirectAttributes.addFlashAttribute("message", "添加物情報を復旧しました");
		redirectAttributes.addFlashAttribute("msgType", "restore");
		return "redirect:/additive/deleted";
	}

}
