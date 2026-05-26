package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.app.domain.Material;
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
		// 全原料リストを取得
		model.addAttribute("materialList", materialService.getAllMaterials());
		return "material/list";
	}

	// 登録画面を表示
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("material", new Material());
		return "material/register";
	}

	// 登録処理を実行
	@PostMapping("/register")
	public String register(@ModelAttribute Material material) {
		materialService.registerMaterial(material);
		return "redirect:/material/list";
	}
}
