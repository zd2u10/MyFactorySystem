package com.example.app.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Item;
import com.example.app.domain.ItemStock;
import com.example.app.domain.ProductionOrder;
import com.example.app.service.ItemService;
import com.example.app.service.ItemStockService;
import com.example.app.service.ProductionOrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/inventory/item")
@RequiredArgsConstructor
public class ItemStockController {

	private final ItemStockService itemStockService;
	private final ProductionOrderService productionOrderService;
	private final ItemService itemService;

	// 製品在庫一覧
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("stockList", itemStockService.getAllItemStock());
		return "inventory/item/list";
	}

	// 製品在庫の編集画面
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long itemId, Model model) {
		// 1. 製品情報の取得 ItemServiceを使用
		Item item = itemService.getItemById(itemId)
				.orElseThrow(() -> new RuntimeException("製品が見つかりません。"));
		model.addAttribute("item", item);

		// 2. 製造中の製品(ProductionOrder)からのロット候補を生成
		List<ProductionOrder> manufacturingOrders = productionOrderService.findManufacturingOrdersByItemId(itemId);
		List<String> selectableLots = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		for (ProductionOrder order : manufacturingOrders) {
			if (order.getProductionDate() != null) {
				selectableLots.add(order.getProductionDate().format(formatter) + "-" + itemId);
			}
		}
		model.addAttribute("selectableLots", selectableLots);
		model.addAttribute("id", itemId);

		// 3. 在庫リストと有効在庫
		List<ItemStock> stockList = itemStockService.findStocksByItemId(itemId);
		model.addAttribute("stockList", stockList);

		BigDecimal totalAvailable = itemStockService.calculateTotalAvailable(itemId);
		model.addAttribute("totalAvailable", totalAvailable);

		return "inventory/item/edit";
	}

	// 製造完了品の追加(入庫処理)
	@PostMapping("/add")
	public String addStock(
			@RequestParam("itemId") Long itemId,
			@RequestParam("lotNumber") String lotNumber,
			@RequestParam("quantity") BigDecimal quantity,
			RedirectAttributes redirectAttributes) {

		try {
			// DBから背託されたロット番号に該当する製造指示データを1件取得する
			ProductionOrder order = productionOrderService.findByLotNumber(lotNumber);
			LocalDate productionDate;

			if (order != null && order.getProductionDate() != null) {
				productionDate = order.getProductionDate();
			} else {
				productionDate = LocalDate.now(); // 人が手動で集計して入力した場合の救済措置
			}

			// サービス層へ引き渡す
			itemStockService.addStock(itemId, lotNumber, quantity, productionDate);

			redirectAttributes.addFlashAttribute("successMessage", "在庫を追加しました。(ロット: " + lotNumber + ")");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "入庫処理に失敗しました:" + e.getMessage());
		}
		return "redirect:/inventory/item/edit/" + itemId;
	}

	// 出庫・破棄・調整処理(減少)
	@PostMapping("/reduce")
	public String reduceStock(
			@RequestParam("stockId") Long stockId,
			@RequestParam("itemId") Long itemId,
			@RequestParam("reduceQuantity") BigDecimal reduceQuantity,
			@RequestParam("transactionType") String transactionType,
			RedirectAttributes redirectAttributes) {

		try {
			itemStockService.reduceStock(stockId, reduceQuantity, transactionType);
			redirectAttributes.addFlashAttribute("successMessage", "在庫を減算しました。");
		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "処理に失敗しました。");

		}
		return "redirect:/inventory/item/edit/" + itemId;
	}
}
