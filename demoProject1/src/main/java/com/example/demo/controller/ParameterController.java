	package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.dto.Member;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("param")
// @Slf4j : log를 이용한 메시지를 콘솔창에 출력할 때 사용(lombok에서 제공)
@Slf4j
public class ParameterController {
	
	// /param/main GET 방식 요청 매핑
	@GetMapping("main")
	public String paramMain() {
		
		// src/main/resources/templates/	 param/param-main 	.html
		return "param/param-main";
	}
	
	/* HttpServletRequest : 요청 클라이언트의 정보, 제출된 파라미터 등을 저장한 객체로 클라이언트 요청 시 생성
	 * Spring의 Controller단 메서드 작성 시 매개변수에 원하는 객체를 작성하면 > 존재하는 객체를 바인딩 or 없으면 생성
	 * > ArgumentResolver (전달 인자 해결사) */
	// /param/test1 POST 방식 요청 매핑
	@PostMapping("test1")
	public String paramTest1(HttpServletRequest req) {
		String inputName = req.getParameter("inputName");
		String inputAddress = req.getParameter("inputAddress");
		int inputAge = Integer.parseInt(req.getParameter("inputAge"));
		
		// debug 레벨로 로그 출력
//		[2m2025-11-24T16:17:5...[0;39m [2m:[0;39m inputName : 홍길동
//		[2m2025-11-24T16:17:5...[0;39m[36mc.e.demo.controller.ParameterController [0;39m [2m:[0;39m inputAddress : 서울
//		[2m2025-11-24T16:17:5...[0;39m[36mc.e.demo.controller.ParameterController [0;39m [2m:[0;39m inputAge : 20
		log.debug("inputName : " + inputName);
		log.debug("inputAddress : " + inputAddress);
		log.debug("inputAge : " + inputAge);
		
		/* Spring에서 Redirect(재요청)하는 방법
		 * - Controller 메서드 반환값에 "redirect:재요청주소"; 작성
		 * * redirect는 get 방식 */
		return "redirect:/param/main";
	}
	
	 /* 2. @RequestParam 어노테이션 - 낱개 파라미터 얻어오기
	 * - request 객체를 이용한 파라미터 전달 어노테이션
	 * - 매개변수 앞에 해당 어노테이션을 작성하면, 매개변수에 값이 주입됨.
	 * - 주입되는 데이터는 매개변수의 타입에 맞게 형변환이 자동으로 수행됨.
	 *
	 * [기본 작성법]
	 * @RequestParam("key") 자료형 매개변수명
	 *
	 * [속성 추가 작성법]
	 * @RequestParam(value="key", required=false, defaultValue="1")
	 *
	 * value : 전달받은 input 태그의 name 속성값 (파라미터 key)
	 * required : 입력된 name 속성값 파라미터 필수 여부 지정 (기본값 true)
	 * -> required = true인 파라미터가 존재하지 않는다면(name 속성이 존재하지 않는다면) 400(Bad Request) 에러 발생
	 * ex) There was an unexpected error (type=Bad Request, status=400). Required parameter 'publisher' is not present.
	 * -> "" (빈문자열)일 때는 에러 발생 X (파라미터가 존재하지 않는것이 아니라 name속성값="" 로 넘어오기 때문에)
	 * defaultValue : 파라미터 중 일치하는 name속성값이 없을 경우에 대입할 값 지정. -> required=false 인 경우 사용
	 */
	// 요청 경로
	@PostMapping("test2")
	public String paramTest2(@RequestParam("title") String title, @RequestParam("writer") String writer,
			// Method parameter 'price': Failed to convert value of type 'java.lang.String' to required type 'int'; For input string: ""
			// 빈 문자열을 String으로 받아주는 것은 가능하나 price는 int 형으로 자동 형변환이 적용되기에 빈 문자열을 정수형으로 변환시킬 수 없어 오류 발생
			@RequestParam("price") int price,
			@RequestParam(value = "publisher", required = false, defaultValue = "한빛출판사") String publisher) {
		log.debug("title : " + title);
		log.debug("writer : " + writer);
		log.debug("price : " + price);
		log.debug("publisher : " + publisher);
		
		// return 구문에 작성하는 문자열 = 파일 경로
		// forward(forward할 html의 파일 경로)하거나 redirect(재요청 주소로 'redirect:' 작성해야)
		return "redirect:/param/main";
	}
	
	/**
	 * 3. RequestParam 여러 개 파라미터 얻어오기
	 * - 같은 name 속성값을 가진 파라미터 얻어오기 (String[], List<String>)
	 * - 제출된 파라미터를 한 번에 묶어서 얻어오기 (Map<String, Object>)
	 */
	@PostMapping("test3")
	public String paramTest3(@RequestParam("color") String[] colorArr, @RequestParam("fruit") List<String> fruitList,
			@RequestParam Map<String, Object> paramMap) {
		
		// colorArr : [Red, Green, Blue]
		log.debug("colorArr : " + Arrays.toString(colorArr));
		// fruitList : [Apple, Banana, Orange]
		log.debug("fruitList : " + fruitList);
		// @RequestParam Map<String, Object> paramMap
		// -> 제출된 모든 파라미터가 Map에 저장된다 -> 같은 name 속성을 가진 파라미터는 배열이나 List 형태가 아님
		// -> 첫 번째로 제출된 value 값만 저장됨
		// paramMap : {color=Red, fruit=Apple, productName=협탁, expirationDate=2025-11-27}
		log.debug("paramMap : " + paramMap);
		
		// 절대 경로 작성법
		return "redirect:/param/main";
	}
	
	
	/** 4. @ModelAttribute를 이용한 파라미터 얻어오기
	 * @ModelAttribute
	 * - DTO(또는 VO)와 함께 사용하는 어노테이션으로
	 * 전달되는 파라미터의 name 속성값이 함께 사용되는 DTO의 필드명과 같다면 자동으로 setter를 호출해서 필드에 값을 지정
	 * > ** DTO에 기본 생성자와 setter가 필수로 존재해야 함 **
	 * @ModelAttribute를 이용해 값이 필드에 세팅된 객체를 "커맨드 객체"라고 부름
	 * @ModelAttribute 생략 가능 */
	@PostMapping("test4")
	// 클라이언트의 요청 데이터를 Member 객체(DTO)에 넣기 위해 > 객체 생성이 우선 > @NoArgsConstructor 필요 (빈 객체 초기화) > 이후 setter로 값 대입
	// + @AllArgsConstructor만 존재 = 기본 생성자가 없고, 인자가 있는 생성자만 존재 > 인자 채워줄 방법이 없어 오류 발생
	public String paramTest4(/*@ModelAttribute*/ Member inputMember) {
		// inputMember : Member(memberId=member01, memberPw=pass01, memberName=홍길동, memberAge=20)
		log.debug("inputMember : " + inputMember);
		
		// 상대 경로 작성법 > **현재 위치** > 현재 경로의 가장 마지막 레벨의 주소값을 "redirect:작성된주소"로 변경하기 때문
		return "redirect:main";
	}
}