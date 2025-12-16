package edu.kh.project.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.Pagination;
import edu.kh.project.board.model.mapper.BoardMapper;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardMapper mapper;

	// 게시판 종류 조회 서비스
	@Override
	public List<Map<String, Object>> selectBoardTypeList() {
		return mapper.selectBoardTypeList();
	}

	// 특정 게시판의 지정된 페이지 목록 조회 서비스
	@Override
	public Map<String, Object> selectBoardList(int boardCode, int cp) {
		// 1. 지정된 게시판(boardCode)에서 삭제되지 않은 게시글 수를 조회
		int listCount = mapper.getListCount(boardCode);
		
		// 2. 1번의 결과 + cp를 이용해 > Pagination 객체 생성
		// *Pagination 객체 : 게시글 목록 구성에 필요한 값을 저장한 객체
		Pagination pagination = new Pagination(cp, listCount);
		
		// 3. 특정 게시판의 지정된 페이지 목록 조회
		// ROWBOUNDS 객체(MyBatis 제공) : 지정된 크기만큼 건너뛰고(offset) 제한된 크기만큼(limit)의 행을 조회하는 객체 > 페이징 처리가 간단해짐
		int limit = pagination.getLimit(); // 10개
		int offset = (cp - 1) * limit; // 1page -> 0 = 0개 건너뛰고 10개 조회, 2page -> 10 = 10개 건너뛰고 10개 조회
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		// Mapper 메서드 호출 시 : 전달할 수 있는 매개변수는 원칙적으로 1개
		// 그러나 2개를 전달할 수 있는 예외가 존재하는데 > RowBounds를 이용할 때! (SQL에 전달할 파라미터, RowBounds 객체)
		// RowBounds 단독으로 전달도 가능 or null과 함께 전달도 가능
		List<Board> boardList = mapper.selectBoardList(boardCode, rowBounds);
		
		// 4. Pagination 객체 + 목록 조회 결과를 Map으로 묶음
		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		// 5. map 반환
		return map;
	}

	// 검색 서비스(게시글 목록 조회 참고)
	@Override
	public Map<String, Object> searchList(Map<String, Object> paramMap, int cp) {
		// 1. 지정된 게시판(boardCode)에서 검색 조건에 맞으면서 && 삭제되지 않은 게시글 수 조회
		int listCount = mapper.getSearchCount(paramMap);
		
		// 2. 1번의 결과(listCount) + cp를 이용해 > Pagination 객체 생성
		Pagination pagination = new Pagination(cp, listCount);
		
		// 3. 특정 게시판의 지정된 페이지 목록 조회(검색 포함)
		int limit = pagination.getLimit(); // 10개
		int offset = (cp - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		List<Board> boardList = mapper.selectSearchList(paramMap, rowBounds);
		
		// 4. 검색 목록 조회 결과 + Pagination 객체를 Map으로 묶음
		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}

	// 게시글 상세 조회
	@Override
	public Board selectOne(Map<String, Integer> map) {
		// mybatis에서 여러 SQL을 한 번에 실행하는 방법
		// 방법 1. 하나의 Service 메서드에서 여러 mapper 메서드 호출하기 > 각 테이블을 조회하는 select 메서드 만들어서 호출하기...
		
		// 방법 2. 수행하려는 SQL이 모두 SELECT이면서 && 먼저 조회된 결과 중 일부를 이용해서 나중에 수행되는 SQL의 조건으로 삼을 수 있는 경우
		// -> MyBatis의 <resultMap>, <collection> 태그를 이용해 > Mapper 메서드를 1회만 호출해도 여러 SELECT문을 한 번에 수행할 수 있음!
		return mapper.selectOne(map);
	}
	
}
