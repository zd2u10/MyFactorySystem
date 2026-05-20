package com.example.app.controller;

import org.springframework.stereotype.Controller;

import com.example.app.service.MaterialService;

import lombok.RequiredArgsConstructor;

// ビューへの操作を担うクラス
@Controller
@RequiredArgsConstructor
public class MaterialController {
	private final MaterialService materialService;

}
