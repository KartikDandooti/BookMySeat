package com.valtech.bookmyseat.exception;

public class DuplicateUserIdException extends Exception {
	private static final long serialVersionUID = 1L;

	public DuplicateUserIdException(String message) {
		super(message);
	}
}