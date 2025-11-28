package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

// @SpringBootApplication : SpringBootApplication에 필요한 필수 어노테이션과 설정을 모아둔 어노테이션
// @ComponentScan(Bean Scanning) 수행
// > @Component, @Repository, @Service, @Controller 어노테이션이 붙은 클래스 모두 찾아 > Bean으로 등록(객체로 생성)
@SpringBootApplication
public class DemoProject1Application {
	/* Spring Boot 프로젝트로 만든 애플리케이션의 실행을 담당하는 클래스
	 * Spring Application을 최소 설정으로 간단하고 빠르게 실행할 수 있게 해줌
	 * java 파일을 실행하듯이 Run 버튼(ctrl+f11) 클릭하면 자동으로 Tomcat 서버가 시작되며 배포가 시작됨 */
	public static void main(String[] args) {
		SpringApplication.run(DemoProject1Application.class, args);
	}
}