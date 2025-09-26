package com.valtech.team3.bookmyseatbackend.exceptions;

public class FailedToDeleteShiftsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FailedToDeleteShiftsException(String message) {
		super(message);
	}
}