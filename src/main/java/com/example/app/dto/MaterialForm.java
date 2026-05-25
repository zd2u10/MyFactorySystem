package com.example.app.dto;

import java.math.BigDecimal;

import lombok.Data;

// 入力フォーム(画面)とのやり取りを担当するクラス

@Data
public class MaterialForm {
	private String name;
	private String unit; // 数量
	private BigDecimal netWeight; // 1つあたりの賞味量
}
