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
	private Long stockId; // どの在庫ロットに対する操作か
	private String transactionType; // "IN(入庫)" or "OUT(消費)"
	private BigDecimal quantityChange; // 変化量(消費時はマイナス値)
	private LocalDate transactionDate; // 操作が行われた日
	private String note; // 備考（「〇〇製品製造のため」など）
	private LocalDateTime createdAt; // システム上の記録日時
}