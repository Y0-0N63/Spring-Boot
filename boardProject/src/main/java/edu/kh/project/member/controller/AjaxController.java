package edu.kh.project.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@ResponseBody // 작성하지 않을 시 오류 발생! (Unknown return value type: java.lang.Integer)
	// 비동기 통신은 데이터를 직접 보내나, 반환값인 int를 뷰(HTML) 파일 경로로 인식하기 때문에 타입 불일치 오류
	@PutMapping("resetPw")
	// @RequestBody : HTTP Body에 담아 보낸 데이터를 > Java 변수로 매핑(파싱)해줌
	public int resetPw(@RequestBody int resetMemberNo) {
		return service.resetPw(resetMemberNo);
	}
	
	@ResponseBody
	@PutMapping("restoreMember")
	public int restoreMember(@RequestBody int memberNo) {
		return service.restoreMember(memberNo);
	}
}
