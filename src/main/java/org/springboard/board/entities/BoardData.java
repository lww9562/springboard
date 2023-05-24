package org.springboard.board.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity @Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class BoardData extends BaseEntity {
	@Id @GeneratedValue
	private Long id;			// 게시글 번호

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="bId")
	private Board board;			// 게시판 ID

	private String gid = UUID.randomUUID().toString();			// 파일 업로드 때 사용

	private String poster;		// 작성자
	private String guestPw;		// 비회원 비밀번호

	private String category;	// 게시판 분류

	private String subject;		// 제목
	private String content;		// 내용

	private int hit;			// 조회수
	private String ua;			// User-Agent : 브라우저 정보
	private String ip;			// 작성자 IP
	private int commentCnt;		// 댓글 수

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="userNo")
	private Member member;		// 작성 회원
}
