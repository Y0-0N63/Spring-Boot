package edu.kh.project.board.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;
import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
			
		} else { // 검색인 경우 > paramMap에 key라는 k에 접근 > 매핑된 value 반환 ex) {key=w, query=짱구} > w 반환
			// boardCode를 paramMap에 추가 > {key=w, query=짱구, boardCode=1}
			paramMap.put("boardCode", boardCode);
			
			// 검색(내가 검색하고 싶은 게시글 목록 조회) 서비스 호출
			map = service.searchList(paramMap, cp);
		}
		
		// model(request scope)에 결과 값 등록
		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("boardList", map.get("boardList"));
		
		// src/main/resources/templates/	board/boardList		.html로 forward
		return "board/boardList";
	}
	
	// 상세 조회 요청 주소
	// @PathVariable("boardCode") int boardCode : URL에서 boardCode라는 이름으로 들어온 변수를 int형의 boardCode + Request Scope에 실어줌
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}")
	public String boardDetail(@PathVariable("boardCode") int boardCode, @PathVariable("boardNo") int boardNo,
							@SessionAttribute(value = "loginMember", required = false) Member loginMember, Model model, RedirectAttributes ra,
							HttpServletRequest req, HttpServletResponse resp // 요청에 담긴 쿠키 얻어오고 > 새로운 쿠키를 만들어서 응답하기 
							) {
		
		// 게시글 상세 조회 서비스 호출
		// 1) Map으로 전달할 파라미터 묶기
		Map<String, Integer> map = new HashMap<>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		// 로그인 경우인 상태에만 memberNo를 map에 추가할 수 있게 하기
		// LIKE_CHECK 시 이용 (로그인한 사람이 '좋아요'를 누른 게시글인지 체크하기 위해)
		if(loginMember != null) {
			map.put("memberNo", loginMember.getMemberNo());
		}
		
		// 2) 서비스 호출 (한 개의 게시글 조회하기)
		Board board = service.selectOne(map);
		
//		log.debug("조회된 board : " + board);
		
		String path = null;
		
		// 조회 결과가 없는 경우 (없는 게시글을 조회하고자 할 경우) > 현재 내가 보고 있는 게시판의 게시글 목록으로 redirect(재요청)
		if(board == null) {
			path = "redirect:/board/" + boardCode;
			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다.");
		} else {
			// 조회 결과가 있는 경우
			
			/* ======================================== 쿠키를 이용한 조회수 증가 ======================================== */

			// 비회원 또는 로그인한 회원의 글이 아닌 경우 (= 글쓴이를 제외한 다른 사람들)
			if(loginMember == null || board.getMemberNo() != loginMember.getMemberNo()) {
				
				// 요청에 담겨있는 모든 쿠키 얻어오기
				Cookie[] cookies = req.getCookies();
				Cookie c = null; // 쿠키 하나를 담아줄 수 있는 변수
				
				for(Cookie temp : cookies) {
					// 쿠키의 이름 중 "readBoardNo"가 존재할 때
					if(temp.getName().equals("readBoardNo")) {
						c = temp;
						break;
					}
				}
				
				int result = 0; // 조회수 증가 결과를 저장할 변수 (DB에도 전달해주어야)
				
				// "readBoardNo"가 쿠키에 없을 때
				if(c == null) {
					// 새 쿠키 생성 ("readBoardNo", [게시글번호])
					c = new Cookie("readBoardNo", "[" + boardNo + "]");
					result = service.updateReadCount(boardNo);
				} else {
					// "readBoardNo"가 쿠키에 있을 때 > readBoardNo(key) : [2](value)와 같이 한 개라도 목록에 있음
					// 현재 글을 처음 읽는 경우 > 해당 글 번호에 쿠키를 누적 + 서비스 호출
					if(c.getValue().indexOf("[" + boardNo + "]") == -1) {
						c.setValue(c.getValue() + "[" + boardNo + "]");
						result = service.updateReadCount(boardNo);
					}
				}
				
				// 조회 수 증가에 성공했을 때 + 조회 성공 시
				if(result > 0) {
					// 앞서 조회했던 board readCount 값을 > result 값을 다시 세팅
					board.setReadCount(result);
					
					// 쿠키 적용 경로 설정 > "/" 이하 경로 요청 시 쿠키를 서버로 전달
					c.setPath("/");
					
					// 쿠키 수명 지정
					// 현재 시간을 얻어오기
					LocalDateTime now = LocalDateTime.now();

					// 다음날 자정 지정
					LocalDateTime nextDayMidnight = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
					
					// 다음날 자정까지 남은 시간 계산(초단위) > 쿠키의 수명은 얼마나 남았는지로 지정해주어야
					long secondsUntilNextDay = Duration.between(now, nextDayMidnight).getSeconds();
					
					// 쿠키 수명 설정
					c.setMaxAge((int)secondsUntilNextDay);
					
					// 응답 객체를 이용해서 클라이언트에게 전달
					resp.addCookie(c);
				}
				
			}
			
			/* ======================================== 쿠키를 이용한 조회수 증가 ======================================== */
			
			// > 상세 조회할 수 있는 페이지로 forward (src/main/resources/templates/	board/boardDetail	.html)
			path = "board/boardDetail";
			
			// board - 게시글 일반 내용 + 게시글에 등록된 imageList + commentList
			model.addAttribute("board", board);
			
			// 조회된 이미지 목록(imageList)이 있을 경우
			if(!board.getImageList().isEmpty()) {
				// 썸네일은 없을 수도 있음 > null로 만들어둔 후 > 썸네일이 있다면 값을 채워주기
				BoardImg thumbnail = null;
				
				// imageList의 0번 인덱스 == IMG_ORDER가 가장 빠른 순서
				// 만약 이미지 목록의 0번째 요소의 IMG_ORDER가 0이라면 > 썸네일
				if(board.getImageList().get(0).getImgOrder() == 0) {
					thumbnail = board.getImageList().get(0);
				}
				
				// thumbnail 변수에는 > 이미지 목록의 0번째 요소가 썸네일이면 > 썸네일 이미지의 BoardImg객체가 들어가있음
				// or 썸네일이 아니라면 > null
				model.addAttribute("thumbnail", thumbnail);
				
				// start라는 key에 thumbnail이 null이 아닐 때 > 1 저장, null > 0 저장
				// 썸네일이 있을 때 > start = 1
				// 썸네일이 없을 때 > 일반 이미지만 있거나, 등록된 이미지가 아예 없을 때 > start = 0
				model.addAttribute("start", thumbnail != null ? 1 : 0);
			}
		}
		
		return path;
	}
	
}