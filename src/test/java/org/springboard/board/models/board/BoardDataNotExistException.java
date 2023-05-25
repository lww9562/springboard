package org.springboard.board.models.board;

import org.springboard.board.commons.CommonException;
import org.springframework.http.HttpStatus;

public class BoardDataNotExistException extends CommonException {

	public BoardDataNotExistException() {
		super(bundleValidation.getString("Validation.boardData.notExists"), HttpStatus.BAD_REQUEST);
	}
}
