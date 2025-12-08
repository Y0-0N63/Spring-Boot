package edu.kh.project.email.model.service;

import java.util.Map;

public interface EmailService {

	/**
	 * 이메일 보내기 서비스
	 * "signup"을 Controller에서 만들어서 보냈는데 > 자동 완성된 추상 메서드의 매개변수에는 String형의 string으로 출력
	 * ㄴ 문자열을 어떤 변수에 담아주어야 할 지 몰라서
	 * @param type : 무슨 이메일을 발송할지 구분할 key로 사용됨
	 * @param email
	 * @return
	 */
	String sendEmail(String type, String email);

	int checkAuthKey(Map<String, String> map);

}
