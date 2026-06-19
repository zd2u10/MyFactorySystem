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

		BigDecimal scheduled = productionOrderMapper.sumManufacturingQuantityByItemId(itemId);
		if (scheduled == null) {
			scheduled = BigDecimal.ZERO;
		}

		BigDecimal shortage = minStock.subtract(available).subtract(scheduled);

		if (shortage.compareTo(BigDecimal.ZERO) <= 0) {
			return; // 在庫は十分、または既存の製造予定で不足分をまかなえている
		}

		int times = shortage.divide(batchSize, 0, RoundingMode.CEILING).intValue();

		for (int i = 0; i < times; i++) {
			ProductionOrder order = new ProductionOrder();
			order.setItemId(itemId);
			order.setQuantity(batchSize);
			order.setStatus("MANUFACTURING");
			order.setProductionDate(LocalDate.now());
			order.setTriggerSource("AUTO_ORDER");
			productionOrderMapper.insertProductionOrder(order);
		}
	}
}
