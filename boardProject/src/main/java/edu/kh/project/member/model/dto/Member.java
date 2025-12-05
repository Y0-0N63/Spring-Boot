package edu.kh.project.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO (Data Transfer Object) : 데이터 전달용 객체로 DB에 조회된 결과 또는 SQL 구문에 사용할 값을 전달하는 용도
// - 관련성이 있는 데이터를 한 번에 묶어서 다룰 때 용이
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
	private int memberNo;
	private String memberEmail;
	private String memberPw;
	private String memberNickname;
	private String memberTel;
	private String memberAddress;
	private String profileImg;
	private String enrollDate;
	private char memberDelFl;
	private int authority;
}