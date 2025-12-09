package edu.kh.project.myPage.model.service;

import org.springframework.beans.factory.annotation.Autowired;
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
}
