package com.mab2.playerservice.exception;

public class ExistingEntityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExistingEntityException(String message) {
		super(message+" already exists");
	}

}
