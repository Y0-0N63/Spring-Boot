package edu.kh.project.board.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.mapper.DeleteBoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class DeleteBoardServiceImpl implements DeleteBoardService {
	
	private final DeleteBoardMapper mapper;
	
	/**
	 * 게시글 삭제 서비스
	 */
	@Override
	public int deleteBoard(Board inputBoard) {
		return mapper.deleteBoard(inputBoard);
	}
}