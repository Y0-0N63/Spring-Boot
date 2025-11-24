package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 요청/응답 제어 역할 명시 + Bean 등록
@Controller
public class ExampleController {
	
	// @RequestMapping("주소")
	// @GetMapping("주소") : GET(조회) 방식 요청 매핑
	// @PostMapping("주소") : POST(삽입) 방식 요청 매핑
	// @PutMapping("주소") : PUT(수정) 방식 요청 매핑 (form, a 태그 요청 불가 / JS 비동기 요청 시 사용)
	// @DeleteMapping("주소") : DELETE(삭제) 방식 요청 매핑 (form, a 태그 요청 불가 / JS 비동기 요청 시 사용)
	
	// 1) Spring Boot에서는 요청 주소 앞에 '/'가 없어도 요청 처리 가능 (= Servlet/JSP의 @WebServlet("/주소")와 달리 '/' 생략 가능)
	// 2) AWS와 같은 호스팅 서비스를 이용해 프로젝트를 배포할 시 > 리눅스 OS를 이용한다면 build 과정에서 경로 상 오류 발생
	@GetMapping("example")
	public String exampleMethod() {
		// forward하려는 html 파일 경로를 return 구문에 작성
		// 단, ViewResolver가 제공하는 접두사, 접미사는 제외하고 작성
		
		// 접두사 : classpath:/templates/
		// 접미사 : .html
		// src/main/resources/templates/example.html
		return "example";
	}
}