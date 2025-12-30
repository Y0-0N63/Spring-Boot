package edu.kh.project.chatting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
	private int messageNo; // 메세지 번호
	private String messageContent; // 메세지 내용
	private String readFl; // 읽음 여부
	private String sendDate; // 메세지를 보낸 시간
	private int senderNo; // 보낸 회원
	private int chattingRoomNo; // 채팅방 번호
}
