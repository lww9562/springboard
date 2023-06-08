package org.springboard.board.models.board;

import org.springboard.board.commons.CommonException;
import org.springframework.http.HttpStatus;

/**
 * 비회원 비밀번호 미검증 시 발생하는 예외
 */
public class GuestPasswordNotCheckedException extends CommonException {
	public GuestPasswordNotCheckedException() {
		super(bundleValidation.getString("GuestPw.notChecked"), HttpStatus.UNAUTHORIZED);
	}
}
