//package com.example.app.controller;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.example.app.domain.ProductionOrder;
//import com.example.app.service.ProductionOrderService;
//
//import lombok.RequiredArgsConstructor;
//
//@Controller
//@RequestMapping("/production")
//@RequiredArgsConstructor
//public class ProductionOrderController2 {
//
//	private final ProductionOrderService productionOrderService;
//
//	@GetMapping("/board")
//	public String showBoard(Model model) {
//		// DRAFT と PLANNING の予定をすべて取得
//		List<ProductionOrder> orders = productionOrderService.findSchedulingOrders();
//
//		// カレンダー表示用に「今日から5日間」の日付リストを作成
//		List<LocalDate> nextDays = new ArrayList<>();
//		LocalDate today = LocalDate.now();
//		for (int i = 0; i < 5; i++) {
//			nextDays.add(today.plusDays(i));
//		}
//
//		model.addAttribute("orders", orders);
//		model.addAttribute("nextDays", nextDays);
//
//		return "production/board"; // board.html を表示
//	}
//}