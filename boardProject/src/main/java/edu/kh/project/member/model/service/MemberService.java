package edu.kh.project.member.model.service;

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

}
