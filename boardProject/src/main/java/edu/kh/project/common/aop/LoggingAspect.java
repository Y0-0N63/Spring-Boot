package edu.kh.project.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
	
	/* 컨트롤러 수행 전 로그 출력(클래스, 메서드, 요청 ip)
	 * "execution(* edu.kh.project..*Controller*.*(..))"를 매번 작성하는 것 대신 PointcutBundle에 모아두고 필요할 때 가져오기 */
	@Before("PointcutBundle.controllerPointCut")
	public void beforeController(JoinPoint jp) {
		// AOP가 적용된 클래스 이름 얻어오기
		String className = jp.getTarget().getClass().getSimpleName();
		
		// 실행된 컨트롤러의 메서드명 얻어오기
		String methodName = jp.getSignature().getName();
		
		// 요청한 클라이언트 ip 로그 출력
		// 1) 요청한 클라이언트의 HttpServletRequest 객체 얻어오기
		// > Controller가 아니기 때문에 요청-제어와 연관이 없음 > 사용자의 요청을 우회해서 가져옴(쓰레드에서 가져오기)
		HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		// 클라이언트 ip 얻어오기
		String ip = getRemoteAddr(req);
		
		// 문자열이 변할 때에는 StringBuilder가 용이 > 로그인 여부 따져줄 것
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("[%s.%s] 요청 / ip : %s", className, methodName, ip));
		
		// 로그인 상태인 경우
		if(req.getSession().getAttribute("loginMember") != null) {
			String memberEmail = ((Member)req.getSession().getAttribute("loginMember")).getMemberEmail();
			
			sb.append(String.format(", 요청 회원 : $s", memberEmail));
		}
		
		log.info(sb.toString());
	}
	
	/**
	 * 접속자 IP 얻어오는 메서드
	 *
	 * @param request
	 * @return ip
	 */
	private String getRemoteAddr(HttpServletRequest request) {
		// 클라이언트(사용자)의 실제 IP 주소를 찾아내기 위한 로직
		String ip = null;
		ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-RealIP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	

}