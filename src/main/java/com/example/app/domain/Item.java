package com.example.app.domain;

import java.math.BigDecimal;

// 製品の情報を持つマスタクラス

import lombok.Data;

@Data
public class Item {
	private Long id; // ID(PK)
	private String name; // 製品名
	private String salesUnit; // 販売・出荷の単位
	private BigDecimal batchSize; // 1製造あたりの製造数
	private BigDecimal standardCost; // 標準原価
	private BigDecimal salesPrice; // 販売価格
	private boolean isActive; // 有効フラグ
}
