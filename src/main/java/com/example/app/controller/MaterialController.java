package com.example.app.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Material;
import com.example.app.dto.MaterialForm;
import com.example.app.service.MaterialService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/masters/materials")
@RequiredArgsConstructor
public class MaterialController {

	private final MaterialService materialService;

	// 一覧表示（RAWとADDITIVEの両方を持ち、フロントでJS切り替えを行う）
	@GetMapping("/list")
	public String list(Model model) {
		List<Material> allMaterials = new ArrayList<>();

		// それぞれ取得して、allMaterialsにまとめて追加
		allMaterials.addAll(materialService.getMaterialsByType("RAW"));
		allMaterials.addAll(materialService.getMaterialsByType("ADDITIVE"));

		model.addAttribute("rawList", materialService.getMaterialsByType("RAW"));
		model.addAttribute("additiveList", materialService.getMaterialsByType("ADDITIVE"));
		model.addAttribute("currentPage", "list");
		return "masters/materials/list";
	}

	// 登録画面（タイプをhiddenで受け取る設計を想定）
	@GetMapping("/register")
	public String showRegisterForm(@RequestParam(required = false, defaultValue = "RAW") String type, Model model) {
		MaterialForm form = new MaterialForm();
		form.setMaterialType(type);
		model.addAttribute("materialForm", form);
		model.addAttribute("currentPage", "register");
		return "masters/materials/register";
	}

	@PostMapping("/register")
	public String register(@Valid @ModelAttribute MaterialForm form, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors())
			return "materials/register";
		materialService.registerMaterial(form.toEntity());
		redirectAttributes.addFlashAttribute("message", "登録しました");
		return "redirect:/masters/materials/list";
	}

	// 編集画面
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		// サービス層で「原料か添加物か」を判定して取得する設計にしておく
		Material material = materialService.getMaterialById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid ID:" + id));

		MaterialForm form = new MaterialForm();
		form.copyFrom(material);

		model.addAttribute("materialForm", form);

		String typeLabel = "RAW".equals(material.getMaterialType()) ? "RAW" : "ADDITIVE";
		model.addAttribute("title", typeLabel + "情報の編集");

		return "masters/materials/edit";
	}

	// 更新処理 (RawもAdditiveも共通)
	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id, @ModelAttribute("materialForm") MaterialForm form) {
		materialService.updateMaterial(form.toEntity());
		return "redirect:/masters/materials/list"; // 完了後は一覧へ
	}

	// 削除処理
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes ra) {
		materialService.logicalDelete(id);
		ra.addFlashAttribute("message", "削除しました");
		return "redirect:/masters/materials/list";
	}

	//削除済み一覧表示
	@GetMapping("/deleted")
	public String showDeletedList(@RequestParam(required = false, defaultValue = "RAW") String type, Model model) {
		// 1.原料と添加物の削除済みリストをそれぞれ個別に取得
		List<Material> rawList = materialService.findDeletedMaterialsByType("RAW");
		List<Material> additiveList = materialService.findDeletedMaterialsByType("ADDITIVE");

		// 2.HTML側へ(rawList, additiveList)として渡す
		model.addAttribute("rawList", rawList);
		model.addAttribute("additiveList", additiveList);

		// 3.現在のページ情報のセット
		model.addAttribute("currentPage", "deleted");

		return "materials/deleted";
	}

	// 復旧処理
	@PostMapping("/restore/{id}")
	public String restore(@PathVariable Long id, @RequestParam String type, RedirectAttributes ra) {
		materialService.restore(id);
		ra.addFlashAttribute("message", "復旧しました");
		// 復旧後、該当タイプの削除済み一覧へ戻る
		return "redirect:/materials/deleted?type=" + type;
	}

}