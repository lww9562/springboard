package org.springboard.board.tests.models.board;

import org.springboard.board.commons.MemberUtil;
import org.springboard.board.commons.validators.LengthValidator;
import org.springboard.board.commons.validators.RequiredValidator;
import org.springboard.board.commons.validators.Validator;
import org.springboard.board.controllers.boards.BoardForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BoardValidator implements Validator<BoardForm>, RequiredValidator, LengthValidator {
	@Autowired
	private MemberUtil memberUtil;
	
	@Override
	public void check(BoardForm boardForm) {
		requiredCheck(boardForm.getBId(), new BoardValidationException("BadRequest"));
		requiredCheck(boardForm.getGid(), new BoardValidationException("BadRequest"));
		requiredCheck(boardForm.getPoster(), new BoardValidationException("NotBlank.boardForm.poster"));
		requiredCheck(boardForm.getSubject(), new BoardValidationException("NotBlank.boardForm.subject"));
		requiredCheck(boardForm.getContent(), new BoardValidationException("NotBlank.boardForm.content"));

		// 비회원 - 비회원 비밀번호 체크
		if (!memberUtil.isLogin()) {
			requiredCheck(boardForm.getGuestPw(), new BoardValidationException("NotBlank.boardForm.guestPw"));

			strLengthCheck(boardForm.getGuestPw(), 6, new BoardValidationException("Size.boardForm.gusetPw"));
		}


	}
}
