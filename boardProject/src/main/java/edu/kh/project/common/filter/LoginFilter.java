package edu.kh.project.common.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/* Filter : 요청, 응답 시 걸러내거나 추가할 수 있는 객체
 * [Filter Class 생성 방법]
 * 1. jakarta.servlet.Filter 인터페이스 상속 받기
 * 2. doFilter() 메서드 오버라이딩 */

// 로그인이 되어있지 않은 경우, 특정 페이지를 접근할 수 없도록 필터링함
public class LoginFilter implements Filter {
	
	// 필터 동작을 정의하는 메서드
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// ServletRequest : HttpServletRequest의 부모 타입
		// ServletResponse : HttpServletResponse의 부모 타입
		
		// session이 필요 > session에 담긴 loginMember를 꺼내오기 위해서(로그인 되어있는지 여부를 확인할 수 있음)
		// HttpServletRequest 형태(자식 형태)로 다운캐스팅
		HttpServletRequest req = (HttpServletRequest) request;
		// HttpServletResponse 형태(자식 형태)로 다운캐스팅
		HttpServletResponse resp = (HttpServletResponse)response;
		
		// 현재 요청의 URI를 가져옴
		String path = req.getRequestURI();
		
		// 프로필 이미지(/myPage/profile... 로 시작)을 보여주기 위해 > 요청 URI가 "/myPage/profile"로 시작하는지 확인
		if(path.startsWith("/myPage/profile/")) {
			// 필터를 통과하도록 함
			// FilterChain : 다음 필터 또는 DispatcherServlet(다음 필터가 없는 경우)과 연결된 객체
			chain.doFilter(request, response);

			// 필터를 통과한 후, return
			return;
			
		}
		
		// session 객체 얻어오기
		HttpSession session = req.getSession();
		
		// 세션에서 로그인한 회원 정보를 꺼내옴 > loginMember가 있는지 or null인지 확인부터
		if(session.getAttribute("loginMember") == null) {
			// 로그인이 되어있지 않은 상태
			// '/loginError'로 재요청(redirect)
			resp.sendRedirect("/loginError");
		} else {
			// 로그인이 되어있는 상태
			// 다음 필터로, 다음 필터가 업다면 DispatcherServlet으로 요청과 응답 전달
			chain.doFilter(request, response);
		}
	}
}
