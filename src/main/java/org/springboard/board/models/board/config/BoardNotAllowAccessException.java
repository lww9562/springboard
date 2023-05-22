package org.springboard.board.models.board.config;

import org.springboard.board.commons.CommonException;
import org.springframework.http.HttpStatus;

public class BoardNotAllowAccessException extends CommonException {
	public BoardNotAllowAccessException() {
		super(bundleValidation.getString("Validation.board.NotAllowAccess"), HttpStatus.UNAUTHORIZED);
	}

}
