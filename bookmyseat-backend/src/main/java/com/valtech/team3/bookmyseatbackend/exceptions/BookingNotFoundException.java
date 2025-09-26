package com.valtech.team3.bookmyseatbackend.exceptions;

public class BookingNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BookingNotFoundException(String message) {
		super(message);
	}
}