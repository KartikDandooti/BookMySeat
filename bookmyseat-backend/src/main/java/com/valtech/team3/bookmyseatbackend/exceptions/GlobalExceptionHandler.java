package com.valtech.team3.bookmyseatbackend.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	private static final String USER_NOT_FOUND = "User not found";
	private static final String INVALID_CERDENTIALS = "Invalid Credentials";

	@ExceptionHandler(DataBaseAccessException.class)
	private ResponseEntity<String> handleDataAccessException(DataBaseAccessException e) {
		LOGGER.error("An error occurred while communicating with the database:{}", e.getMessage());
		LOGGER.debug("An error occurred while communicating with the database", e);

		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(BadCredentialsException.class)
	private ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {
		LOGGER.error("Invalid Credentials:{}", e.getMessage());
		LOGGER.debug(INVALID_CERDENTIALS, e);

		return new ResponseEntity<>(INVALID_CERDENTIALS, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(DisabledException.class)
	private ResponseEntity<String> handleDisabledException(DisabledException e) {
		LOGGER.error("Account is InActive:{}", e.getMessage());
		LOGGER.debug("Account is InActive", e);

		return new ResponseEntity<>("Account is InActive", HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AuthenticationException.class)
	private ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
		LOGGER.error("Invalid Credentials:{}", e.getMessage());
		LOGGER.debug(INVALID_CERDENTIALS, e);

		return new ResponseEntity<>(INVALID_CERDENTIALS, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(DuplicateEmployeeException.class)
	public ResponseEntity<String> handlingDuplicateEmployeeException(DuplicateEmployeeException e) {
		LOGGER.error("Sending Response if Employee already Exists :{}!", e.getMessage());
		LOGGER.debug("Sending Response if Employee already Exists !", e);

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getLocalizedMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handlingUserNotFoundException(UserNotFoundException e) {
		LOGGER.debug(USER_NOT_FOUND, e);
		LOGGER.error(USER_NOT_FOUND, e.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(EmailSendingException.class)
	public ResponseEntity<String> handlingEmailSendingException(EmailSendingException e) {
		LOGGER.debug(USER_NOT_FOUND, e);
		LOGGER.error(USER_NOT_FOUND, e.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

	@ExceptionHandler(BookingNotFoundException.class)
	public ResponseEntity<String> handlingBookingNotFoundException(BookingNotFoundException e) {
		LOGGER.debug("No bookings found for current date!", e);
		LOGGER.error("No bookings found for current date!:{}", e.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

	@ExceptionHandler(NoAttendanceMarkedBookingsFoundException.class)
	public ResponseEntity<String> handlingNoAttendanceMarkedBookingsFoundException(NoAttendanceMarkedBookingsFoundException e) {
		LOGGER.debug("No bookings found for current date!", e);
		LOGGER.error("No bookings found for current date!:{}", e.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

	@ExceptionHandler(AlreadyBookedForDateException.class)
	public ResponseEntity<String> handlingAlreadyBookedForDateException(AlreadyBookedForDateException e) {
		LOGGER.debug("Seat is Already booked for the date range ", e);
		LOGGER.error("Seat is Already booked for the date range:{}", e.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

	@ExceptionHandler(AttendanceMarkedException.class)
	public ResponseEntity<String> handlingAttendanceMarkedException(AttendanceMarkedException e) {
		LOGGER.debug("Attendance is Already marked seat can't be", e);
		LOGGER.error("Attendance is Already marked seat can't be:{}", e.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

	@ExceptionHandler(NoParkingAvailableException.class)
	public ResponseEntity<String> handlingNoParkingAvailableException(NoParkingAvailableException e) {
		LOGGER.debug("Parking is full. Please use public transport.", e);
		LOGGER.error("Parking is full. Please use public transport:{}", e.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

	@ExceptionHandler(ShiftsNotFoundException.class)
	public ResponseEntity<String> handlingShiftsNotFoundException(ShiftsNotFoundException e) {
		LOGGER.debug("No shifts found for user.", e);
		LOGGER.error("No shifts found for user:{}", e.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

	@ExceptionHandler(FailedToAddShiftException.class)
	public ResponseEntity<String> handlingFailedToAddShiftException(FailedToAddShiftException e) {
		LOGGER.debug("Failed to add shift for user.", e);
		LOGGER.error("Failed to add shift for user:{}", e.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

	@ExceptionHandler(FailedToDeleteShiftsException.class)
	public ResponseEntity<String> handlingFailedToDeleteShiftsException(FailedToDeleteShiftsException e) {
		LOGGER.debug("Failed to delete shifts for user.", e);
		LOGGER.error("Failed to delete shifts for user:{}", e.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}
}