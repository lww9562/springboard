package org.springboard.board.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity @Data
public class Configs {
	@Id
	private String code;

	@Lob
	private String value;
}