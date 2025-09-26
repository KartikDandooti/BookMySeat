package com.valtech.team3.bookmyseatbackend.exceptions;

public class FailedToAddShiftException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FailedToAddShiftException(String message) {
		super(message);
	}
}