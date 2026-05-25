package com.example.app.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

// 在庫の出入りを管理するクラス
// 入庫や製造での消費といった在庫の変化を記録

@Data
public class InventoryTransaction {
	private Long id;
	private Long stockId; // どの原料ロットを消費したか
	private String transactionType; // "PRODUCTION(製造)","DISPOSAL(廃棄)","IN(入庫)"など
	private BigDecimal quantityChange; // 変化量(消費時はマイナス値)
	private String productCode; // 製品コード
	private String productNumber; // 製造ロットナンバー
	private String note; // 備考（「〇〇製品製造のため」など）
	private LocalDate transactionDate; // 操作が行われた日
	private LocalDateTime createdAt; // システム上の記録日時
}