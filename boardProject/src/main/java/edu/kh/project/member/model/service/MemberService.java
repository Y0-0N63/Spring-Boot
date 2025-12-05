package edu.kh.project.member.model.service;

import edu.kh.project.member.model.dto.Member;

public interface MemberService {

	/** [로그인]
	 * @param inputMember
	 * @return loginMember
	 * @throws Exception 
	 */
	Member login(Member inputMember) throws Exception;

}
