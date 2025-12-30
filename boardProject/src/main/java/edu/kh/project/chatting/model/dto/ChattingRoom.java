package edu.kh.project.chatting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChattingRoom {
	private int chattingRoomNo;

	// 로그인한 회원이 참여한 채팅방 목록 조회 SQL문의 별칭
	private String lastMessage; // 채팅방의 마지막(가장 최근) 메세지
	private String sendTime; // 마지막 메세지를 보낸 시간 
	private int targetNo; // 채팅방의 대상자 회원 번호
	private String targetNickname; // 채팅방의 대상자 닉네임
	private String targetProfile; // 대상자의 프로필 이미지 경로
	private int notReadCount; // 채팅방의 사용자가 읽지 않은 메세지 개수
}
