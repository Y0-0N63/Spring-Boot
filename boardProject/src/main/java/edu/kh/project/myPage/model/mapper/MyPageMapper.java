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

}
