package edu.kh.project.myPage.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MyPageMapper {

	/**
	 * 회원 정보를 수정하는 SQL문 작성
	 * @param inputMember
	 * @return
	 */
	int updateInfo(Member inputMember);

	/**
	 * DB에 저장된 비밀번호 가져오기
	 * @param loginMember
	 * @return
	 */
	String getPw(Member loginMember);

	/**
	 * 비밀번호를 업데이트하는 SQL문 작성
	 * @param encPw
	 * @return
	 */
	int updatePw(Member loginMember);


}
