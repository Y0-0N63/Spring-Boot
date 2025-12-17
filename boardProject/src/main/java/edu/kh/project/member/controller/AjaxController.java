package edu.kh.project.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;

@Controller
@RequestMapping("ajax")
public class AjaxController {
	
	@Autowired
	private MemberService service;
	
	@ResponseBody
	@GetMapping("memberList")
	public List<Member> getMemberList() {

		List<Member> memberList = service.getMemberList();
		
		return memberList;
	}
}
