package com.valtech.team3.bookmyseatbackend.exceptions;

public class SeatAlreadyBookedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SeatAlreadyBookedException(String message) {
		super(message);
	}
}