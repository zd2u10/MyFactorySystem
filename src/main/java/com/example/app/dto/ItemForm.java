package com.example.app.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import com.example.app.domain.Item;

import lombok.Data;

@Data
public class ItemForm {

	private Long id;

	@NotBlank(message = "製品名は必須です")
	private String name;

	@NotBlank(message = "販売単位は必須です")
	private String salesUnit;

	@NotNull(message = "バッチ製造数は必須です")
	@Positive(message = "0より大きい値を入力してください")
	private BigDecimal batchSize;

	@NotNull(message = "標準原価は必須です")
	@PositiveOrZero(message = "0以上の値を入力してください")
	private BigDecimal standardCost;

	@NotNull(message = "販売価格は必須です")
	@PositiveOrZero(message = "0以上の値を入力してください")
	private BigDecimal salesPrice;

	private boolean isActive = true;

	private BigDecimal minHydrationRate;

	private BigDecimal maxHydrationRate;

	// Entityへ変換するメソッド
	public Item toEntity() {
		Item item = new Item();
		item.setId(this.id);
		item.setName(this.name);
		item.setSalesUnit(this.salesUnit);
		item.setBatchSize(this.batchSize);
		item.setStandardCost(this.standardCost);
		item.setSalesPrice(this.salesPrice);
		item.setActive(this.isActive);
		item.setMinHydrationRate(this.minHydrationRate);
		item.setMaxHydrationRate(this.maxHydrationRate);
		return item;
	}

	// Entityから値をコピーするメソッド
	public void copyFrom(Item item) {
		this.id = item.getId();
		this.name = item.getName();
		this.salesUnit = item.getSalesUnit();
		this.batchSize = item.getBatchSize();
		this.standardCost = item.getStandardCost();
		this.salesPrice = item.getSalesPrice();
		this.isActive = item.isActive();
		this.minHydrationRate = item.getMinHydrationRate();
		this.maxHydrationRate = item.getMaxHydrationRate();
	}

}