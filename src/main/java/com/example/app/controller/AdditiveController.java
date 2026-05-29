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
		return "additive/list";
	}

	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("additiveForm", new AdditiveForm());
		return "additive/register";
	}

	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("additiveForm") AdditiveForm form,
			BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors())
			return "additive/register";
		additiveService.registerAdditive(form.toEntity());
		redirectAttributes.addFlashAttribute("message", "登録しました");
		return "redirect:/additive/list";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		Additive additive = additiveService.getAdditiveById(id).orElseThrow();
		AdditiveForm form = new AdditiveForm();
		form.copyFrom(additive);
		model.addAttribute("additiveForm", form);
		return "additive/edit";
	}

	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id, @Valid @ModelAttribute("additiveForm") AdditiveForm form,
			BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors())
			return "additive/edit";
		additiveService.updateAdditive(form.toEntity());
		redirectAttributes.addFlashAttribute("message", "更新しました");
		return "redirect:/additive/list";
	}
}