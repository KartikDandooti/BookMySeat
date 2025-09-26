package com.valtech.team3.bookmyseatbackend.exceptions;

public class AttendanceMarkedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AttendanceMarkedException(String message) {
		super(message);
	}
}