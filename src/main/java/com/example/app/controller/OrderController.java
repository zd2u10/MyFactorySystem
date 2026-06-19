package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.dto.OrderForm;
import com.example.app.service.ItemService;
import com.example.app.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final ItemService itemService;

	// 受注一覧（未出荷=OPENのみ）
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("orderList", orderService.getOrderList("OPEN"));
		model.addAttribute("currentPage", "list");
		return "orders/list";
	}

	// 受注履歴（ログ：全件、出荷済やキャンセルも含む）
	@GetMapping("/history")
	public String history(Model model) {
		model.addAttribute("orderList", orderService.getOrderList(null));
		model.addAttribute("currentPage", "history");
		return "orders/history";
	}

	// 受注入力フォーム
	@GetMapping("/new")
	public String newForm(Model model) {
		model.addAttribute("items", itemService.getAllItems());
		model.addAttribute("orderForm", new OrderForm());
		return "orders/new";
	}

	// 受注登録
	@PostMapping
	public String create(@ModelAttribute OrderForm orderForm, RedirectAttributes redirectAttributes) {
		try {
			orderService.createOrder(orderForm);
			redirectAttributes.addFlashAttribute("successMessage", "受注を登録しました。");
		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/orders/new";
		}
		return "redirect:/orders/list";
	}

	// 受注詳細
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		model.addAttribute("order", orderService.getOrderById(id));
		model.addAttribute("orderItems", orderService.getOrderItems(id));
		return "orders/detail";
	}

	@PostMapping("/detail/{id}/status")
	public String updateStatus(@PathVariable("id") Long id,
			@RequestParam("status") String status,
			RedirectAttributes redirectAttributes) {
		try {
			// DBのステータスを更新する処理
			orderService.updateOrderStatus(id, status);

			String message = status.equals("SHIPPED") ? "出荷しました。" : "注文をキャンセルしました。";
			redirectAttributes.addFlashAttribute("successMessage", message);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "ステータスの更新に失敗しました。");
		}
		// 更新後は一覧に戻る(タスクが減ったことを確認させる)
		return "redirect:/orders/list";
	}
}
