package com.example.demo.controller;

import com.example.demo.DemoProject1Application;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// instance : 개발자가 직접 new 연산자를 통해 만든 객체(관리하는 객체)
// bean : Spring에서 IOC를 이용해 Container가 생성하고, 생명 주기 제어를 담당하는 객체
// IOC(제어의 반전) : 객체의 생성 및 생명 주기의 권한이 개발자가 아닌, 프레임워크에게 있다

// @Controller : 요청, 응답을 제어하는 역할인 컨트롤러임을 명시 + Bean으로 등록하는 어노테이션
@Controller
// @RequestMapping("/test")
// @RequestMapping("요청주소") : 요청 주소를 처리할 클래스/메서드를 매핑하는 어노테이션
// 1) 클래스와 메서드에 함께 작성 > 공통 주소(ex_/test)를 매핑 (ex_/test/insert, /test/update, /test/select)
public class TestController {

    private final DemoProject1Application demoProject1Application;

    TestController(DemoProject1Application demoProject1Application) {
        this.demoProject1Application = demoProject1Application;
    }
	// Servlet : 클래스 단위로 하나의 요청만 처리 가능 (하나의 클래스가 "/test"하나만 매핑받아 doGet/doPost 처리를 할 수 있음)
	// Spring : 메서드 단위로 요청 처리 가능
	
//	// "/test/insert"로 들어온 요청을 methodA가 처리
//	@RequestMapping("/insert")
//	public void methodA() {
//		
//	}
//	
//	// "/test/update"로 들어온 요청을 methodB가 처리
//	@RequestMapping("/update")
//	public void methodB() {
//		
//	}
//	
//	// "/test/select"로 들어온 요청을 methodC가 처리
//	@RequestMapping("/select")
//	public void methodC() {
//		
//	}
	
	// 2) 메서드에 작성 : 요청 주소와 해당 메서드를 매핑 > GET/POST 가리지 않고 매핑 (속성을 통해서 지정 가능 or 다른 어노테이션 이용)
	// "/test" 요청 시 testMethod가 매핑하여 처리
//	@RequestMapping(value = "/test", method = RequestMethod.GET)
    @RequestMapping("/test")
	public String testMethod() {
		// 2025-11-24T14:32:31.748+09:00[0;39m [32m INFO[0;39m [35m21620[0;39m [2m--- [demoProject1] [p-nio-80-exec-1] [0;39m[36mo.s.web.servlet.DispatcherServlet       [0;39m [2m:[0;39m Completed initialization in 1 ms
		// /test 요청 받음
		System.out.println("/test 요청 받음");
		
		// Controller 메서드의 반환형은 String인 이유 > 메서드에서 반환하는 문자열이 forward할 html 파일의 경로가 되기 때문!
		// Thymeleaf : JSP 대신 사용하는 템플릿 엔진(html 형태)
		// 접두사 : classpath :/templates/
		// 접미사 : .html
		// *classpath: == src/main/resources >> src/main/resources/templates/(접두사)test.html(접미사)
		// 접두사 + 반환값 + 접미사 경로의 html로 포워드
		return "test";
	}
}