package com.example.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

/**
 * 入荷登録フォーム
 * inventory_stocks に書き込む情報をすべて受け取る
 */
@Data
public class ArrivalForm {

	@NotNull(message = "原料を選択してください")
	private Long materialId;

	@NotBlank(message = "ロット番号を入力してください")
	private String lotNumber;

	// 産地は未入力を許可（産地不問の材料に対応）
	private String origin;

	@NotNull(message = "1パッケージ当たりの内容量(g/ml)を入力して下さい")
	@DecimalMin(value = "0.001", message = "0より大きい値を入力して下さい")
	private BigDecimal netWeight;

	@NotNull(message = "入荷パッケージ数(袋/ケース)を入力して下さい")
	@DecimalMin(value = "1", message = "1以上の数量を入力してください")
	private BigDecimal packageCount;

	@NotNull(message = "賞味期限を入力してください")
	private LocalDate expiryDate;

	@NotNull(message = "入荷日を入力してください")
	private LocalDate arrivalDate;

	// 検品済みフラグ（チェックボックス、デフォルトfalse）
	private boolean inspected = false;

	// 備考（inventory_transactionsのnoteに使用）
	private String note;
}
