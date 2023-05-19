package org.springboard.board.controllers.admins;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ConfigForm {
	private String siteTitle;
	private String siteDescription;
	private String cssJsVersion;
	private String joinTerms;
}
