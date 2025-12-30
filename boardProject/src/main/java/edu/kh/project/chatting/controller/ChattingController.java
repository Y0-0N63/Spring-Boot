package edu.kh.project.chatting.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.kh.project.chatting.model.dto.ChattingRoom;
import edu.kh.project.chatting.model.dto.Message;
import edu.kh.project.chatting.model.service.ChattingService;
import edu.kh.project.member.model.dto.Member;

@Controller
@RequestMapping("chatting")
public class ChattingController {
	
	@Autowired
	private ChattingService service;

	/**
	 * 채팅 목록 조회 및 채팅 페이지로 전환(forward)
	 * > 채팅 페이지를 접속하자마자 채팅 목록이 조회되어야만 함
	 */
	@GetMapping("list")
	public String chatting(@SessionAttribute("loginMember") Member loginMember, Model model) {
		
		// 채팅 목록 조회를 위해 > 현재 로그인한 회원의 회원 번호 필요 > @SessionAttribute
		List<ChattingRoom> roomList = service.selectRoomList(loginMember.getMemberNo());
		model.addAttribute("roomList", roomList);
		
		return "chatting/chatting";
	}
	
	/**
	 * 채팅 상대 검색 (비동기)
	 * @RequestParam : 쿼리스트링으로 보낸 query를 받아줌
	 * @return SELECT한 Member가 담긴 List를 그대로 js로 전달(단일값이 아니므로 > JSON 형태)  
	 */
	@GetMapping("selectTarget")
	@ResponseBody
	public List<Member> selectTarget(@RequestParam("query") String query, @SessionAttribute("loginMember") Member loginMember) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("memberNo", loginMember.getMemberNo());
		map.put("query", query);
		
		return service.selectTarget(map);
	}
	
	/**
	 * JS(새로운 채팅방 입장 또는 기존 채팅방 선택 함수)에서 보낸 비동기
	 * 채팅방 입장 (없으면 새로운 채팅방을 생성)
	 * @return chattingNo(현재 채팅방 번호)
	 */
	@GetMapping("enter")
	@ResponseBody
	public int chattingEnter(@RequestParam("targetNo") int targetNo, @SessionAttribute("loginMember") Member loginMember) {
		
		Map<String, Integer> map = new HashMap<>();
		map.put("targetNo", targetNo);
		map.put("loginMemberNo", loginMember.getMemberNo());
		
		// 채팅방 번호 체크 서비스 호출 및 반환 (기존 생성된 방이 있는지)
		int chattingNo = service.checkChattingRoomNo(map);
		
		// 반환받은 채팅방의 결과값이 0이라면 > 없음을 의미 > 새로운 채팅방 생성하기
		if(chattingNo == 0) {
			chattingNo = service.createChattingRoom(map);
		}
		
		return chattingNo;
	}
	
	/**
	 * 채팅방 목록 조회(비동기)
	 * @return service.selectRoomList(loginMember.getMemberNo())
	 * List -> JSON 형태로 변환되어 JS로 전달
	 */
	@GetMapping("roomList")
	@ResponseBody
	public List<ChattingRoom> selectRoomList(@SessionAttribute("loginMember") Member loginMember) {
		
		return service.selectRoomList(loginMember.getMemberNo());
	} 
	
	/**
	 * 메세지 목록 조회(비동기)
	 * @RequestParamMap <String, Integer> paramMap: chattingRoomNo=${selectChattingNo}&memberNo=${loginMemberNo}
	 */
	@GetMapping("selectMessage")
	@ResponseBody
	public List<Message> selectMessageList(@RequestParam Map<String, Integer> paramMap) {
		
		return service.selectMessageList(paramMap);
	}
}
