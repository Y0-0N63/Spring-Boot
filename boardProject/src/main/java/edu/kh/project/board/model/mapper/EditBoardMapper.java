package edu.kh.project.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;

@Mapper
public interface EditBoardMapper {

	/**
	 * 게시글 부분 작성 SQL 수행
	 * @param inputBoard
	 * @return
	 */
	int boardInsert(Board inputBoard);

	/**
	 * 게시글 이미지 모두 삽입하는 SQL 수행
	 * @param uploadList
	 * @return
	 */
	int insertUploadList(List<BoardImg> uploadList);

	/**
	 * 게시글의 제목과 내용만 수정 SQL
	 * @param inputBoard
	 * @return
	 */
	int boardUpdate(Board inputBoard);

	/**
	 * 게시글 이미지 삭제 SQL
	 * @param map
	 * @return
	 */
	int deleteImg(Map<String, Object> map);

	/**
	 * 게시글 이미지 수정 SQL
	 * @param img
	 * @return
	 */
	int updateImage(BoardImg img);

	/**
	 * 게시글 이미지 삽입 SQL
	 * @param img
	 * @return
	 */
	int insertImage(BoardImg img);
}
	