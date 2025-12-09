package edu.kh.project.myPage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.service.MyPageService;
import lombok.extern.slf4j.Slf4j;

// @sessionAttributes : Model에 추가된 속성 중, key 값이 일치하는 속성을 session scope로 변경하는 어노테이션
// - 클래스 상단에 @SessionAttributes({"loginMember"})처럼 작성해주기
// @SessionAttribute : @SessionAttributes를 통해 session에 등록된 속성을 꺼내올 때 사용하는 어노테이션
// - 메서드의 매개변수 자리에 @SessionAttribute("loginMember") Member loginMember 작성
@SessionAttributes({"loginMember"})
@Controller
@RequestMapping("myPage")
@Slf4j
public class MyPageController {
	
	@Autowired
	private MyPageService service;
	
	/**
	 * 내 정보 조회
	 * @param loginMember : 세션에 존재하는 loginMember를 얻어와 Member 타입의 매개변수에 대입
	 * @return
	 */
	@GetMapping("info") // /myPage/info GET 방식 요청 매핑
	public String info(@SessionAttribute("loginMember") Member loginMember, Model model) {
		
		// 현재 로그인한 회원의 주소를 꺼내옴
		// 현재 로그인한 회원 정보 > session scope에 등록된 상태(loginMember, memberAddress도 포함된 상태)
		// > 회원가입 당시, 주소를 입력했다면 주소값에 문자열이 들어가있을 것 (^^^ 구분자를 포함해 만들어진 문자열), 입력하지 않았다면 null
		String memberAddress = loginMember.getMemberAddress();
		
		// 주소가 있을 경우에만 동작
		if(memberAddress != null) {
			// 구분자 "^^^"를 기준으로 memberAddress 값을 쪼개 > String[] 형태로 반환
//			String[] arr = memberAddress.split("^^^"); > 정규표현식을 파라미터로 가지기 때문에 "^^^"로 작성하면 정규표현식에서 시작의 의미를 갖는 ^로 인식
			String[] arr = memberAddress.split("\\^\\^\\^");
			
			model.addAttribute("postcode", arr[0]); // 우편주소
			model.addAttribute("address", arr[1]); // 도로명/지번주소
			model.addAttribute("detailAddress", arr[2]); // 상세주소
		}
		
		return "myPage/myPage-info";
	}

	// 프로필 이미지 변경 화면으로 이동
	@GetMapping("profile")
	public String profile() {
		return "myPage/myPage-profile";
	}
	
	@GetMapping("changePw")
	public String changePw() {
		return "myPage/myPage-changePw";
	}
	
	@GetMapping("secession")
	public String secession() {
		return "myPage/myPage-secession";
	}
	
	@GetMapping("fileTest")
	public String fileTest() {
		return "myPage/myPage-fileTest";
	}
	
	@GetMapping("fileList")
	public String fileList() {
		return "myPage/myPage-fileList";
	}
	
	/**
	 * 회원 정보 수정
	 * @param inputMember : 커맨드 객체 (@ModelAttribute가 생략된 상태)로 제추로딘 memberNickname, memberTel이 세팅된 상태
	 * @param : memberAddress : 주소만 따로 배열 형태로 얻어옴
	 * @param : loginMember : 로그인한 회원 정보(현재 로그인한 회원의 회원 번호(PK) 사용할 예정
	 * @return
	 */
	@PostMapping("info")
	public String updateInfo(Member inputMember, @RequestParam("memberAddress") String[] memberAddress,
							@SessionAttribute("loginMember") Member loginMember, RedirectAttributes ra)  {

		// inputMember에 현재 로그인한 회원의 번호  > 로그인한 회원 번호 추가
		inputMember.setMemberNo (loginMember.getMemberNo());
		
		// 회원 정보 수정 서비스 호출
		int result = service.updateInfo(inputMember, memberAddress);
		
		return "redirect:info"; // 재요청 경로 : /myPage/info
	}
}