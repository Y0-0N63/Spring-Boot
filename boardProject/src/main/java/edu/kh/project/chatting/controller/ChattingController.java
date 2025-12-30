package edu.kh.project.chatting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.kh.project.chatting.model.dto.ChattingRoom;
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
}
