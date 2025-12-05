package edu.kh.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	// 동기식 > 주로 forward or Redirect를 하게 됨 > forward하기 위해 반환형은 String
	@RequestMapping("/")
	public String mainPage() {
		// 접두사/접미사 제외 > classpath:/templates/	.html 제외
		return "common/main";
	}
}
