package edu.kh.project.common.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/* AOP(Aspect-Oriented Programming) : 분산되어 있는 관심사/관점을 모듈화시키는 기법
 * - 주요 비즈니스 로직과 관련이 없는 부가적인 기능(로그 찍기, 트랜잭션 처리, 보안 처리 추가 등)을 추가할 때 유용
 * 
 * 주요 어노테이션
 * - @Aspect : Aspect를 정의하는 데 사용되는 어노테이션으로, 클래스 상단에 작성
 * - @Before(포인트컷) : Advice를 적용할 수 있는 대상 메서드(포인트컷)을 실행하기 전에 Advice를 실행
 * - @After(포인트컷) : 대상 메서드 실행 후에 Advice를 실행
 * - @Around(포인트컷) : 대상 메서드의 실행 전과 후로 Advice를 실행(@Before + @After) */

// @Bean : 메서드에서 반환되는 객체를 만듦! > 이 클래스를 객체로 만들기 위해서는 @Bean이 아닌 @Component 사용
@Component // Bean으로 등록
@Aspect // 공통 관심사가 작성된 클래스임을 명시(AOP 동작용 클래스)
@Slf4j // log를 찍을 수 있는 객체(Logger) 생성 코드를 추가
public class TestAspect {
	
	// Advice : 실제로 동작하는 코드 = 공통되는 부분을 따로 빼내어 작성하는 메소드 = 끼워넣을 코드(메서드)
	public void testAdvice() {
		log.info("-------------------------- testAdvice()가 수행됨 --------------------------");
	}
}
