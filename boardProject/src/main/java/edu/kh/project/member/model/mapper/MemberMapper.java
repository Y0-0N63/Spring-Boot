package edu.kh.project.member.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MemberMapper {

	/**
	 * 로그인 SQL 실행
	 * @param memberEmail
	 * @return loginMember
	 */
	Member login(String memberEmail) throws Exception;

	/**
	 * 이메일 중복 검사 SQL 실행
	 * @param memberEmail
	 * @return
	 */
	int checkEmail(String memberEmail);

	/**닉네임 중복 검사 SQL 실행
	 * @param memberNickname
	 * @return
	 */
	int checkNickname(String memberNickname);

	/**
	 * 회원가입 SQL 실행
	 * @param inputMember
	 * @return
	 */
	int signup(Member inputMember);

	/* ==================== Ajax ==================== */
	
	/**
	 * 회원 리스트 조회 SQL 실행
	 * @return
	 */
	List<Member> getMemberList();

	/**
	 * 입력받은 회원의 비밀번호를 pass01!로 업데이트하는 SQL 실행
	 * @param resetMemberNo
	 * @param bcryptPassword
	 * @return
	 */
	int resetPw(Map<String, Object> map);

}