package edu.kh.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.demo.model.dto.Student;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("example")
@Slf4j
public class ExampleController {
	@GetMapping("ex1")
	public String ex1(HttpServletRequest req, Model model) {
		/* Servlet 내장 객체 범위 : page < request < session < application
		 * *page scope는 사실상 Servlet에 존재하지 않는 셈 (JSP에만 존재)
		 * Model (org.springframework.ui.Model) : Spring에서 데이터 전달 역할을 하는 객체로, request가 기본 scope
		 * SessionAttribute와 함께 사용 시 session scope 변환
		 
		 *  [기본 사용법]
		 *  model.addAttribute("key", value);
		 *  */
		
		// 방법 1 > request scope에 담김
		req.setAttribute("test1", "HttpServletRequest로 전달한 값");
		// 방법 2 > request scope에 담김
		model.addAttribute("test2", "Model로 전달한 값");

		// 단일 값(숫자, 문자열)
		model.addAttribute("productName", "마이크");
		model.addAttribute("price", 20000	);
		
		// 복수 값(배열, List) Model을 이용해 html로 전달
		List<String> fruitList = new ArrayList<>();
		fruitList.add("사과");
		fruitList.add("딸기");
		fruitList.add("바나나");
		
		model.addAttribute("fruitList", fruitList);
		
		// DTO 객체를 Model 객체를 이용해 html로 전달하기
		Student std = new Student();
		std.setStudentNo("12345");
		std.setName("홍길동");
		std.setAge(22);
		
		model.addAttribute("std", std);
		
		// List<Student> 객체 Model을 이용해 html로 전달
		List<Student> stdList = new ArrayList<>();
		stdList.add(new Student("11111", "김일번", 20));
		stdList.add(new Student("22222", "최이번", 23));
		stdList.add(new Student("33333", "홍삼번", 22));
		
		model.addAttribute("stdList", stdList);
		
		// src/main/resources/templates/example/ex1.html로 forward하기
		return "example/ex1";
	}
	
	@PostMapping("ex2")
	public String ex2(Model model) {

		// *Model model은 객체 자체, @ModelAttribute : DTO와 함께 이용하는 어노테이션
		model.addAttribute("str", "<h1> 테스트 중 </h1>"); // request scope
		
		// src/main/resources/templates/example/ex2.html
		return "example/ex2";
	}
	
	@GetMapping("ex3")
	public String ex3(Model model) {
		model.addAttribute("key", "제목");
		model.addAttribute("query", "검색어");
		model.addAttribute("boardNo", 10);
		
		return "example/ex3";
	}
	
	@GetMapping("ex3/{path}")
	public String pathVariableTest(@PathVariable("path") int path) {
		// controller에서 해야하는 로직이 동일한 경우 (example/ex3/1, example/3/2...) > 주소 중 {path} 부분의 값을 가져와 매개변수로 저장
		// 해당 매개변수의 값을 controller단의 메서드에서 사용할 수 있도록 해줌 (이 값을 Service -> DAO -> DB)
		// Request Scope에 값이 자동으로 세팅됨 (변수명(path)=값의 Key=value 형식)
		
		return "example/testResult";
	}
	
	@GetMapping("ex4")
	public String ex4(Model model) {
		Student std = new Student("6789", "잠만보", 30);
		model.addAttribute("std", std);
		model.addAttribute("num", 1000);
		
		return "example/ex4";
	}
}
