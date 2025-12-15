package edu.kh.project.board.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.project.board.model.service.BoardService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("board")
@Slf4j
public class BoardController {
	
	@Autowired
	private BoardService service;
	
	/** 게시글 목록 조회
	 * "{boardCode}" : /board/xxx : /board 이하 1레벨 자리에 어떤 주소 값이 들어오든(숫자, 문자열 등 모두) 이 메서드에 매핑함
	 *  > /board/ 이하 1레벨 자리에 숫자로 된 요청 주소가 작성되어 있을 때에만 동작할 수 있도록 해야 함 > 정규표현식 사용
	 *  > "{boardCode:[0-9]+}
	 *  	- [0-9] : 한 칸에 0~9 사이의 숫자(한자릿수) 입력 가능
	 *  	- [0-9] : 모든 숫자
	 *  
	 *  @param boardCode : 게시판 종류 구분 번호(1, 2, 3)
	 *  @param cp : 현재 조회 요청한 페이지 번호(페이징 처리 관련 변수), 없으면 1(default)
	 *  @param model : forward할 때 관련 객체를 담아서 전달해줌
	 *  @param paramMap : 검색했을 경우 queryString에 {key=t, query=폭탄}로 전달됨 > 제출된 파라미터를 key, query를 Map 형태로 저장
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}")
	public String selectBoardList(@PathVariable("boardCode") int boardCode, @RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
								Model model, @RequestParam Map<String, Object> paramMap) {

		// 조회 서비스 호출 후 결과 반환
		Map<String, Object> map = null;
		
		// 검색이 아닌 경우 > paramMap이 {}로 비어있음
		if(paramMap.get("key") == null) {
			
			// 게시글 목록 조회 서비스 호출
			map = service.selectBoardList(boardCode, cp);
			
		} else { // 검색인 경우
			// 검색(내가 검색하고 싶은 게시글 목록 조회) 서비스 호출
		}
		
		// src/main/resources/templates/	board/boardList		.html로 forward
		return "board/boardList";
	}
}