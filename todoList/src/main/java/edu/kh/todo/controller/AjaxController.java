package edu.kh.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;

/* @ResponseBody : 컨트롤러 메서드의 반환값을 Http 응답 본문에 직접 바인딩하는 역할임을 명시
* -> 컨트롤러 메서드의 반환값을 비동기 요청했던 HTML/JS 파일 부분에 값을 그대로 돌려 보낼 것임을 명시
* -> 해당 어노테이션이 붙은 컨트롤러의 메서드는 return 에 작성된 값이 forward/redirect 로 인식되지 않음!
*
* @RequestBody : 비동기 요청시 전달되는 데이터 중 body 부분에 포함된 요청 데이터를 알맞은 Java 객체 타입으로 바인딩하는 어노테이션
* - 기본적으로 JSON 형식을 기대함.
*
* [HttpMessageConvertor]
* Spring에서 비동기 통신 시
* - 전달받은 데이터의 자료형
* - 응답하는 데이터의 자료형
* 위 두가지를 알맞은 형태로 가공(변환)해주는 객체
*
*    Java                       JS
* 문자열,숫자 <--------------> TEXT
*    Map      <->   JSON   <-> JS Object
*    DTO      <->   JSON   <-> JS Object
*   
* (참고)
* Spring에서 HttpMessageConverter 작동하기 위해서는 jackson-data-bind 라이브러리가 필요한데 Spring boot에는 모듈에 내장되어 있음
* (Project and External Dependencies에서 확인 가능) */


@Controller // 요청과 응답을 제어하는 역할을 명시하는 동시에 Bean으로 등록 (IOC)
@RequestMapping("ajax") // 요청 주소 시작이 "/ajax"인 요청을 매핑
public class AjaxController {
	
	// @Service를 통해 TodoService 인터페이스가 Bean으로 등록되어 있음 > 등록된 Bean 중 같은 타입 또는 상속 관계인 Bean 찾아  
	@Autowired
	private TodoService service;
	
	@GetMapping("main")
	public String ajaxMain() {
		return "ajax/main";
	}
	
	// 전체 Todo 개수를 비동기 방식으로 조회하기
	// forward/redirect를 하고자 하는 것이 아닌, '전체 Todo 개수'라는 데이터를 비동기 요청으로 보낸 클라이언트(브라우저)로 반환되는 것을 하고자 함
	// > 반환되어야 하는 결과값의 자료형을 반환형에 써야 함 (= String으로 고정되어 있지 않음)
	@ResponseBody // 반환값을 HTTP 응답 본문으로 직접 전송 (값을 그대로 돌려보냄) > return에 forward/redirect 값을 작성하지 않음
	@GetMapping("totalCount") // /ajax/totalCount로 온 Get 방식 요청을 매핑
	public int getTotalCount() {
		
		// 전체 할 일 개수를 조회하는 서비스 호출 > 결과 반환받기
		int totalCount = service.getTotalCount();
		
		// 결과 리턴(비동기 요청을 보낸 JS 쪽으로)
		return totalCount;
	}

	@ResponseBody
	@GetMapping("completeCount")
	public int getCompleteCount() {
		return service.getCompleteCount();
	}
	
	@ResponseBody
	@PostMapping("add")
						// 요청 body(본문)에 담긴 값을 Todo라는 DTO에 저장해 반환
	public int addTodo(@RequestBody Todo todo) {
		// 할 일 추가 서비스 호출 후 결과값 리턴
		return service.addTodo(todo.getTodoTitle(), todo.getTodoContent());
	}
	
	// 전체 할 일 목록 조회
	@ResponseBody
	@GetMapping("selectList")
	public List<Todo> selectList() {
		// 반환되는 값인 List = Java 전용 타입 > JS 인식 불가능 > JSON으로 변환해야
		// ㄴ> HttpMessageConverter가 자동으로 JSON 형태로 변환하여 반환해줌!
		return service.selectList();
	}
	
	// 할 일 상세 조회
	// @ResponseBody : Controller 메서드의 return 값이 view 이름이 아니라 HTTP 응답 본문에 직접 작성되어 클라이언트(브라우저)에게 전송되어야 함을 알려줌 
	@ResponseBody // 비동기 요청을 보낸 곳으로 응답값을 보내줌
	@GetMapping("detail")
	public Todo selectTodo(@RequestParam("todoNo") int todoNo) {
		// HttpMessageConverter : 자바 -> JSON : 형태 가공(변환)
		return service.todoDetail(todoNo);
	}
	
	// 할 일 삭제 요청(DELETE)
	@ResponseBody
	@DeleteMapping("delete") // /ajax/delete
	public int todoDelete(@RequestBody int todoNo) {
		return service.todoDelete(todoNo);
	}
	
	// 완료 여부 변경(PUT)
	@ResponseBody
	@PutMapping("changeComplete") // /ajax/changeComplete
	public int changeComplete(@RequestBody Todo todo) {
		return service.changeComplete(todo);
	}
	
	// 할 일 수정(PUT)
	@ResponseBody
	@PutMapping("update") // /ajax/update
	public int todoUpdate(@RequestBody Todo todo) {
		return service.todoUpdate(todo);
		
	}
}