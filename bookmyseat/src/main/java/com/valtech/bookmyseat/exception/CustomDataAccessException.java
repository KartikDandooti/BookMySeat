package com.valtech.bookmyseat.exception;

public class CustomDataAccessException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CustomDataAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}
