package com.valtech.bookmyseat.exception;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
	private int status;
	private String message;
	private LocalDate timeStamp;

	@Override
	public String toString() {
		return "ExceptionResponse [status=" + status + ", message=" + message + ", timeStamp=" + timeStamp + "]";
	}
}