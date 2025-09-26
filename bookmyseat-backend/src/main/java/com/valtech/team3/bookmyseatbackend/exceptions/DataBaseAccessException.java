package com.valtech.team3.bookmyseatbackend.exceptions;

public class DataBaseAccessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataBaseAccessException(String message) {
		super(message);
	}
}