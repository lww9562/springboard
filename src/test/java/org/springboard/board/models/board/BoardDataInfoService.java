package org.springboard.board.models.board;

import lombok.RequiredArgsConstructor;
import org.springboard.board.entities.BoardData;
import org.springboard.board.repositories.BoardDataRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardDataInfoService {
	private final BoardDataRepository boardDataRepository;

	public BoardData get(Long id) {
		BoardData boardData = boardDataRepository.findById(id).orElseThrow(BoardDataNotExistException::new);

		return boardData;
	}
}
