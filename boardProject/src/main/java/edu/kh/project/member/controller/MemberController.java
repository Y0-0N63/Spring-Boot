package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("member")
@Slf4j
public class MemberController {
	@Autowired // 의존성 주입(DI)
	private MemberService service;
	
	/** [로그인]
	 * - 특정 사이트에 아이디(이메일), 비밀번호 입력해 > 해당 정보가 존재하면 > 조회/서비스 이용
	 * - 로그인한 회원 정보를 session scope에 기록해 > 로그아웃 or 브라우저 종료 시까지 해당 정보를 브라우저에서 계속 이용할 수 있게 함
	 * @param inputMember : 커맨드 객체로 @ModelAttribute는 생략 가능
	 * 					  : memberEmail, memberPw가 세팅된 상태
	 * @return
	 */
	@PostMapping("login")
	public String login(/* @ModelAttribute */ Member inputMember) {
		// 로그인 서비스 호출
		try {
			Member loginMember = service.login(inputMember);
		} catch (Exception e) {
			log.info("로그인 중 예외 발생");
			e.printStackTrace();
		}
		
		return "";
	}
}
