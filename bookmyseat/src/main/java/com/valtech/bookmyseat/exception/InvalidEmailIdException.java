package com.valtech.bookmyseat.exception;

public class InvalidEmailIdException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidEmailIdException(String message) {
		super(message);
	}
}
