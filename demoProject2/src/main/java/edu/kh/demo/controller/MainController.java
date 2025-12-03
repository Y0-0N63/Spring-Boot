package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// @Controller 어노테이션 : 요청과 응답의 역할을 명시하는 동시에 (해당 역할을 하는 객체라는 의미로) Bean으로 등록
@Controller
public class MainController {
	// '/' 주소로 요청이 왔을 시 > MainController에서 main.html 파일로 forward 시켜주기
	@RequestMapping("/")
	public String mainPage() {
		// forward : 요청 위임
		// thymleaf : String Boot에서 사용하는 템플릿 엔진 (html 파일 사용)
		// 접두사 : classpath:/tempaltes/ 		접미사 : .html
		// src/main/resources/templates/	common/main		.html
		return "common/main";
	}
}