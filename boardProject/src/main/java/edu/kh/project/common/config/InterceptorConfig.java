package edu.kh.project.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import edu.kh.project.common.interceptor.BoardTypeInterceptor;

// 인터셉터가 어떤 요청을 가로챌지(언제 수행될지) 설정하는 클래스
@Configuration // 서버가 켜지면 Bean으로 등록하고 내부 메서드를 모두 수행
public class InterceptorConfig implements WebMvcConfigurer {
	// WebMvcConfigurer : Interceptor를 등록하는 메서드를 가지고 있음

	// Bean으로 등록하기 위해 new 연산자로 객체 생성하지 않고, 메서드로 작성
	@Bean // > 개발자가 수동으로 만든 객체지만 관리를 Spring Container가 수행하도록 할 수 있음
	public BoardTypeInterceptor boardTypeInterceptor() {
		return new BoardTypeInterceptor();
	}
	
	// 동작할 인터셉터 객체를 추가하는 메서드
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// Bean으로 등록된 BoardTypeInterceptor를 얻어와서 등록(언제 수행할지)하기 위한 절차
		registry
		.addInterceptor(boardTypeInterceptor()) // 메서드 자체를 등록
		.addPathPatterns("/**") // 가로챌 요청 주소를 '/**'로 설정 => '/' 이하 모든 요청
		.excludePathPatterns("/css/**", "/js/**", "/images/**", "favicon.ico"); // 가로채지 않을 요청 주소 (정적 리소스 요청은 가로채지 않음)
	}
}