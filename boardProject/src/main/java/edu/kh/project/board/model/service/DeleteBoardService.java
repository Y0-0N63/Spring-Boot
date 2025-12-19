package edu.kh.project.board.model.service;

import edu.kh.project.board.model.dto.Board;

public interface DeleteBoardService {

	
	/**
	 * 게시글 삭제 서비스
	 * @param inputBoard
	 * @return
	 */
	int deleteBoard(Board inputBoard);

}