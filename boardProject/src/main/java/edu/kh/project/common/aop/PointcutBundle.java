package edu.kh.project.common.aop;

import org.aspectj.lang.annotation.Pointcut;

// Pointcut을 모아두는 클래스
// > 작성하기 어려운 Pointcut을 미리 작성해둔 후 > 필요한 곳에서 클래스명.메서드명()으로 호출해서 사용
public class PointcutBundle {

	// @Before("execution(* edu.kh.project..*Controller*.*(..))") -> @Before("PointBundle.controllerPointCut()")
	@Pointcut("execution(* edu.kh.project..*Controller*.*(..))")
	public void controllerPointCut() {}
	
	@Pointcut("execution(* edu.kh.project..*ServiceImpl*.*(..))")
	public void serviceImplPointCut() {}
}