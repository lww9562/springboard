package org.springboard.board.models.board.config;

import org.springboard.board.commons.CommonException;
import org.springframework.http.HttpStatus;

public class DuplicateBoardConfigException extends CommonException {
	public DuplicateBoardConfigException() {
		super("이미 등록된 게시판 입니다.", HttpStatus.BAD_REQUEST);
	}
}
