package com.example.app.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.example.app.domain.Material;
import com.example.app.domain.Recipe;
import com.example.app.dto.RecipeForm;
import com.example.app.mapper.InventoryStockMapper;
import com.example.app.service.ItemService;
import com.example.app.service.MaterialService;
import com.example.app.service.RecipeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

	private final RecipeService recipeService;
	private final MaterialService materialService;
	private final ItemService itemService;
	private final InventoryStockMapper inventoryStockMapper;

	@GetMapping("/list/{itemId}")
	public String list(@PathVariable Long itemId, Model model) {
		Item item = itemService.getItemById(itemId)
				.orElseThrow(() -> new RuntimeException("該当する商品が見つかりません"));
		model.addAttribute("itemName", item.getName());
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
		// ★★★ 追加: 最初から空の入力行を1つセットしておく ★★★
		data.getRecipeList().add(new RecipeForm());
		model.addAttribute("recipeRegisterData", data);
		buildRegisterModel(itemId, model);
		return "recipes/register";
	}

	@PostMapping("/register")
	public String registerRecipe(@Valid @ModelAttribute RecipeRegisterData data,
			BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			buildRegisterModel(data.getItemId(), model);
			return "recipes/register";
		}
		recipeService.registerRecipe(data);
		return "redirect:/recipes/list/" + data.getItemId();
	}

	private void buildRegisterModel(Long itemId, Model model) {
		Item item = itemService.getItemById(itemId)
				.orElseThrow(() -> new RuntimeException("該当する商品が見つかりません"));
		model.addAttribute("item", item);
		List<Material> rawList = materialService.getMaterialsByType("RAW");
		List<Material> additiveList = materialService.getMaterialsByType("ADDITIVE");
		model.addAttribute("rawList", rawList);
		model.addAttribute("additiveList", additiveList);
		model.addAttribute("originsMap", buildOriginsMap(rawList, additiveList));
	}

	private Map<Long, List<String>> buildOriginsMap(
			List<Material> rawList, List<Material> additiveList) {
		Map<Long, List<String>> map = rawList.stream().collect(
				Collectors.toMap(
						Material::getId,
						m -> inventoryStockMapper.findDistinctOriginsByMaterialId(m.getId())));
		additiveList.forEach(m -> map.put(m.getId(),
				inventoryStockMapper.findDistinctOriginsByMaterialId(m.getId())));
		return map;
	}
}
