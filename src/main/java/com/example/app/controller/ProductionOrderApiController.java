package com.example.app.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.service.ProductionOrderService;

import lombok.RequiredArgsConstructor;

// ★ @RestController は「HTML」ではなく「データ」を返す専用のコントローラー
@RestController
@RequestMapping("/api/production")
@RequiredArgsConstructor
public class ProductionOrderApiController {

	private final ProductionOrderService productionOrderService;

	@PostMapping("/move")
	public ResponseEntity<?> moveSchedule(@RequestBody Map<String, Object> request) {
		System.out.println("■■■ 受け取ったリクエストデータ: " + request);
		try {
			Long orderId = Long.valueOf(request.get("orderId").toString());

			Object dateObj = request.get("scheduledDate");
			LocalDate scheduledDate = dateObj != null ? LocalDate.parse(dateObj.toString()) : null;

			productionOrderService.updateScheduledDateOnly(orderId, scheduledDate);
			return ResponseEntity.ok(Map.of("success", true));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
		}
	}

	@PostMapping("/confirm")
	public ResponseEntity<?> confirmSchedule(@RequestBody Map<String, Object> request) {
		try {
			Long orderId = Long.valueOf(request.get("orderId").toString());

			String newLotNumber = productionOrderService.confirmSchedule(orderId);
			return ResponseEntity.ok(Map.of("success", true, "lotNumber", newLotNumber));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
		}
	}
}