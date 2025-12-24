package edu.kh.project.common.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
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
	@Before("PointcutBundle.controllerPointCut()")
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
	
	// @Around 사용 시 > 반환형은 무조건 Object
	// @Around 메서드 종료 시 > proceed 반환값을 return해야만 함
	// ProceedingJoinPoint : proceed 메서드를 제공하며, JoinPoint를 상속한 자식 객체로 @Around에서 사용 가능
	// 	-> proceed() 메서드 호출 전후로 Before/After가 구분되어짐
	/**
	 * 서비스 수행 전/후로 동작하는 코드(클래스, 메서드, 파라미터, 비즈니스 로직의 실제 실행 시간)
	 * @return
	 * @throws Throwable 
	 */
	@Around("PointcutBundle.serviceImplPointCut()")
	public Object aroundServiceImpl(ProceedingJoinPoint pjp) throws Throwable {
		// Throwable : 예외 처리의 최상위 클래스 > Exception은 Throwable 자식 클래스 (Throwable - Exception/Error)
		// + Exception(예외) : 개발자가 처리할 수 있는 문제, Error(오류) : 시스템 레벨의 심각한 문제(H/W의 문제 등)
		
		// AOP가 적용된 클래스 이름 얻어오기
		String className = pjp.getTarget().getClass().getSimpleName();
		
		// 실행된 컨트롤러의 메서드명 얻어오기
		String methodName = pjp.getSignature().getName();

		log.info("-------------------------- {}.{} 서비스 호출 --------------------------", className, methodName);
		
		// 파라미터를 로그로 출력
		log.info("Parameter : {}", Arrays.toString(pjp.getArgs()));
		
		// 서비스의 주요 비즈니스 로직 코드 실행 소요 시간을 기록하기
		long startMs = System.currentTimeMillis();
		
		// ---------------------------- ^ Before // v After ----------------------------
		Object obj = pjp.proceed(); // 전/후를 나누는 기준점이 됨
		
		long endMs = System.currentTimeMillis();
		
		log.info("RunningTime : {}ms", endMs-startMs);
		log.info("----------------------------------------------------");
		
		return obj;
	}
	
	/**	 접속자 IP 얻어오는 메서드
	 * @param request
	 * @return ip */
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