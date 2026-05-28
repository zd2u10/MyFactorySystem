//package com.example.app.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.example.app.service.ItemService;
//
//import lombok.RequiredArgsConstructor;
//
//@Controller
//@RequestMapping("/items/{itemId}/recipes") // パスを階層化
//@RequiredArgsConstructor
//public class RecipeController {
//
//	private final RecipeService recipeService;
//	private final ItemService itemService;
//
//	@GetMapping("/list")
//	public String list(@PathVariable Long itemId, Model model) {
//		model.addAttribute("item", itemService.getItemById(itemId).get());
//		model.addAttribute("recipes", recipeService.findByItemId(itemId));
//		return "items/recipes/list"; // フォルダ階層に合わせる
//	}
//}
