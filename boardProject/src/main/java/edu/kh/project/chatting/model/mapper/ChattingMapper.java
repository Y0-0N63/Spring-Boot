package edu.kh.project.chatting.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.chatting.model.dto.ChattingRoom;
import edu.kh.project.chatting.model.dto.Message;
import edu.kh.project.member.model.dto.Member;

@Mapper
public interface ChattingMapper {

	// 채팅방 목록 조회 SQL
	List<ChattingRoom> selectRoomList(int memberNo);

	// 채팅 상대 검색 SQL
	List<Member> selectTarget(Map<String, Object> map);

	// 채팅방 번호 체크 > 기존에 존재한다면 채팅방 번호를 반환함 (그렇지 않으면 0)
	int checkChattingRoomNo(Map<String, Integer> map);

	// 새로운 채팅방 생성 SQL > 생성된 채팅방의 번호를 반환함
	int createChattingRoom(Map<String, Integer> map);

	// 메세지 목록 조회 SQL
	// Integer chattingRoomNo > Object chattingRoomNo로 변경 : 채팅 목록의 프로필을 클릭했을 때 > 채팅방이 열림
	List<Message> selectMessageList(Object chattingRoomNo);

	// 채팅 메세지 읽음 처리 SQL
	int updateReadFlag(Map<String, Object> paramMap);

	// 채팅 입력 SQL
	int insertMessage(Message msg);
}
