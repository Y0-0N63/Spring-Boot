package edu.kh.todo.model.service;

import java.util.Map;

import edu.kh.todo.model.dto.Todo;

public interface TodoService {
	
	/**
	 * (Test) todoNo가 1인 할 일의 제목 조회
	 * @return
	 */
	String testTitle();

	/**
	 * 할 일 목록 + 완료된 할 일 개수 조회
	 * @return Map
	 */
	Map<String, Object> selectAll();

	int addTodo(String todoTitle, String todoContent);

	/**
	 * 할 일 상세 조회
	 * @param todoNo
	 * @return
	 */
	Todo todoDetail(int todoNo);

	/**
	 * 할 일 삭제
	 * @param todoNo
	 * @return
	 */
	int todoDelete(int todoNo);

}
