package org.springboard.board.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springboard.board.commons.CommonException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@ControllerAdvice("org.springboard.board.controllers")
public class CommonController {

	@ExceptionHandler(Exception.class)
	public String errorHandler(Exception e, Model model, HttpServletRequest request, HttpServletResponse response){
		int status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		if(e instanceof CommonException){
			CommonException commonException = (CommonException) e;
			status = commonException.getStatus().value();

		}
		response.setStatus(status);

		String URL = request.getRequestURI();

		model.addAttribute("status", status);
		model.addAttribute("path", URL);
		model.addAttribute("message", e.getMessage());
		model.addAttribute("exception", e);

		e.printStackTrace();

		return "error/common";
	}
}
