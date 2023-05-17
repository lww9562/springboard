package org.springboard.board.commons.validators;

import java.util.regex.Pattern;

public interface MobileValidator {
	default boolean mobileNumCheck(String mobile){

		mobile = mobile.replaceAll("\\D", "");
		System.out.println(mobile);

		Pattern pattern = Pattern.compile("^01[016]\\d{3,4}\\d{4}$");

		return pattern.matcher(mobile).find();
	}
}
