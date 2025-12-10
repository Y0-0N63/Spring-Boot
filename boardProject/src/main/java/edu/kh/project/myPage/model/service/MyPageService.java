package edu.kh.project.myPage.model.service;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.Member;

public interface MyPageService {
	/**
	 * 회원 정보 업데이트
	 * @param inputMember
	 * @param memberAddress
	 * @return
	 */
	int updateInfo(Member inputMember, String[] memberAddress);

	/**
	 * 회원 비밀번호 변경 서비스
	 * @param currentPw
	 * @param newPw
	 * @param loginMember
	 * @return
	 */
	int updatePw(String currentPw, String newPw, Member loginMember);

	/**
	 * 회원 탈퇴 서비스
	 * @param memberPw
	 * @param memberNo
	 * @return
	 */
	int secession(String memberPw, int memberNo);

	/**
	 * 파일 업로드 테스트 1
	 * @param uploadFile
	 * @return
	 * @throws Exception 
	 */
	String fileUpload1(MultipartFile uploadFile) throws Exception;

}
