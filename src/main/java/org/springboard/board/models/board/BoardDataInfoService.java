package org.springboard.board.models.board;

import lombok.RequiredArgsConstructor;
import org.springboard.board.entities.BoardData;
import org.springboard.board.models.board.config.BoardConfigInfoService;
import org.springboard.board.repositories.BoardDataRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardDataInfoService {
	private final BoardDataRepository boardDataRepository;
	private final BoardConfigInfoService configInfoService;
	
	public BoardData get(Long id) {
		BoardData boardData = boardDataRepository.findById(id).orElseThrow(BoardDataNotExistException::new);
		
		// 게시판 설정 조회 + 접근 권한체크
		configInfoService.get(boardData.getBoard().getBId(), "view");

		return boardData;
	}
}
