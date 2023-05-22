package org.springboard.board.controllers.admins;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class BoardForm {
	private String mode;	// mode값에 따라 수정/등록 판단 → update : 수정, 없으면 추가

	/** 일반 설정 S */
	@NotBlank
	private String bId;								// 게시판 ID
	@NotBlank
	private String bName;							// 게시판명
	private boolean use;							// 사용여부
	private int rowsOfPage = 20;					// 1페이지 당 게시글 수
	private boolean showViewList;					// 게시글 하단 목록 노출
	/** 일반 설정 E */


	/** 분류 설정 S */
	private String category;						// 게시판 분류
	/** 분류 설정 E */


	/** 접근 권한 설정 S */
	private String listAccessRole = "ALL";			// 목록 접근 권한
	private String viewAccessRole = "ALL";			// 글보기 접근 권한
	private String writeAccessRole = "ALL";			// 글쓰기 접근 권한
	private String replyccessRole = "ALL";			// 답글 접근 권한
	private String commentAccessRole = "ALL";		// 댓글 접근 권한
	/** 접근 권한 설정 E */


	/** 기능 설정 S */
	private boolean useEditor;						// 에디터 사용 여부
	private boolean useAttachFile;					// 파일 첨부 사용 여부
	private boolean useAttachImage;					// 이미지 첨부 사용 여부
	private String locationAfterWriting = "view";	// 글 작성 후 이동
	private boolean useReply;						// 답글 사용 여부
	private boolean useComment;						// 댓글 사용 여부
	private String skin = "default";				// 게시판 스킨
	/** 기능 설정 E */
}
