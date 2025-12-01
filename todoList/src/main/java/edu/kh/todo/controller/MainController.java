package edu.kh.todo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {
	// @Service을 통해 Bean으로 등록된 Service 객체를 > @Autowired을 사용해 Beans 영역 중 TodoService와 타입이 같거나 상속 관계인 Bean을 주입받기(DI)
	@Autowired
	private TodoService service;
	
	@RequestMapping("/")
	public String mainPage(Model model) {
		// service : edu.kh.todo.model.service.TodoServiceImpl@1435753b > 주소값이 확인되었으므로 객체화된 것을 알 수 있음!
		log.debug("service : " + service);
		
		// todoNo가 1인 todo의 제목을 조회하여 request scope에 추가하기
		String testTitle = service.testTitle();
		model.addAttribute("testTitle", testTitle);
		
		// TB_TODO 테이블에 저장된 전체 할 일의 목록 조회 + 완료된 할 일 개수
		// service 메서드 호출 후 결과 반환 받기
		Map<String, Object> map = service.selectAll();
		
		// map에 담긴 내용 가져오기
		List<Todo> todoList = (List<Todo>)map.get("todoList");
		int completeCount = (int)map.get("completeCount");
		
		// Model을 이용해서 조회 결과들을 request scope에 추가
		model.addAttribute("todoList", todoList);
		model.addAttribute("completeCount", completeCount);
				
		// src/main/resources/templates/	.html
		return "common/main";
	}
}
