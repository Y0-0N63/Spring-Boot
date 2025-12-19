package edu.kh.project.board.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.board.model.dto.Board;

@Mapper
public interface DeleteBoardMapper {

	int deleteBoard(Board inputBoard);

}