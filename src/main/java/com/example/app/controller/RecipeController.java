package com.example.app.controller;

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

import com.example.app.data.RecipeRegisterData;
import com.example.app.domain.Item;
import com.example.app.domain.Recipe;
import com.example.app.service.ItemService;
import com.example.app.service.MaterialService;
import com.example.app.service.RecipeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/recipes") // パスを階層化
@RequiredArgsConstructor
public class RecipeController {

	private final RecipeService recipeService;
	private final MaterialService materialService;
	private final ItemService itemService;

	// レシピ表示
	@GetMapping("/list/{itemId}")
	public String list(@PathVariable Long itemId, Model model) {
		Item item = itemService.getItemById(itemId)
				.orElseThrow(() -> new RuntimeException("該当する商品が見つかりません"));

		model.addAttribute("itemName", item.getName());

		// 2.レシピと加水率
		List<Recipe> recipes = recipeService.findByItemId(itemId);
		model.addAttribute("recipes", recipes);
		model.addAttribute("itemId", itemId);
		model.addAttribute("waterRange", recipeService.calculateWaterRange(itemId, recipes));

		return "recipes/list";
	}

	@GetMapping("/register/{itemId}")
	public String showRegister(@PathVariable Long itemId, Model model) {
		RecipeRegisterData data = new RecipeRegisterData();
		data.setItemId(itemId);

		// ① Item情報を取得してモデルに渡す(加水率と商品名を表示・計算するため)
		Item item = itemService.getItemById(itemId)
				.orElseThrow(() -> new RuntimeException("該当する商品が見つかりません"));
		model.addAttribute("item", item);

		model.addAttribute("recipeRegisterData", data);
		model.addAttribute("rawList", materialService.getMaterialsByType("RAW"));
		model.addAttribute("additiveList", materialService.getMaterialsByType("ADDITIVE"));

		return "recipes/register";
	}

	@PostMapping("/register")
	public String registerRecipe(@Valid @ModelAttribute RecipeRegisterData data,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			// エラーがある場合はフォーム画面へ戻す処理
			// (必要に応じて　rawList, additiveList を再取得)
			return "recipes/register";
		}

		// 登録画面へ進む
		// Serviceを呼んでDBにまとめて保存
		recipeService.registerRecipe(data);

		// 保存が終わったら、その商品のレシピ画面へリダイレクト
		return "redirect:recipes/list/" + data.getItemId();
	}

}
