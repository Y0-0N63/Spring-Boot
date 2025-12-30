package edu.kh.project.chatting.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.chatting.model.dto.ChattingRoom;
import edu.kh.project.chatting.model.dto.Message;
import edu.kh.project.chatting.model.mapper.ChattingMapper;
import edu.kh.project.member.model.dto.Member;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ChattingServiceImpl implements ChattingService {
	
	@Autowired
	private ChattingMapper mapper;
	
	// 채팅방 목록 조회
	@Override
	public List<ChattingRoom> selectRoomList(int memberNo) {
		
		return mapper.selectRoomList(memberNo);
	}

	// 채팅 상대 검색 서비스
	@Override
	public List<Member> selectTarget(Map<String, Object> map) {
		
		return mapper.selectTarget(map);
	}

	// 채팅방 번호 체크 서비스
	@Override
	public int checkChattingRoomNo(Map<String, Integer> map) {
		
		return mapper.checkChattingRoomNo(map);
	}
	
	// 새로운 채팅방 생성 서비스
	@Override
	public int createChattingRoom(Map<String, Integer> map) {
		
		int result = mapper.createChattingRoom(map);
		
		if(result > 0) {
			// keyProperty="chattingRoomNo"를 통해 생성된 채팅방의 시퀀스 번호를 받아옴
			return map.get("chattingRoomNo");
		}
		
		return 0;
	}

	// 채팅 메세지 조회 서비스
	@Override
	public List<Message> selectMessageList(Map<String, Object> paramMap) {
		
		List<Message> messageList = mapper.selectMessageList(paramMap.get("chattingRoomNo"));
		
		// 해당 채팅방에서 기존에 나누었던 메세지의 목록이 있다면 > 읽음 처리
		if(!messageList.isEmpty()) {
			// 결과값을 다루지 않기 때문에 > update된 행의 개수를 받아주지 않고 메서드를 호출하기만 해도 됨
			mapper.updateReadFlag(paramMap);
		}
		
		return messageList;
	}

	// 읽음 표시 업데이트
	@Override
	public int updateReadFlag(Map<String, Object> paramMap) {
		return mapper.updateReadFlag(paramMap);
	}

	
	@Override
	public int insertMessage(Message msg) {
		return mapper.insertMessage(msg);
	}

}
