package com.valtech.team3.bookmyseatbackend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valtech.team3.bookmyseatbackend.entities.BookingMapping;
import com.valtech.team3.bookmyseatbackend.models.MarkAttendanceModel;
import com.valtech.team3.bookmyseatbackend.service.BookingMappingService;

@RestController
@RequestMapping("/bookmyseat")
@CrossOrigin(origins = "*")
public class ReceptionistController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReceptionistController.class);

	@Autowired
	private BookingMappingService bookingMappingService;

	@GetMapping("/markattendance")
	public ResponseEntity<List<BookingMapping>> getMarkAttendence() {
		LOGGER.info("Redirecting Receptionist to mark Attendence");
		LOGGER.debug("Employees booked for the particular date with employeeIds: {}", bookingMappingService.getAllBookingsForCurrentDate());

		return ResponseEntity.status(HttpStatus.OK).body(bookingMappingService.getAllBookingsForCurrentDate());
	}

	@PutMapping("/markattendance")
	public ResponseEntity<String> markAttendence(@RequestBody MarkAttendanceModel markAttendanceModel) {
		LOGGER.info("Marking Employee Attendence with employeeId:{} ", markAttendanceModel.getEmployeeId());
		try {
			if (!bookingMappingService.isBookingAvailableForCurrentDate(markAttendanceModel.getEmployeeId())) {
				LOGGER.error("Seat is not booked for the date");

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seat is not booked for the date.");
			}
			if (bookingMappingService.isAttendanceMarkedForCurrentDate(markAttendanceModel.getEmployeeId())) {
				LOGGER.error(" Attendence is already marked");

				return ResponseEntity.status(HttpStatus.CONFLICT).body("Attendance is already marked.");
			}
			bookingMappingService.updateUserAttendence(markAttendanceModel.getEmployeeId());
			LOGGER.info("Marked attendence");
			LOGGER.debug("Attendence Marked for Employee with employee Id: {}", markAttendanceModel.getEmployeeId());

			return ResponseEntity.ok("Attendance Marked");
		} catch (Exception e) {
			LOGGER.error("Failed to mark Attendence", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to mark attendance.");
		}
	}

	@GetMapping("/attendancemarked")
	public ResponseEntity<List<BookingMapping>> getAllAttendanceMarkedBookings() {
		LOGGER.info("Redirecting Receptionist to see Attendance Marked Bookings");
		LOGGER.debug("Employees attendance marked for the current date:{}", bookingMappingService.getAllBookingsAttendanceMarkedForCurrentDate());

		return ResponseEntity.status(HttpStatus.OK).body(bookingMappingService.getAllBookingsAttendanceMarkedForCurrentDate());
	}
}