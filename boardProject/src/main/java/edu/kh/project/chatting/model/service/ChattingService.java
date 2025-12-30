package edu.kh.project.chatting.model.service;

import java.util.List;

import edu.kh.project.chatting.model.dto.ChattingRoom;

public interface ChattingService {

	// 채팅방 목록 조회
	List<ChattingRoom> selectRoomList(int memberNo);

}
