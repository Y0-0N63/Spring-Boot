package edu.kh.project.common.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/* 스프링에서의 예외 처리 방법 (우선 순위별로 작성) 
 * 1. 메서드에서 직접 처리 (try-catch, throws...)
 * 2. 컨트롤러 클래스에서 클래스 단위로 모아서 처리 (@ExceptionHandler 어노테이션을 지닌 메서드를 해당 클래스에 작성)
 * 3. 별도 클래스를 만들어 프로젝트 단위로 모아서 처리 (@ControllerAdvice 어노테이션을 지닌 클래스를 작성)
 * 	ㄴ 이 방식을 사용하고자 ExceptionController 클래스를 만듦!
 * 	ㄴ 이 방식을 사용하더라도 각 메서드에 @ExceptionHandler를 작성해주어야 함 */

@ControllerAdvice // 전역적 예외 처리 활성화 어노테이션
public class ExceptionController {
	
	// @ExceptionHandler(예외 종류) : 어떤 예외를 다룰 것인지를 작성함
	// 예외의 종류
	// SQLException.class - SQL 관련 예외만 처리...
	// IOException.class - 입출력 관련 예외만 처리...
	
	// @Controller이므로 > 요청과 응답 사이에서 발생한 예외를 잡아 처리함 > 반환 타입이 String이어야만 함!
	@ExceptionHandler(NoResourceFoundException.class)
	public String notFound() { // 404 오류
		return "error/404";
	}
	
	// 프로젝트에서 발생하는 모든 종류의 Exception을 500으로 처리하고자 함
	@ExceptionHandler(Exception.class)
	public String allExceptionHandler(Model model, Exception e) {
		
		e.printStackTrace();
		model.addAttribute("e", e);
		return "error/500";
	}
}
