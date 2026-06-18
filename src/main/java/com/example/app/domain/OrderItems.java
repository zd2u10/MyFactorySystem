package com.example.app.domain;

import java.math.BigDecimal;

public class OrderItems {

	private Long id;
	private Long orderId;
	private Long itemId;
	private BigDecimal quantity; // 受注数
	private BigDecimal shippedQuantity; // 出荷済み数量(出荷処理されるまでは常に0)

}
