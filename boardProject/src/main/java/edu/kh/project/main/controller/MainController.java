package edu.kh.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

	// 동기식 > 주로 forward or Redirect를 하게 됨 > forward하기 위해 반환형은 String
	@RequestMapping("/")
	public String mainPage() {
		// 접두사/접미사 제외 > classpath:/templates/	.html 제외
		return "common/main";
	}
	
	// loginFilter에서 로그인을 하지 않았을 때, 리다이렉트 요청
	@GetMapping("loginError")
	public String LoginError(RedirectAttributes ra) {
		ra.addFlashAttribute("message", "로그인 후 이용해주세요.");
		return "redirect:/";
	}
}
