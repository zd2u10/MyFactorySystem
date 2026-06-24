package com.example.app.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ProductionOrder {
	private Long id;
	private Long itemId;
	private String itemName;
	private String lotNumber;
	private BigDecimal quantity;
	private BigDecimal waterAmount;

	// カレンダー上の「製造予定日」（パズルで決める日付）
	private LocalDate scheduledDate;

	//「 実際に製造された日」 MANUFACTURINGでセットする
	private LocalDate productionDate;

	// DRAFT, PLANNING, MANUFACTURING, COMPLETEDなど
	private String status;

	// 予定が自動生成か手動追加かを判別させる
	private String triggerSource; // "AUTO_ORDER" または "MANUAL"

	// 一度でも「期限切れ」状態になったことがあるかの履歴フラグ
	// scheduledDateを更新（再調整）してもtrueのまま保持される
	private Boolean wasOverdue;

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}
