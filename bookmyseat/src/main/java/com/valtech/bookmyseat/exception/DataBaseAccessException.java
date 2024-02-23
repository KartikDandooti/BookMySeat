package com.valtech.bookmyseat.exception;

import org.springframework.dao.DataAccessException;

public class DataBaseAccessException extends DataAccessException {
	private static final long serialVersionUID = 1L;

	public DataBaseAccessException(String msg) {
		super(msg);
	}
}
