package edu.kh.project.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {
	// 등록된 Bean 중에서 같은 타입 or 상속 관계인 Bean 의존성 주입(DI)
	// BCrypt 암호화 객체의존성 주입 (common.config.SecurityConfig)
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Autowired
	private MemberMapper mapper;

	@Override
	public Member login(Member inputMember) throws Exception {
		// 암호화 진행 (bcrypt.encode(문자열) : 문자열을 암호화해 반환)
		String bcryptPassword = bcrypt.encode(inputMember.getMemberPw());
		// pass01 입력했을 때 > bcryptPassword : $2a$10$nilHFN6gLec5/19WfzxJcObCiM/Cf7r1jtW30BE5yBAA0VfwysMI
//		log.debug("bcryptPassword : " + bcryptPassword);
		
		// 1. 이메일이 일치하면서 && 탈퇴하지 않은 회원의 비밀번호 조회
		Member loginMember = mapper.login(inputMember.getMemberEmail()); 
	
		// 2. 일치하는 이메일이 없는 경우 > 조회 결과가 null
		if (loginMember == null) return null;
		
		// 3. loginMember에 값이 존재한다면(!null) 
		// > 입력받은 비밀번호(평문, inputMember.getMemberPw())와 암호화된 비밀번호(loginMember.getMemberPw())가 일치하는지 확인
		// bcrypt.matches(평문, 암호화된 문장) : 평문과 암호화가 내부적으로 일치한다고 판단 > true, or not > false
		
		// 일치하지 않으면 > null 반환
		if(!bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) return null;
		
		// 일치하면 > loginMember의 암호화된 비밀번호 제거 > Controller로 반환 
		loginMember.setMemberPw(null);
		
		return loginMember;
	}

	@Override
	public int checkEmail(String memberEmail) {
		return mapper.checkEmail(memberEmail);
	}

	@Override
	public int checkNickname(String memberNickname) {
		return mapper.checkNickname(memberNickname);
	}
}