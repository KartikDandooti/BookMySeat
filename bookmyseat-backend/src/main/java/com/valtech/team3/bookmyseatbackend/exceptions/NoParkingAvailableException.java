package com.valtech.team3.bookmyseatbackend.exceptions;

public class NoParkingAvailableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoParkingAvailableException(String message) {
		super(message);
	}
}