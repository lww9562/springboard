package org.springboard.board.models.board;

import lombok.RequiredArgsConstructor;
import org.springboard.board.entities.BoardData;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestPasswordCheckService {
	private final BoardDataInfoService infoService;
	private final PasswordEncoder passwordEncoder;

	public void check(Long id, String password) {
		check(id, password, "board");
		// 값을 입력하지 않을 경우, 기본적으로 게시글
	}

	public void check(Long id, String password, String mode) {
		if(id == null) {
			throw new BoardValidationException("BadRequest");
		}
		mode = mode == null || mode.isBlank() ? "board" : mode;

		if(mode.equals("board")) {
			//게시글 비밀번호 확인
			BoardData boardData = infoService.get(id, "update");
			String guestPw = boardData.getGuestPw();

			boolean matched = passwordEncoder.matches(password, guestPw);
			if(!matched) {
				throw new GuestPasswordIncorrectException();
			}

		} else if (mode.equals("comment")){
			// 댓글 비밀번호 확인

		}
	}
}
