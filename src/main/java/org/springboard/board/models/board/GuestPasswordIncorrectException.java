package org.springboard.board.models.board;

import org.springboard.board.commons.CommonException;
import org.springframework.http.HttpStatus;

public class GuestPasswordIncorrectException extends CommonException {
	public GuestPasswordIncorrectException() {
		super(bundleValidation.getString("GuestPw.incorrect"), HttpStatus.BAD_REQUEST);
	}
}
