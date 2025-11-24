package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("param")
// @Slf4j : log를 이용한 메시지를 콘솔창에 출력할 때 사용(lombok에서 제공)
@Slf4j
public class ParameterController {
	
	// /param/main GET 방식 요청 매핑
	@GetMapping("main")
	public String paramMain() {
		
		// src/main/resources/templates/param/param-main.html
		return "param/param-main";
	}
	
	/* HttpServletRequest : 요청 클라이언트의 정보, 제출된 파라미터 등을 저장한 객체로 클라이언트 요청 시 생성
	 * Spring의 Controller단 메서드 작성 시 매개변수에 원하는 객체를 작성하면 > 존재하는 객체를 바인딩 or 없으면 생성
	 * > ArgumentResolver (전달 인자 해결사) */
	// /param/test1 POST 방식 요청 매핑
	@PostMapping("test1")
	public String paramTest1(HttpServletRequest req) {
		String inputName = req.getParameter("inputName");
		String inputAddress = req.getParameter("inputAddress");
		int inputAge = Integer.parseInt(req.getParameter("inputAge"));
		
		// debug 레벨로 로그 출력
//		[2m2025-11-24T16:17:5...[0;39m [2m:[0;39m inputName : 홍길동
//		[2m2025-11-24T16:17:5...[0;39m[36mc.e.demo.controller.ParameterController [0;39m [2m:[0;39m inputAddress : 서울
//		[2m2025-11-24T16:17:5...[0;39m[36mc.e.demo.controller.ParameterController [0;39m [2m:[0;39m inputAge : 20
		log.debug("inputName : " + inputName);
		log.debug("inputAddress : " + inputAddress);
		log.debug("inputAge : " + inputAge);
		
		/* Spring에서 Redirect(재요청)하는 방법
		 * - Controller 메서드 반환값에 "redirect:재요청주소"; 작성
		 * * redirect는 get 방식 */
		return "redirect:/param/main";
	}
}