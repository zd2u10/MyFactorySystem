package com.example.app.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Item;
import com.example.app.domain.ProductionOrder;
import com.example.app.mapper.OrderMapper;
import com.example.app.mapper.ProductionOrderMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductionOrderServiceImpl implements ProductionOrderService {

	private final ProductionOrderMapper productionOrderMapper;
	private final OrderMapper orderMapper; // 未出荷受注数の集計に使用（OrderServiceには依存しない＝循環参照を避ける）
	private final ItemService itemService;
	private final ItemStockService itemStockService;

	@Override
	public List<ProductionOrder> findManufacturingOrdersByItemId(Long itemId) {
		return productionOrderMapper.findManufacturingOrdersByItemId(itemId);
	}

	@Override
	public ProductionOrder findByLotNumber(String lotNumber) {
		return productionOrderMapper.findByLotNumber(lotNumber);
	}

	@Override
	public List<ProductionOrder> findSchedulingOrders() {
		// ボード用の予定一覧(DRAFT, PLANNING)を取得して返す
		return productionOrderMapper.findSchedulingOrders();
	}

	// 有効在庫 = 実在庫(出荷引当後) − 未出荷受注数
	// 不足数 = 適正在庫(min_stock) − 有効在庫 − 既存のMANUFACTURING予定合計
	// 製造回数 = 不足数 ÷ batch_size（繰り上げ）
	@Override
	@Transactional
	public void checkAndGenerateForItem(Long itemId) {
		Item item = itemService.getItemById(itemId)
				.orElseThrow(() -> new RuntimeException("製品が見つかりません。(id=" + itemId + ")"));

		BigDecimal minStock = item.getMinStock();
		BigDecimal batchSize = item.getBatchSize();

		// 適正在庫・1回あたりの製造数が設定されていない品目は自動生成の対象外とする
		if (minStock == null || batchSize == null || batchSize.compareTo(BigDecimal.ZERO) <= 0) {
			return;
		}

		BigDecimal physicalAvailable = itemStockService.calculateTotalAvailable(itemId);

		BigDecimal openOrderQty = orderMapper.sumOpenQuantityByItemId(itemId);
		if (openOrderQty == null) {
			openOrderQty = BigDecimal.ZERO;
		}

		BigDecimal available = physicalAvailable.subtract(openOrderQty);

		// 「DRAFT」や「PLANNING」の予定数も足し合わせる
		BigDecimal scheduled = productionOrderMapper.sumManufacturingQuantityByItemId(itemId);
		if (scheduled == null) {
			scheduled = BigDecimal.ZERO;
		}

		// 不足数 = 適正在庫 - 有効在庫 - すでに予定されている全数(DRAFTを含む)
		BigDecimal shortage = minStock.subtract(available).subtract(scheduled);

		if (shortage.compareTo(BigDecimal.ZERO) <= 0) {
			return; // 在庫は十分、または既存の製造予定で不足分をまかなえている
		}

		int times = shortage.divide(batchSize, 0, RoundingMode.CEILING).intValue();

		for (int i = 0; i < times; i++) {
			ProductionOrder order = new ProductionOrder();
			order.setItemId(itemId);
			order.setQuantity(batchSize);
			// 1:DRAFT状態にする
			order.setStatus("DRAFT");
			// 2:システムが自動振り出しした証
			order.setTriggerSource("AUTO_ORDER");

			productionOrderMapper.insertProductionOrder(order);
		}
	}

	@Override
	@Transactional
	public void updateScheduledDateOnly(Long orderId, LocalDate scheduledDate) {
		ProductionOrder order = productionOrderMapper.findById(orderId);

		// DRAFT（未確定）状態の時だけ、日付の変更（移動）を許す
		if (order != null && "DRAFT".equals(order.getStatus())) {
			// 上書き前の予定日が「今日より前」だった場合＝この時点で期限切れだった、という事実を残す。
			// このフラグは一度trueになったら、再調整後もfalseに戻らない（履歴として保持）。
			boolean wasOverdue = order.getScheduledDate() != null
					&& order.getScheduledDate().isBefore(LocalDate.now());

			productionOrderMapper.updateScheduledDate(orderId, scheduledDate, wasOverdue);
		} else {
			throw new RuntimeException("この予定はすでに確定されているか、存在しません。");
		}
	}

	@Override
	@Transactional
	public String confirmSchedule(Long orderId) {
		ProductionOrder order = productionOrderMapper.findById(orderId);

		if (order == null || !"DRAFT".equals(order.getStatus()) || order.getScheduledDate() == null) {
			throw new RuntimeException("無効なオーダー、または予定日が未設定です。");
		}

		LocalDate scheduledDate = order.getScheduledDate();

		// 日付文字列（例: 2026年6月25日 -> "20260625"）
		String dateStr = scheduledDate.toString().replace("-", "");

		// 新しいロット番号のルール ＝ "日付" ＋ "-" ＋ "商品ID(3桁ゼロ埋め)"
		// (例: 商品IDが 3 なら "20260625-003" となる)
		String newLotNumber = String.format("%s-%03d", dateStr, order.getItemId());

		// DBを更新
		productionOrderMapper.updateScheduleAndLot(orderId, scheduledDate, "PLANNING", newLotNumber);

		return newLotNumber;
	}
}
