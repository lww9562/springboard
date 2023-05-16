package org.springboard.board.entities;

import jakarta.persistence.Column;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public abstract class BaseEntity {
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;


	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime modifiedAt;
}
