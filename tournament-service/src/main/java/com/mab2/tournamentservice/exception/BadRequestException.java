package com.mab2.tournamentservice.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception {

	private static final long serialVersionUID = 1L;
	
	final List<String> errors;
	
	public List<String> getErrors() {
		return errors;
	}

	public BadRequestException(List<String> errors) {
		super();
		this.errors = errors;
	}
	
	public BadRequestException(String error) {
		super();
		this.errors = Arrays.asList(error);
	}

}
