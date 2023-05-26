package org.springboard.board.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

@Entity @Data
@IdClass(BoardViewId.class)
public class BoardView {
	@Id
	private Long id;	// 게시글 번호

	@Id
	@Column(length=40)
	private String uid;	// IP + UserAgent + 회원번호
}
