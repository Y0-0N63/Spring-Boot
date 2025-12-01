package edu.kh.todo.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.todo.model.mapper.TodoMapper;

@Repository // DAO 계층 역할 명시 + Bean으로 등록
public class TodoDAO {
	// mapper에는 TodoMapper 인터페이스의 구현체가 의존성 주입될 것 > 해당 구현체는 sqlSessionTemplate을 이용하고 있음
	@Autowired
	private TodoMapper mapper;

	public String testTitle() {
		return mapper.testTitle();
	}

}
