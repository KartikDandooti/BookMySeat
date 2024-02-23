package com.valtech.bookmyseat.exception;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(DuplicateEmailException.class)
	private ResponseEntity<ExceptionResponse> handleUserAlreadyExistsException(DuplicateEmailException e) {
		LOGGER.error("Handling the DuplicateEmailException:{}", e.getMessage());
		ExceptionResponse response = new ExceptionResponse();
		response.setStatus(HttpStatus.CONFLICT.value());
		response.setMessage(e.getMessage());
		response.setTimeStamp(LocalDate.now());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(DataBaseAccessException.class)
	private ResponseEntity<ExceptionResponse> handleDatabaseException(DataBaseAccessException e) {
		LOGGER.error("Handling the DataBaseAccessException:{}", e.getMessage());
		ExceptionResponse response = new ExceptionResponse();
		response.setStatus(HttpStatus.CONFLICT.value());
		response.setMessage(e.getMessage());
		response.setTimeStamp(LocalDate.now());

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	private ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException e) {
		LOGGER.error("Handling the IllegalArgumentException:{}", e.getMessage());
		ExceptionResponse response = new ExceptionResponse();
		response.setStatus(HttpStatus.CONFLICT.value());
		response.setMessage(e.getMessage());
		response.setTimeStamp(LocalDate.now());

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DuplicateUserIdException.class)
	public ResponseEntity<ExceptionResponse> handle(DuplicateUserIdException e) {
		LOGGER.error("Handling the DuplicateUserIdException: {}", e.getMessage());
		ExceptionResponse response = new ExceptionResponse();
		response.setMessage(e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(BadCredentialsException.class)
	private ResponseEntity<ExceptionResponse> handleInvalidCredentials(BadCredentialsException e) {
		LOGGER.error("Handling the BadCredentialsException:{}", e.getMessage());
		ExceptionResponse response = new ExceptionResponse();
		response.setStatus(HttpStatus.CONFLICT.value());
		response.setMessage(e.getMessage());
		response.setTimeStamp(LocalDate.now());

		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(InvalidEmailIdException.class)
	public ResponseEntity<ExceptionResponse> handle(InvalidEmailIdException e) {
		LOGGER.error("Handling the InvalidEmailIdException: {}", e.getMessage());
		ExceptionResponse response = new ExceptionResponse();
		response.setMessage(e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EmailException.class)
	public ResponseEntity<ExceptionResponse> handleEmailException(EmailException e) {
		LOGGER.error("Handling Email Exception: {}", e.getMessage());
		ExceptionResponse response = new ExceptionResponse();
		response.setMessage(e.getMessage());
    
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleEmailException(UsernameNotFoundException e) {
		LOGGER.error("Handling UsernameNotFoundException: {}", e.getMessage());
		ExceptionResponse response = new ExceptionResponse();
		response.setMessage(e.getMessage());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
}