package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/* @SessionAttributes({"key", "key", "key"...}) : Model에 추가된 속성 중 key값이 일치하는 속성을 session scope로 변경 */
@Controller
@RequestMapping("member")
@Slf4j
@SessionAttributes({"loginMember"})
public class MemberController {
	@Autowired // 의존성 주입(DI)
	private MemberService service;
	
	/** [로그인]
	 * - 특정 사이트에 아이디(이메일), 비밀번호 입력해 > 해당 정보가 존재하면 > 조회/서비스 이용
	 * - 로그인한 회원 정보를 session scope에 기록해 > 로그아웃 or 브라우저 종료 시까지 해당 정보를 브라우저에서 계속 이용할 수 있게 함
	 * @param inputMember : 커맨드 객체로 @ModelAttribute는 생략 가능
	 * 					  : memberEmail, memberPw가 세팅된 상태
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("login")
	// Model : 기본값이 request scope > @sessionAttributes와 함께 사용하면 session scope로 변경 가능
	public String login(/* @ModelAttribute */ Member inputMember, RedirectAttributes ra, Model model,
					@RequestParam(value="saveId", required = false) String saveId, HttpServletResponse resp) throws Exception {
		// 로그인 서비스 호출
//		try {
			Member loginMember = service.login(inputMember);
			// 올바른 비밀번호 입력 > loginMember : Member(memberNo=1, memberEmail=user01@kh.or.kr, memberPw=null, memberNickname=유저일, memberTel=01012341234...
			// 잘못된 비밀번호 입력 > loginMember : null
			log.debug("loginMember : " + loginMember);
			
			// 로그인 실패
			if(loginMember == null) {
				ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
			} else {
				// 로그인 성공 시
				// 1. request scope에 세팅됨
				// 2. 클래스 위에 @SessionAttributes() 어노테이션 작성해 > session scope로 이동
				model.addAttribute("loginMember", loginMember);
				
				// 쿠키(cookie)를 이용해 id 저장하기 > 이메일 저장할 쿠키 객체 생성(K:V)
				Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
				
				// 쿠키가 적용될 경로 설정 = 클라이언트가 어떤 요청을 할 때 쿠키가 첨부될지 결정
				// "/" -> IP or Domain or localhost > 메인 페이지 + 그 하위 주소들 모두
				cookie.setPath("/");
				
				// 쿠키의 만료 기간 지정
				if(saveId != null) {
					 cookie.setMaxAge(60*60*24*30); // 30일 초단위로 지정
				} else {
					// 0초 > 클라이언트의 쿠키 삭제
					cookie.setMaxAge(0);
				}
				
				// 응답 객체에 쿠키 추가해서 > 클라이언트에게 전달하기
				resp.addCookie(cookie);
			}
			
//		} catch (Exception e) {
//			log.info("로그인 중 예외 발생");
//			e.printStackTrace();
//		}
		
		return "redirect:/";
	}
	
	/**
	 * 로그아웃 : 세션에 저장된 로그인된 회원 정보를 없앰 (세션 스코프를 초기화)
	 * @paam SessionStatus : @SessionAttributes로 지정된 특정 속성을 세션에서 제거할 수 있는 기능을 제공하는 객체
	 * @return
	 */
	@GetMapping("logout")
	public String logout(SessionStatus status) {
		// session을 완료시킴(= 세션에 들어있던 내용 초기화)
		status.setComplete();
		
		return "redirect:/";
	}
	
	/**
	 * 회원 가입 페이지로 이동시켜주는 요청을 받아 처리
	 * @return
	 */
	@GetMapping("signup")
	public String signupPage() {
		return "member/signup";
	}
	
	/**
	 * 이메일 중복 검사(비동기 요청)
	 * ResponseBody : 응답 본문으로 응답값을 돌려보냄
	 * @return
	 */
	@ResponseBody
	@GetMapping("checkEmail")
	public int checkEmail(@RequestParam("memberEmail") String memberEmail) {
		
		return service.checkEmail(memberEmail);
	}
	
	/**
	 * 닉네임 중복 검사
	 * @param memberNickname
	 * @return 중복 1, 중복이 아니라면 0
	 */
	@ResponseBody
	@GetMapping("checkNickname")
	public int checkNickname(@RequestParam("memberNickname") String memberNickname) {
		return service.checkNickname(memberNickname);
	}
	
	/**
	 * 동기식 요청이기 때문에  > String
	 * @param inputMember : 커맨드 객체(입력된 회원 정보) memberEmail, memberPw, memberNickname, memberTel(memberAddress도 우편번호-필요는 없음)
	 * @param memberAddress : 입력한 주소 input 3개의 값을 배열로 전달[우편번호, 도로명/지번주소, 상세 주소]
	 * @param ra : RedirectAttributes로 리다이렉트 시 1회성으로 req->session->req로 전달되는 객체
	 * @return
	 */
	@PostMapping("signup")
	public String signup(@ModelAttribute Member inputMember, @RequestParam("memberAddress") String[] memberAddress,
						RedirectAttributes ra) {
		// 회원 가입 서비스 호출
		int result = service.signup(inputMember, memberAddress);
		
		String path = null;
		String message = null;
		
		if(result > 0) { // 성공 > redirect:/ (메인페이지 재요청)
			message = inputMember.getMemberNickname() + "님의 가입을 환영합니다!";
			path = "/";
		} else { // 실패 > redirect:signup (상대경로, 현재 경로 /member/signup)
			message = "회원가입 실패...";
			path = "signup";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
}
