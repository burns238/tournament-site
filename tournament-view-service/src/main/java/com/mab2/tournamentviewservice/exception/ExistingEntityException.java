package com.mab2.tournamentviewservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExistingEntityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExistingEntityException(String message) {
		super(message+" already exists");
	}

}
