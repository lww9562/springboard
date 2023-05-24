package org.springboard.board.controllers.boards;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springboard.board.commons.CommonException;
import org.springboard.board.entities.Board;
import org.springboard.board.models.board.config.BoardConfigInfoService;
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
	public String write(@PathVariable String bId, Model model) {
		commonProcess(bId, "write", model);

		return "board/write";
	}

	/**
	 * 게시글 수정
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}/update")
	public String update(@PathVariable Long id, Model model) {
		commonProcess(null, "update", model);

		return "board/update";
	}

	@PostMapping("/save")
	public String save(@Valid BoardForm boardForm, Errors errors, Model model) {
		Long id = boardForm.getId();
		commonProcess(boardForm.getBId(), id == null ? "update" : "write", model);

		return null;
	}

	/**
	 * 게시글 조회
	 * @param id
	 * @return
	 */
	@GetMapping("/view/{id}")
	public String view(@PathVariable Long id, Model model) {
		commonProcess(null, "view", model);

		return "board/view";
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
		Board board = configInfoService.get(bId, action);

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

	@ExceptionHandler(CommonException.class)
	public String errorHandler(CommonException e, Model model, HttpServletResponse response) {
		e.printStackTrace();

		String message = e.getMessage();
		HttpStatus status = e.getStatus();
		response.setStatus(status.value());

		String script = String.format("alert('%s'); history.back();", message);
		model.addAttribute("script", script);

		return "commons/execute_script";
	}
}
