package edu.kh.todo.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.todo.model.dao.TodoDAO;
import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.mapper.TodoMapper;

// @Transactional : 트랜잭션 처리 수행을 지시하는 어노테이션으로, 정상 코드 수행 시 Commit
// or not > Rollback (기본값 : Service 내부 코드 수행 중 RuntimeException 발생 시 Rollback
// rollbackFor : 어떤 예외가 발생했을 시 rollback할지 지정하는 속성, Exception.class 작성 시 모든 Exception 발생 시 rollback

@Transactional(rollbackFor = Exception.class)
@Service // 비즈니스 로직(데이터 가공, 트랜잭션 처리) 역할 명시 + Bean으로 등록
public class TodoServiceImpl implements TodoService {
	@Autowired //TodoDAO와 같은 타입이거나 상속 관계인 Bean을 의존성 주입받음 (DI)
	private TodoDAO dao;
	
	@Autowired
	private TodoMapper mapper;
	
	@Override
	public String testTitle() {
		return dao.testTitle();
	}

	@Override
	public Map<String, Object> selectAll() {
		// 1. 할 일 목록 조회
		List<Todo> todoList = mapper.selectAll();
		
		// 2. 완료된 할 일 개수 조회
		int completeCount = mapper.getCompleteCount();
		
		// 3. 위 두 개의 결과값을 Map으로 묶어서 반환
		Map<String, Object> map = new HashMap<>();
		map.put("todoList", todoList);
		map.put("completeCount", completeCount);
		
		return map;
	}

	@Override
	public int addTodo(String todoTitle, String todoContent) {
		// 마이바티스에서 SQL에 전달할 수 있는 파라미터 개수는 오직 1개
		// > TodoMapper에 생성될 추상 메서드의 매개변수도 1개
		// > todoTitle, todoContent 2개의 파라미터를 전달하기 위해 Todo DTO로 묶어서 전달하면 됨!
		Todo todo = new Todo();
		todo.setTodoTitle(todoTitle);
		todo.setTodoContent(todoContent);
		
		return mapper.addTodo(todo);
	}

	@Override
	public Todo todoDetail(int todoNo) {
		return mapper.todoDetail(todoNo);
	}

	@Override
	public int changeComplete(Todo todo) {
		return mapper.changeComplete(todo);
	}

	@Override
	public int todoUpdate(Todo todo) {
		return mapper.todoUpdate(todo);
	}
	
	@Override
	public int todoDelete(int todoNo) {
		return mapper.todoDelete(todoNo);
	}
}