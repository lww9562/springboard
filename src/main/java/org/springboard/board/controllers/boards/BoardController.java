package org.springboard.board.controllers.boards;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springboard.board.commons.CommonException;
import org.springboard.board.commons.MemberUtil;
import org.springboard.board.entities.Board;
import org.springboard.board.entities.BoardData;
import org.springboard.board.entities.Member;
import org.springboard.board.models.board.*;
import org.springboard.board.models.board.config.BoardConfigInfoService;
import org.springboard.board.models.board.config.BoardNotAllowAccessException;
import org.springboard.board.models.member.MemberInfo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	private final BoardConfigInfoService configInfoService;
	private final BoardDataInfoService infoService;
	private final BoardDataSaveService saveService;
	private final BoardFormValidator formValidator;
	private final HttpServletResponse response;
	private final MemberUtil memberUtil;
	private final UpdateHitService updateHitService;
	private final GuestPasswordCheckService passwordCheckService;
	private final HttpSession session;
	
	private Board board;	// 게시판 설정

	/**
	 * 게시글 목록
	 * @param bId
	 * @return
	 */
	@GetMapping("/list/{bId}")
	public String list(@PathVariable String bId, Model model) {
		commonProcess(bId, "list", model);

		return "board/list";
	}

	/**
	 * 게시글 작성
	 * @param bId
	 * @return
	 */
	@GetMapping("/write/{bId}")
	public String write(@PathVariable String bId, @ModelAttribute BoardForm boardForm, Model model) {
		commonProcess(bId, "write", model);
		boardForm.setBId(bId);
		if(memberUtil.isLogin()) {
			boardForm.setPoster(memberUtil.getMember().getUserNm());
		}

		return "board/write";
	}

	/**
	 * 게시글 수정
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}/update")
	public String update(@PathVariable Long id, Model model) {
		BoardData boardData = infoService.get(id, "update");
		board = boardData.getBoard();

		commonProcess(board.getBId(), "update", model);

		// 수정 권한 체크
		updateDeletePossibleCheck(boardData);

		BoardForm boardForm = new ModelMapper().map(boardData, BoardForm.class);
		if(boardData.getMember() == null) {
			//boardForm.setUserNo(boardData.getMember().getUserNo());
			board.setGuest(true);
		}
		model.addAttribute("boardForm", boardForm);

		return "board/update";
	}

	@PostMapping("/save")
	public String save(@Valid BoardForm boardForm, Errors errors, Model model) {
		Long id = boardForm.getId();
		String mode = "write";
		if(id != null) {
			mode = "update";
			BoardData boardData = infoService.get(id);
			board = boardData.getBoard();
			if(boardData.getMember() == null) {
				board.setGuest(true);
			} else {
				boardForm.setUserNo(boardData.getMember().getUserNo());
			}

			updateDeletePossibleCheck(boardData);

		}
		commonProcess(boardForm.getBId(), mode, model);

		formValidator.validate(boardForm, errors);

		if(errors.hasErrors()) {
			return "board/" + mode;
		}

		saveService.save(boardForm);
		
		//작성 후 이동 설정 - 목록, 글보기
		String location = board.getLocationAfterWriting();
		String url = "redirect:/board/";
		url += location.equals("view") ? "view/" + boardForm.getId() : "list/" + boardForm.getBId();

		return url;
	}

	/**
	 * 게시글 조회
	 * @param id
	 * @return
	 */
	@GetMapping("/view/{id}")
	public String view(@PathVariable Long id, Model model) {
		BoardData boardData = infoService.get(id);
		board = boardData.getBoard();

		commonProcess(board.getBId(), "view", model);

		model.addAttribute("boardData", boardData);
		model.addAttribute("board", board);

		updateHitService.update(id);	// 게시글 조회수 업데이트

		return "board/view";
	}

	@PostMapping("/password")
	public String password(String password) {
		String mode = (String)session.getAttribute("guestPwMode");
		Long id = (Long)session.getAttribute("guestPwId");

		// 비회원 비밀번호 검증
		passwordCheckService.check(id, password, mode);

		// 비회원 비밀번호 검증 완료 후 세션 처리
		session.setAttribute(mode + "_" + id, true);

		// 비회원 비밀번호 확인 후 이동 경로
		String url = mode == "comment" ? "/board/" + id + "/comment" : "/board/" + id + "/update";

		// 검증 완료 후 세션 비우기
		session.removeAttribute("guestPwMode");
		session.removeAttribute("guestPwId");
		
		return "redirect:" + url;
	}


	private void commonProcess(String bId, String action, Model model) {
		/**
		 * 1. bId를 이용, 게시판 설정 조회
		 * 2. action - write, update - 공통 스크립트, 공통 CSS
		 * 			 - 에디터 사용 	→ 에디터 스크립트 추가
		 * 			 - 에디터 미사용 	→ 에디터 스크립트 미추가
		 * 			 - write, list, view, ... → 권한 체크
		 * 			 - update 	- 본인이 쓴 게시글만 수정 가능하도록 구현
		 * 			 			- 회원 	: 회원번호
		 * 			 			- 비회원	: 비회원 비밀번호
		 * 			 			- 관리자	: 전체 가능
		 */

		// 1번, 2번(권한 체크) 구현
		board = configInfoService.get(bId, action);

		List<String> addCss = new ArrayList<>();
		List<String> addScript = new ArrayList<>();

		// 공통 스타일 CSS 추가
		addCss.add("board/style");
		if(board.getSkin() != null) {
			addCss.add(String.format("board/%s_style", board.getSkin()));
		}

		// 글 작성과 수정 시 필요한 자바스크립트 추가
		if(action.equals("write") || action.equals("update")) {
			if(board.isUseEditor()) {	// 에디터 사용 경우
				addScript.add("ckeditor/ckeditor");
			}
			addScript.add("board/form");
		}

		// 공통 필요 속성 추가
		model.addAttribute("board", board);		// 게시판 설정 추가
		model.addAttribute("addCss", addCss);
		model.addAttribute("addScript", addScript);

	}

	/**
	 * 수정, 삭제 권한 체크
	 * 	- 회원 : 작성한 회원
	 * 	- 비회원 : 비밀번호 검증
	 * 	- 관리자 : 검증 없이 가능
 	 * @param boardData
	 */
	public void updateDeletePossibleCheck(BoardData boardData) {
		if (memberUtil.isAdmin()) {
			return;    // 관리자는 무조건 가능
		}

		Member member = boardData.getMember();

		if (member == null) {
			// 비회원일 경우에는 비밀번호 검증이 되었는지 체크, 안되어있다면 비밀번호 확인 페이지
			/*
			 * 세션 키 - "board_게시글 번호" 가 있으면 비회원 검증 완료
			 */
			if (session.getAttribute("board_" + boardData.getId()) == null) {
				// 1. 위치 - 게시글 : board, 댓글 comment
				// 2. 게시글 번호

				session.setAttribute("guestPwMode", "board");
				session.setAttribute("guestPwId", boardData.getId());

				throw new GuestPasswordNotCheckedException();    //비밀번호 확인 페이지 노출
			}
		}
		else {
			if (memberUtil.isLogin()
					&& memberUtil.getMember().getUserNo() != boardData.getMember().getUserNo()) {
				throw new BoardNotAllowAccessException();
				// 글을 작성한 회원쪽만 가능하도록 통제
			}
		}
	}

	public void updateDeletePossibleCheck(Long id) {
		BoardData boardData = infoService.get(id, "update");
		updateDeletePossibleCheck(boardData);
	}

	@ExceptionHandler(CommonException.class)
	public String errorHandler(CommonException e, Model model) {
		e.printStackTrace();

		String message = e.getMessage();
		HttpStatus status = e.getStatus();
		response.setStatus(status.value());

		if(e instanceof GuestPasswordNotCheckedException) {	// 회원 비밀번호 검증 관련 예외인 경우
			return "board/password";
		}

		String script = String.format("alert('%s'); history.back();", message);
		model.addAttribute("script", script);

		return "commons/execute_script";
	}
}
