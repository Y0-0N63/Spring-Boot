package edu.kh.project.board.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* 일반적으로 객체를 만들 때 new 연산자나 Setter를 사용
 * > 클래스의 속성 값이 많은 경우, 생성자 사용 시 코드가 길어져 가독성을 해침
 * > setter 함수를 활용하는 경우, 어디서 객체 속성 값이 변경되었는지 추적이 어려움
 * @Builder (Lombok)
 * 객체를 생성할 수 있는 빌더를 builder() 함수를 통해 얻어내 > 세팅하고자하는 값을 세팅 > build()를 통해 객체 생성
 * > Builder를 사용할 시, 체인 형태로 값을 넣을 수 있어 > 가독성이 좋고, 정해진 파라미터 순서대로 값을 넣을 필요가 없어 편리
 * > 필수 속성, 선택 속성을 나누어 활용할 수 없음(Pagination) */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {
	// Board 테이블 컬럼
	// 객체 생성 시 속성에 값이 직접 지정되지 않음
	private int boardNo;
	private String boardTitle;
	private String boardContent;
	private String boardWriteDate;
	private String boardUpdateDate;
	private int readCount;
	private String boardDelFl;
	private int boardCode;
	private int memberNo;
	
	// Member 테이블 조인 후 사용할 필드
	private String memberNickname;
	
	// 목록 조회 시 서브쿼리 필드
	private int commentCount;
	private int likeCount;
	
	// 게시글 작성자 프로필 이미지
	private String profileImg;

	// 게시글의 썸네일 이미지
	private String thumbnail;
	
	// 게시글 좋아요 여부 확인
	private int likeCheck;
	
	// 게시글 이미지 목록
	private List<BoardImg> imageList;
	
	// 게시글에 등록된 댓글 목록
	private List<Comment> commentList;
}