package edu.kh.project.board.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.board.model.service.EditBoardService;
import edu.kh.project.member.model.dto.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("editBoard")
@Slf4j
@RequiredArgsConstructor
public class EditBoardController {
	
	private final EditBoardService service;
	private final BoardService boardService;

	@GetMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(@PathVariable("boardCode") int boardCode) {
		// src/main/resource/templates/ 	board/boardWrite	.html로 forward
		return "board/boardWrite";
	}
	
	/**
	 * 게시글 작성
	 * @param boardCode
	 * @param inputBoard : 입력된 값(제목, 내용)이 세팅되어 있음 (커맨드 객체)
	 * @param loginMember : 로그인한 회원 번호를 얻어오는 용도 (세션에 등록되어있음)
	 * @param images : 제출된 file 타입 > input 태그가 전달한 데이터들 (이미지 파일...)
	 * @return
	 */
	@PostMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(@PathVariable("boardCode") int boardCode, @ModelAttribute Board inputBoard,
							@SessionAttribute("loginMember") Member loginMember, @RequestParam("images") List<MultipartFile> images,
							RedirectAttributes ra) throws IllegalStateException, IOException {
		
		log.debug("images : " + images);
		/* 첨부된 이미지가 없어도 > [MulipartFile, MulipartFile, MulipartFile, MulipartFile, MulipartFile]
		 * List<MultipartFile> images는 실제 제출된 파일이 있던 없던 > 무조건 길이 5의 MultipartFile이 요소로 있는 List가 제출됨
		 * - 5개 모두 업로드 0 -> 0~4 인덱스에 실제 파일이 들어간 MultipartFile이 저장됨
		 * - 2번 인덱스만 업로드 -> 2번 인덱스 파일이 저장된 MultipartFile, 나머지  0, 1, 3, 4번 인덱스에는 MultipartFile이 비어있음
		 * >> 무작정 서버에 저장 X > List의 각 인덱스에 들어있는 MultipartFile에 실제로 제출된 파일이 있는지 확인해야 함!
		 * *참고! List 요소의 index 번호 = IMG_ORDER와 같음 */
		
		// 1. boardCode, 로그인한 회원 번호를 inputBoard에 세팅
		inputBoard.setBoardCode(boardCode);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		// -> inputBoard 총 네 가지 세팅됨(boardTitle, boardContent, boardCode, memberNo)
		
		// 2. 서비스 호출 후 결과 반환 받기 > 성공 시 [상세 조회]를 요청할 수 있도록 > 삽입된 게시글 번호를 반환받기
		int boardNo = service.boardInsert(inputBoard, images);
		
		// 3. 서비스 결과에 따라 > message, redirect 경로 지정
		String path = null;
		String message = null;
		
		if(boardNo > 0) {
			path = "/board/" + boardCode + "/" + boardNo;
			message = "게시글이 작성되었습니다.";
		} else {
			path = "insert"; // 상대경로 > 현재 요청 주소 : /editBoard/1/insert > 게시글 삽입 페이지를 다시 보여줌
			message = "게시글 작성에 실패하였습니다.";
		}
		
		ra.addFlashAttribute("message", message);
		return "redirect:" + path;
	}
	
	/**
	 * 게시글 수정 화면 전환
	 * location.href 통해 요청을 보냄 > get
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
	public String boardUpdate(@PathVariable("boardCode") int boardCode, @PathVariable("boardNo") int boardNo,
							@SessionAttribute("loginMember") Member loginMember, Model model, RedirectAttributes ra) {
		
		// 수정 화면에 출력할 기존의 제목, 내용, 이미지 조회
		Map<String, Integer> map = new HashMap<>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		// BoardService.selectOne(map) 호출 결과값 반환 받기
		Board board = boardService.selectOne(map);
		
		String message = null;
		String path = null;
		
		if(board == null) {
			// Get 방식이기 때문
			message = "해당 게시글이 존재하지 않습니다.";
			path = "redirect:/"; // 메인페이지로 리다이렉트
			
			ra.addFlashAttribute("message", message);
		} else if(board.getMemberNo() != loginMember.getMemberNo()) {
			message = "자신이 작성한 글만 수정할 수 있습니다.";
			
			// 해당 글 상세 조회 리다이렉트 (/board/1/2000)
			path = String.format("redirect:/board/%d/%d", boardCode, boardNo);
			
			ra.addFlashAttribute("message", message);
		} else {
			path = "board/boardUpdate"; // forward	templates/board/boardUpdate.html
			model.addAttribute("board", board);
		}
		
		return path;
	}
	
	/**
	 * 게시글 수정 요청
	 * @param inputBoard : 커맨드 객체(제목, 내용)
	 * @param images : 제출된 type="file"인 모든 요소
	 * @param deleteOrderList : 삭제된 이미지 순서가 기록된 문자열 (ex_"1,2,3")
	 * @return
	 */
	@PostMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
	public String boardUpdate(@PathVariable("boardCode") int boardCode, @PathVariable("boardNo") int boardNo,
							@ModelAttribute Board inputBoard, @RequestParam("images") List<MultipartFile> images,
							@RequestParam(value = "deleteOrderList", required = false) String deleteOrderList,
							@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
							@SessionAttribute("loginMember") Member loginMember, // 자신이 작성한 글만 수정할 수 있도록 하기 위해
							RedirectAttributes ra) throws Exception {
		
		// 1. 커맨드 객체인 inputBoard(Board)에 boardCode, boardNo, memberNo 세팅해주기 > 제목, 내용, boardCode, boardNo, 회원번호 세팅 완료
		inputBoard.setBoardCode(boardCode);
		inputBoard.setBoardNo(boardNo);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		
		// 2. 게시글 수정 서비스 호출 후 결과 반환 받기
		int result = service.boardUpdate(inputBoard, images, deleteOrderList);
		
		// 3. 서비스 결과에 따라 응답 제어
		String message = null;
		String path = null;
		
		if(result > 0) {
			message = "게시글이 수정되었습니다.";
			path = String.format("/board/%d/%d?cp=%d", boardCode, boardNo, cp);
		} else {
			message = "게시글 수정에 실패하였습니다.";
			// 게시글 수정 화면으로 새로고침한 것처럼 redirect = GET 방식으로
			path = "update";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
}