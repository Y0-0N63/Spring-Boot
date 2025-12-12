package edu.kh.project.board.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

public interface BoardService {

	/**
	 * 게시판 종류 조회 서비스
	 * @return
	 */
	List<Map<String, Object>> selectBoardTypeList();

}