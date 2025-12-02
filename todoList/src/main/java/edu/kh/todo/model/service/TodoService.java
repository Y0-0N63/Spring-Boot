package edu.kh.todo.model.service;

import java.util.List;
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

	/** 
	 * 할 일 추가
	 * @param todoTitle
	 * @param todoContent
	 * @return
	 */
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

	/**
	 * 완료 여부 변경
	 * @param todo
	 * @return
	 */
	int changeComplete(Todo todo);

	/**
	 * 할 일 변경(수정
	 * @param todo
	 * @return
	 */
	int todoUpdate(Todo todo);

	/* =================================== Ajax =================================== */
	/**
	 * 전체 Todo 개수 조회
	 * @return
	 */
	int getTotalCount();

	/**
	 * 완료된 Todo 개수 조회
	 * @return
	 */
	int getCompleteCount();

	/**
	 * 전체 할 일 목록 조회
	 * @return
	 */
	List<Todo> selectList();

}
