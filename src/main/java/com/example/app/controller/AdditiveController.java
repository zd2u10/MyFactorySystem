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

import com.example.app.domain.Additive;
import com.example.app.dto.AdditiveForm;
import com.example.app.service.AdditiveService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/additive")
public class AdditiveController {
	private final AdditiveService additiveService;

	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("additiveList", additiveService.getAllAdditives());
		model.addAttribute("currentPage", "list");
		return "additive/list";
	}

	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("additiveForm", new AdditiveForm());
		model.addAttribute("currentPage", "register");
		return "additive/register";
	}

	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("additiveForm") AdditiveForm form,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			// エラー時はフォームを保持して画面へ戻す
			model.addAttribute("currentPage", "register");
			return "additive/register";
		}
		//変換処理をServiceに任せる
		additiveService.registerAdditiveFromForm(form);
		// 成功メッセージ
		redirectAttributes.addFlashAttribute("message", "添加物を登録しました");
		return "redirect:/material/list";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		Additive additive = additiveService.getAdditiveById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid additive Id:" + id));
		AdditiveForm form = new AdditiveForm();
		form.copyFrom(additive);

		model.addAttribute("additiveForm", form);
		model.addAttribute("id", id);
		model.addAttribute("currentPage", "list");
		return "additive/edit";
	}

	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id, @Valid @ModelAttribute("additiveForm") AdditiveForm form,
			BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors())
			return "additive/edit";

		// FormをEntityに変換
		Additive additive = form.toEntity();

		// Service経由で更新を実行
		additiveService.updateAdditive(additive);
		// 成功メッセージ
		redirectAttributes.addFlashAttribute("message", "更新しました");
		return "redirect:/additive/list";
	}

	//理論削除処理
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id,
			RedirectAttributes redirectAttributes) {
		additiveService.logicalDelete(id);
		redirectAttributes.addFlashAttribute("message", "削除しました");
		return "redirect:/additive/list";
	}

	//削除済み一覧表示用
	@GetMapping("/deleted")
	public String showDeletedList(Model model) {
		model.addAttribute("additiveList", additiveService.findDeletedAdditives());
		model.addAttribute("currentPage", "deleted");
		return "additive/deleted";
	}

	//復旧処理用
	@PostMapping("/deleted/{id}")
	public String restore(@PathVariable Long id,
			RedirectAttributes redirectAttributes) {
		additiveService.restore(id);
		// 成功メッセージ
		redirectAttributes.addFlashAttribute("message", "復旧しました");
		return "redirect:/additive/deleted";
	}

}
