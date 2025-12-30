package edu.kh.project.chatting.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.chatting.model.dto.ChattingRoom;
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

}
