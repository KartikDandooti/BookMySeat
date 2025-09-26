package com.valtech.team3.bookmyseatbackend.exceptions;

public class DuplicateEmployeeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateEmployeeException(String message) {
		super(message);
	}
}