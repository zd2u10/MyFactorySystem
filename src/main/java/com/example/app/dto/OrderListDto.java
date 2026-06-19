package com.example.app.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderListDto {
	private Long id;
	private LocalDate orderDate;
	private String customerName;
	private String status;
	private String itemSummary; // 「うどん x10, 玄米うどん x5」のような表示用まとめ文字列
}
