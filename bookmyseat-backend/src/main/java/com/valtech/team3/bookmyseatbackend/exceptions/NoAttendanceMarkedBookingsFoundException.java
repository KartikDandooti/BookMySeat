package com.valtech.team3.bookmyseatbackend.exceptions;

public class NoAttendanceMarkedBookingsFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoAttendanceMarkedBookingsFoundException(String message) {
		super(message);
	}
}