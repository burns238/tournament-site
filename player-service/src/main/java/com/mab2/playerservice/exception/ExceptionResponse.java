package com.mab2.playerservice.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExceptionResponse {

	private Date timestamp;
	
	private String details;
	
	private List<String> errors;
	
	@Override
	public String toString() {
		return "ExceptionResponse [timestamp=" + timestamp + ", details=" + details + ", errors=" + errors + "]";
	}

	public ExceptionResponse(Date timestamp, String details, List<String> errors) {
		super();
		this.timestamp = timestamp;
		this.details = details;
		this.errors = errors;
	}
	
	public ExceptionResponse(Date timestamp, String details, String error) {
		super();
		this.timestamp = timestamp;
		this.details = details;
		List<String> errors = new ArrayList<>();
		errors.add(error);
		this.errors = errors;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getDetails() {
		return details;
	}

	public void setMessage(String details) {
		this.details = details;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
}
