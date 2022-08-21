package com.pbs.schedulerjobs.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	private final static String ERROR_PATH = "error";

	@ExceptionHandler(Exception.class)
	public ModelAndView handleError(HttpServletRequest req, Exception ex) {
		log.error("Request: " + req.getRequestURL() + " raised " + ex.getMessage(), ex);

		ModelAndView mav = new ModelAndView();
		mav.addObject("message", ex.getMessage());
		mav.addObject("ex", ex);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName(ERROR_PATH);
		return mav;
	}
}
