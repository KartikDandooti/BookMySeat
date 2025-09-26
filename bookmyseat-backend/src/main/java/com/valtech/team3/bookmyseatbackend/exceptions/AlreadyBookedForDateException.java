package com.valtech.team3.bookmyseatbackend.exceptions;

public class AlreadyBookedForDateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AlreadyBookedForDateException(String message) {
		super(message);
	}
}