package edu.kh.project.member.model.service;

import java.util.List;

import edu.kh.project.member.model.dto.Member;

public interface MemberService {

	/** [로그인]
	 * @param inputMember
	 * @return loginMember
	 * @throws Exception 
	 */
	Member login(Member inputMember) throws Exception;

	/**
	 * 이메일 중복 검사 서비스
	 * @param memberEmail
	 * @return
	 */
	int checkEmail(String memberEmail);

	/**
	 * 닉네임 중복 검사 서비스
	 * @param memberNickname
	 * @return
	 */
	int checkNickname(String memberNickname);

	/**
	 * 회원가입 서비스
	 * @param inputMember
	 * @param memberAddress
	 * @return
	 */
	int signup(Member inputMember, String[] memberAddress);

	/* ==================== Ajax ==================== */
	/**
	 * 회원 목록 조회
	 * @return
	 */
	List<Member> getMemberList();

	/**
	 * 회원의 비밀번호를 pass01!로 초기화
	 * @param resetMemberNo
	 * @return
	 */
	int resetPw(int resetMemberNo);

	int restoreMember(int memberNo);

}
