package org.springboard.board.tests;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springboard.board.controllers.boards.BoardForm;
import org.springboard.board.controllers.members.JoinForm;
import org.springboard.board.entities.Board;
import org.springboard.board.entities.Member;
import org.springboard.board.models.board.config.BoardConfigInfoService;
import org.springboard.board.models.board.config.BoardConfigSaveService;
import org.springboard.board.models.member.MemberSaveService;
import org.springboard.board.tests.models.board.BoardDataSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("게시글 등록, 수정 테스트")
public class BoardSaveTests {
	@Autowired
	private BoardDataSaveService saveService;

	@Autowired
	private BoardConfigSaveService configSaveService;

	@Autowired
	private BoardConfigInfoService configInfoService;

	@Autowired
	private MemberSaveService memberSaveService;

	private Board board;

	private JoinForm joinForm;


	@BeforeEach
	@Transactional
	void init() {
		// 게시판 설정 추가
		org.springboard.board.controllers.admins.BoardForm boardForm = new org.springboard.board.controllers.admins.BoardForm();
		boardForm.setBId("freetalk");
		boardForm.setBName("자유게시판");
		configSaveService.save(boardForm);

		board = configInfoService.get(boardForm.getBId(), true);


		// 회원가입 추가
		joinForm = JoinForm.builder()
				.userId("user01")
				.userPw("aA!123456")
				.userPwRe("aA!123456")
				.userNm("사용자01")
				.email("user01@test.org")
				.mobile("01000000000")
				.agrees(new boolean[]{true})
				.build();

		memberSaveService.save(joinForm);
	}

	private BoardForm getGuestBoardForm() {
		return BoardForm.builder()
				.bId(board.getBId())
				.guestPw("12345678")
				.poster("비회원")
				.subject("제목!")
				.content("내용!")
				.category(board.getCategories() == null ? null : board.getCategories()[0])
				.build();
	}

	//@WithMockUser(username="user01", password="aA!123456")
	private BoardForm getMemberBoardForm() {
		return BoardForm.builder()
				.bId(board.getBId())
				.poster(joinForm.getUserNm())
				.subject("제목!")
				.content("내용!")
				.category(board.getCategories() == null ? null : board.getCategories()[0])
				.build();
	}


	@Test
	@DisplayName("게시글 등록(비회원) 성공 시 예외 없음")
	void registerGuestSuccessTest() {
		assertDoesNotThrow(() -> {
			saveService.save(getGuestBoardForm());
		});
	}
}
