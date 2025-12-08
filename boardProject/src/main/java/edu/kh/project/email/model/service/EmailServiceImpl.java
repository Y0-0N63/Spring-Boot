package edu.kh.project.email.model.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
	private final EmailMapper mapper;
	// 시렞 자바 메일 객체 발송을 담당
	private final JavaMailSender mailSender;
	// 타임리프를 이용해서 html 코드 > java 코드 변환
	private final SpringTemplateEngine templateEngine;
	
	@Override
	public String sendEmail(String type, String email) {
		// 1. 인증키 생성 > 발급한 인증키 DB에 저장
		String authKey = createAuthKey(); 
				
		// MyBatis에서 mapper로 전달할 수 있는 매개변수는 1개 > DTO or MAP 사용
		Map<String, String> map = new HashMap<>();
		map.put("authKey", authKey);
		map.put("email", email);
		
		// DB에 저장이 실패한 경우 > 해당 메서드 종료
		if(!storeAuthKey(map)) return null;

		// 2. DB에 저장이 성공된 경우 > 메일 발송 시도
		// MimeMessage : 메일 발송 시 사용하는 객체 (누가 누구에게 보내는가? 등...)
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		try {
			// 메일 발송을 도와주는 Helper 클래스 > 파일 첨부, 템플릿 설정 들을 쉽게 처리할 수 있음
			// - mimeMessage : MimeMessage 객체로, 이메일 메시지의 내용을 담고 있음(이메일의 본문, 제목, 수신자 정보 등을 포함)
			// - true : 파일 첨부를 사용할 것인지 여부 지정(파일 첨부 및 내부 이미지 삽입 가능)
			// - "UTF-8" : 이메일 내용을 UTF-8 인코딩으로 전송ㄴ
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			
			// 메일 기본 정보 세팅
			helper.setTo(email);
			helper.setSubject("[boardProject] 회원 가입 인증번호입니다."); // 제목 세팅

			// 문자만 보낼 경우
//			helper.setText("인증번호 : " + authKey);
			// html 파일로 보낼 경우
			helper.setText(loadHtml(authKey, type), true);
			// 메일 이미지 첨부(로고)
			helper.addInline("logo", new ClassPathResource("static/images/logo.jpg"));
			
			// 실제 메일 발송
			mailSender.send(mimeMessage);
			
			return authKey;
		} catch (Exception e) {
			e.printStackTrace();
			return null; // 메일 발송 실패 시 null 반환
		}
	}

	// HTML 템플릿에 데이터를 넣어 최종 HTML 생성
	private String loadHtml(String authKey, String type) {
		// Context(org.thymeleaf.context.Context) : 타임리프에서 제공하는 HTML 템플릿에 데이터를 전달하기 위해 사용하는 클래스
		Context context = new Context();
		context.setVariable("authKey", authKey);
		
		// src/main/resources/templates/email/signup.html
		return templateEngine.process("email/" + type, context);
	}

	// 인증키와 이메일을 DB에 저장하는 메서드
	@Transactional(rollbackFor = Exception.class) // 메서드 레벨에서도 이용 가능
	public boolean storeAuthKey(Map<String, String> map){
		// 1. 기존 이메일에 대한 인증키 update 수행
		int result = mapper.updateAuthKey(map);
		
		// 2. update 실패 시(반환값이 0일 때) insert 수행
		if(result == 0) {
			result = mapper.insertAuthKey(map);
		}
		
		// 3. 성공 여부 반환	
		return result > 0;
	}
	
	/**
	 * 인증 번호 발급 메서드 : 난수를 발생시켜 인증키를 생성해야 함
	 * UUID(Universally Unique IDentifier) : 전세계에서 고유한 식별자를 생성하기 위한 표준, 매우 낮은 확률로 중복되는 식별자 생성
	 * 주로 데이터베이스 기본 키, 고유한 식별자를 생성해야 할 때 사용
	 * @return UUID.randomUUID().toString().subString(0, 6)
	 */
	private String createAuthKey() {
		return UUID.randomUUID().toString().substring(0, 6);
	}

	@Override
	public int checkAuthKey(Map<String, String> map) {
		return mapper.checkAuthKey(map);
	}
	

}