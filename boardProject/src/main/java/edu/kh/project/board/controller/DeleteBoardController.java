package edu.kh.project.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.service.DeleteBoardService;
import edu.kh.project.member.model.dto.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("editBoard")
@RequiredArgsConstructor
public class DeleteBoardController {

	private final DeleteBoardService service;
	
	// localhost/editBoard/1/2005/delete?cp=1
	@RequestMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/delete")
	public String deleteBoard(@PathVariable("boardCode") int boardCode,	 @PathVariable("boardNo") int boardNo,
			@ModelAttribute Board inputBoard,
			@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
			@SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra) {
		
		// 1. 커맨드 객체(inputBoard)에 boardCode, boardNo, memberNo 세팅
		inputBoard.setBoardCode(boardCode);
		inputBoard.setBoardNo(boardNo);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		
		// 2. 게시글 삭제 서비스 호출 후 결과 반환 받기
		int result = service.deleteBoard(inputBoard);
		
		// 3. 서비스 결과에 따라 응답 제어
		String message = "";
		String path = "";
		
		if(result > 0) {
			message = "게시글이 삭제되었습니다.";
			// localhost/board/1
			path = String.format("/board/%d?cp=%d", boardCode, cp);
			
		} else {
			message = "게시글 삭제에 실패하였습니다.";
			path = String.format("/board/%d/%d?cp=%d", boardCode, boardNo, cp);
		}

		ra.addFlashAttribute("message", message);
		return "redirect:" + path;
	} 
}
