package edu.kh.project.myPage.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;

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
	 * @param memberNo
	 * @return
	 */
	String getPw(int memberNo);

	/**
	 * 비밀번호를 업데이트하는 SQL문 작성
	 * @param encPw
	 * @return
	 */
	int updatePw(Member loginMember);

	/**
	 * 회원 탈퇴 SQL (update)
	 * @param memberNo
	 * @return
	 */
	int secession(int memberNo);

	/**
	 * 파일 정보를 DB에 삽입하는 SQL (INSERT)
	 * @param uf
	 * @return
	 */
	int insertUploadFile(UploadFile uf);

	/** 파일 목록을 조회하는 SQL
	 * @param memberNo
	 * @return
	 */
	List<UploadFile> fileList(int memberNo);
}
