package org.springboard.board.tests;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springboard.board.commons.constants.Role;
import org.springboard.board.controllers.boards.BoardForm;
import org.springboard.board.entities.Board;
import org.springboard.board.models.board.BoardDataInfoService;
import org.springboard.board.models.board.BoardDataNotExistException;
import org.springboard.board.models.board.BoardDataSaveService;
import org.springboard.board.models.board.config.BoardConfigInfoService;
import org.springboard.board.models.board.config.BoardConfigSaveService;
import org.springboard.board.models.board.config.BoardNotAllowAccessException;
import org.springboard.board.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@Transactional
public class BoardViewTests {
	private Board board;
	private Long id;	// 게시글 번호

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private BoardDataSaveService saveService;
	@Autowired
	private BoardConfigSaveService configSaveService;
	@Autowired
	private BoardConfigInfoService configInfoService;
	@Autowired
	private BoardDataInfoService infoService;

	private BoardForm boardForm2;

	private String bId = "freetalk";	// 게시판 ID

	private Board getBoard() {
		Board board = configInfoService.get(bId, true);

		return board;
	}

	@BeforeEach
	void init() {
		// 게시판 설정 추가
		org.springboard.board.controllers.admins.BoardForm boardForm = new org.springboard.board.controllers.admins.BoardForm();
		boardForm.setBId(bId);
		boardForm.setBName("자유게시판");
		boardForm.setCategory("분류1");
		boardForm.setUse(true);
		configSaveService.save(boardForm);
		board = getBoard();


		// 테스트용 기본 게시글 추가
		boardForm2 = BoardForm.builder()
				.bId(board.getBId())
				.gid(UUID.randomUUID().toString())
				.poster("작성자")
				.guestPw("12345678")
				.subject("제목!")
				.content("내용!")
				.category(board.getCategories() == null ? null : board.getCategories()[0])
				.build();
		saveService.save(boardForm2);
		id = boardForm2.getId();
	}

	@Test
	@DisplayName("게시글 조회 성공 시 예외 없음")
	void getBoardDataSuccessTest() {
		assertDoesNotThrow(() -> {
			infoService.get(id);
		});
	}

	@Test
	@DisplayName("등록되지 않은 게시글이면 BoardDataNotExistException 발생")
	void getBoardDataNotExistTest() {
		assertThrows(BoardDataNotExistException.class, () -> {
			infoService.get(id + 10);
		});
	}

	@Test
	@DisplayName("게시판 사용 여부(use)가 false인 경우, 접근 불가 - BoardNotAllowAccessException 발생")
	void accessAuthCheck1Test() {
		assertThrows(BoardNotAllowAccessException.class, () -> {
			Board board = getBoard();
			board.setUse(false);
			boardRepository.saveAndFlush(board);

			infoService.get(id);
		});
	}

	@Test
	@DisplayName("회원 전용 글 보기 권한 - 비회원 접속 시 BoardWNotAllowAccessException 발생")
	@WithAnonymousUser
	void accessAuthCheck2Test(){
		assertThrows(BoardNotAllowAccessException.class, () -> {
			Board board = getBoard();
			board.setUse(true);
			board.setViewAccessRole(Role.USER);
			boardRepository.saveAndFlush(board);

			infoService.get(id);
		});
	}
}
