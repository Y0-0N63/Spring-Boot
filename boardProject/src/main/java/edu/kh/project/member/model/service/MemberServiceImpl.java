package edu.kh.project.member.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	
	// 회원가입 서비스
	@Override
	public int signup(Member inputMember, String[] memberAddress) {
		// 1. 주소 배열 -> 하나의 문자열로 가공
		// 주소가 입력되지 않았다면 > memberAddress은 [, , ]로 출력됨 > inputMember.getMemberAddress 
		if(!inputMember.getMemberAddress().equals(",,")) {
			// String.join("구분자", 배열) > 배열의 모든 요소 사이에 "구분자"를 추가하여 하나의 문자열로 만들어 반환하는 메서드
			String address = String.join("^^^", memberAddress);

			// inputMember의 주소값을 위에서 만든 주소로 세팅
			inputMember.setMemberAddress(address);
		} else {
			// 주소가 입력되지 않은 경우
			inputMember.setMemberAddress(null);
		}
		
		// 2. 비밀번호 암호화
		// inputMember안의 memberPw = 평문 상태 > 비밀번호 암호화하여 inputMember에 세팅
		String encPw = bcrypt.encode(inputMember.getMemberPw());
		inputMember.setMemberPw(encPw);
		
		// 회원 가입 매퍼 메서드 호출
		return mapper.signup(inputMember);
	}
	
	/* ==================== Ajax ==================== */

	// 회원 목록 조회 서비스
	@Override
	public List<Member> getMemberList() {
		return mapper.getMemberList();
	}

	// 회원의 비밀번호를 pass01!로 초기화하는 서비스
	@Override
	public int resetPw(int resetMemberNo) {
		// 암호화 진행 (bcrypt.encode(문자열) : 문자열을 암호화해 반환)
		String bcryptPassword = bcrypt.encode("pass01!");
		
		Map<String, Object> map = new HashMap<>();
		map.put("resetMemberNo", resetMemberNo);
		map.put("bcryptPassword", bcryptPassword);
		
		return mapper.resetPw(map);
	}

	// 특정 회원(회원번호) 탈퇴 복구 > 입력받은 회원 번호의 > 탈퇴 여부(member_del_fl)을 'N'로 수정(PUT)
	@Override
	public int restoreMember(int memberNo) {
		return mapper.restoreMember(memberNo);
	}

}