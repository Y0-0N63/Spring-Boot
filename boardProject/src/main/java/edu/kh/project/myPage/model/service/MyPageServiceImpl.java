package edu.kh.project.myPage.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class MyPageServiceImpl implements MyPageService {
	
	@Autowired
	private MyPageMapper mapper;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@Override
	public int updateInfo(Member inputMember, String[] memberAddress) {
		// 입력된 주소가 있을 경우
		// A^^^B^^^C 형태로 가공
		// 주소가 입력되었을 때 (입력되지 않았다면 ",,"형태로 들어옴
		if(!inputMember.getMemberAddress().equals(",,")) {
			String address = String.join("^^^", memberAddress);
			inputMember.setMemberAddress(address);
		} else {
			// 주소가 입력되지 않았을 때
			inputMember.setMemberAddress(null);
		}
		
		// inputMember : 수정 닉네임, 수정 전화번호, 수정 주소, 회원 번호까지 세팅...
		
		return mapper.updateInfo(inputMember);
	}

	@Override
	public int updatePw(String currentPw, String newPw, Member loginMember) {
		// 입력한 현재 비밀번호(currentPw)와 DB에 저장된 비밀번호(loginMember.getMemberNo()를 통해 비밀번호 가져오기) 
		// 일치 여부 확인(bcrypt.matches(평문, 암호화된 문장))
		if(bcrypt.matches(currentPw, mapper.getPw(loginMember.getMemberNo()))) {
			// 일치할 경우 > 새 비밀번호(newPw)를 암호화(bcrypt.encode(문자열))
			String encPw = bcrypt.encode(newPw);
			loginMember.setMemberPw(encPw);
			// DB 업데이트
			return mapper.updatePw(loginMember);
		} else {
			// 일치하지 않을 경우
			return 0;
		}
	}

	/**
	 * 회원 탈퇴 서비스
	 */
	@Override
	public int secession(String memberPw, int memberNo) {
		
		// 1. 현재 로그인한 회원의 암호화된 비밀번호를 DB에서 조회
		String encPw = mapper.getPw(memberNo);
		
		// 2. 입력받은 비밀번호 & 암호화된 DB 비밀번호가 같은지 비교
		// 다를 경우
		if(!bcrypt.matches(memberPw, encPw)) {
			return 0;
		}
		
		// 일치할 경우
		return mapper.secession(memberNo);
	}
}
