package edu.kh.project.myPage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;
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

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private MyPageService service;

    MyPageController(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
	
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
	public String fileList(Model model, @SessionAttribute("loginMember") Member loginMember) {
		// 파일 목록 조회 서비스 호출 (현재 로그인한 회원이 올린 이미지 가져오기)
		int memberNo = loginMember.getMemberNo();
		List<UploadFile> list = service.fileList(memberNo);
		
		// model에 list 담아서 forward
		model.addAttribute("list", list);
		
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
		
		String message = null;
		
		if(result > 0) {
			message = "회원 정보가 수정되었습니다.";
			
			// loginMember에 DB상 업데이트된 내용으로 다시 세팅해주기
			// -> loginMember는 세션에 저장된 로그인한 회원의 정보가 저장되어 있음 (= 로그인했을 당시의(= 수정 전) 기존 데이터)
			// -> loginMember를 수정하면 세션에 저장된 로그인한 회원의 정보가 업데이트됨 > 세션에 있는 회원 정보와 DB 데이터를 동기화해야 함!
			loginMember.setMemberNickname(inputMember.getMemberNickname());
			loginMember.setMemberTel(inputMember.getMemberTel());
			loginMember.setMemberAddress(inputMember.getMemberAddress());
		} else {
			message = "회원 정보 수정에 실패했습니다.";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:info"; // 재요청 경로 : /myPage/info
	}
	
	/**
	 * 비밀번호 변경
	 * @param currentPw
	 * @param newPw
	 * @param loginMember : 세션에 등록된 현재 로그인한 회원 정보
	 * @param ra : 리다이렉트 시 메시지 전달 역할
	 * @return
	 */
	@PostMapping("changePw")
	public String updatePw(@RequestParam("currentPw") String currentPw, @RequestParam("newPw") String newPw,
							@SessionAttribute("loginMember") Member loginMember, RedirectAttributes ra) {
		
		// 비밀번호 수정 서비스 호출
		int result = service.updatePw(currentPw, newPw, loginMember);
		
		String message = null;
		String path = null;
		
		if(result > 0) {
			message = "비밀번호가 변경되었습니다.";
			path = "info";
		} else {
			message = "현재 비밀번호가 일치하지 않습니다.";
			path = "changePw";
		}
			
		ra.addFlashAttribute("message", message);
		return "redirect:" + path;
	}
	
	/**
	 * 회원 탈퇴
	 * @param memberPw : 제출받은(사용자가 입력한) 현재 비밀번호
	 * @param loginMember : 현재 로그인한 회원의 정보를 가지고 있는 객체(세션에서 꺼내옴)
	 * 						> 회원 번호 필요 (SQL에서 조건으로 사용)
	 * @param status : @SessionAttributes()와 함께 사용!!
	 * @return
	 */
	@PostMapping("secession") // /myPage/secession  POST 요청 매핑
	public String secession(@RequestParam("memberPw") String memberPw, @SessionAttribute("loginMember") Member loginMember,
							SessionStatus status, RedirectAttributes ra) {
		
		// 로그인한 회원의 회원 번호 꺼내오기
		int memberNo = loginMember.getMemberNo();
		
		// 서비스 호출 (입력받은 비밀번호, 로그인한 회원 번호)
		int result = service.secession(memberPw, memberNo);
		
		String message = null;
		String path = null;
		
		// 탈퇴 성공 - 메인페이지 재요청
		if(result > 0 ) {
			message = "탈퇴되었습니다.";
			path = "/";
			
			// 탈퇴되었으므로 > 로그인이 되어있으면 안 됨 > 세션을 비워줘야(완료해줘야)
			status.setComplete();
		} else {
			// 탈퇴 실패 - 탈퇴 페이지로 재요청
			message = "비밀번호가 일치하지 않습니다.";
			path = "secession";
		}
		
		ra.addFlashAttribute("message", message);
		return "redirect:" + path;
	}
	
	// ================================ File ================================
	
	/* Spring에서 파일을 처리하는 방법
	 * - enctype="multipart/form-data"로 클라이언트의 요청을 받으면(문자, 숫자, 파일 등이 섞여있는 요청)
	 * > MultipartResolver(FileConfig에 정의)를 사용해 섞여있는 파라미터를 분리하는 작업을 함 
	 * 문자열, 숫자 > String
	 * 파일 > MultipartFile */
	/**
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("file/test1")  // /myPage/file/test1  POST 요청 매핑
	public String fileUpload1(@RequestParam("uploadFile") MultipartFile uploadFile, RedirectAttributes ra) throws Exception {
		
		try {
			// /myPage/flle/파일명.jpg
			String path = service.fileUpload1(uploadFile);
		
			
			// 파일이 실제로 서버 컴퓨터에 저장이 되어 웹에서 접근할 수 있도록 경로가 반환되었을 때
			if(path != null) {
				ra.addFlashAttribute("path", path);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("파일 예제 1 업로드 중 예외 발생:");
		}
				
		return "redirect:/myPage/fileTest";
	}
	
	@PostMapping("file/test2")
	public String fileUpload2(@RequestParam("uploadFile") MultipartFile uploadFile, @SessionAttribute("loginMember") Member loginMember,
							RedirectAttributes ra) {
		
		try {
			// 로그인한 회원의 번호를 얻어오기 (누가 업로드했는지 알기 위해)
			int memberNo = loginMember.getMemberNo();
			
			// 업로드된 파일 정보를 DB에도 INSERT해준 후 > 결과 행의 개수 반환받기
			int result = service.fileUpload2(uploadFile, memberNo);
			
			String message = null;
			
			if(result > 0) {
				message = "파일 업로드 성공";
			} else {
				message = "파일 업로드 실패";
			}
			
			ra.addFlashAttribute("message", message);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("파일 업로드 테스트2 중 예외 발생");
		}
		return "redirect:/myPage/fileTest";
	}
	
	@PostMapping("file/test3")
	public String fileUpload3(@RequestParam("aaa") List<MultipartFile> aaaList, @RequestParam("bbb") List<MultipartFile> bbbList,
							@SessionAttribute("loginMember") Member loginMember, RedirectAttributes ra) throws Exception {
		
//		// 아무 파일도 선택하지 않고 '제출하기'를 눌렀을 때
//		log.debug("aaaList: " + aaaList); // StandardMultipartFile@3bc75bf0, StandardMultipartFile@6385d0ac > [요소, 요소] > 0번, 1번 (파일은 모두 비어있음)
//		log.debug("bbbList: " + bbbList); // (multiple) StandardMultipartFile@4165391b > [요소] > 0번 (파일은 모두 비어있음)
//		// 세 개를 선택하고 '제출하기'를 눌렀을 때 : aaaList > 요소 2개, bbbList > 요소 3개
		
		// 여러 파일을 업로드할 수 있는 서비스 호출
		int result = service.fileUpload3(aaaList, bbbList, loginMember.getMemberNo());
		
		// result == aaaList와 bbbList에 업로드된 파일 갯수
		
		String message = null;
		
		if(result == 0) {
			message = "업로드된 파일이 없습니다.";
		} else {
			message = result + "개의 파일이 업로드되었습니다.";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:/myPage/fileTest";
	}
	
	@PostMapping("profile")
	public String profile(@RequestParam("profileImg") MultipartFile profileImg, @SessionAttribute("loginMember") Member loginMember, 
						RedirectAttributes ra) throws Exception {
		// 서비스 호출
		int result = service.profile(profileImg, loginMember);
		
		String message = null;
		
		if(result > 0) {
			message = "변경 성공!";
		} else {
			message = "변경 실패";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:profile";
	}
}