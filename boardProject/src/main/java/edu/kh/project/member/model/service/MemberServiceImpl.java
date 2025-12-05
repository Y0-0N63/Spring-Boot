package edu.kh.project.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {
	// BCrypt 암호화 객체의존성 주입 (common.config.SecurityConfig)
	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@Override
	public Member login(Member inputMember) {
		// 암호화 진행 (bcrypt.encode(문자열) : 문자열을 암호화해 반환)
		String bcryptPassword = bcrypt.encode(inputMember.getMemberPw());
		// pass01 입력했을 때 > bcryptPassword : $2a$10$nilHFN6gLec5/19WfzxJcObCiM/Cf7r1jtW30BE5yBAA0VfwysMI
		log.debug("bcryptPassword : " + bcryptPassword);
	
		return null;
	}
}