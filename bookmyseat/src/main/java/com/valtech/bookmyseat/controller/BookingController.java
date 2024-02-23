package com.valtech.bookmyseat.controller;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valtech.bookmyseat.entity.BookingMapping;
import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.exception.DataBaseAccessException;
import com.valtech.bookmyseat.model.BookingDTO;
import com.valtech.bookmyseat.model.BookingModel;
import com.valtech.bookmyseat.model.SeatBookingResponse;
import com.valtech.bookmyseat.service.BookingService;
import com.valtech.bookmyseat.service.SeatService;
import com.valtech.bookmyseat.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/bookmyseat/user")
public class BookingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private SeatService seatService;

	@PostMapping("/projectPrefrenced/floor/seat")
	public ResponseEntity<SeatBookingResponse> getSeatsandPreference(@RequestBody BookingModel bookingModel,
			@AuthenticationPrincipal UserDetails userDetails) {
		SeatBookingResponse seatBookingResponse = new SeatBookingResponse();
		LOGGER.debug("Floor Selected is: {}", bookingModel.getFloorId());
		seatBookingResponse.setSeats(seatService.getAvailableSeatsByFloorOnDate(bookingModel));
		User user = userService.findUserByEmail(userDetails.getUsername());
		seatBookingResponse.setPreferredSeats(
				bookingService.getUserPreferredSeats(bookingModel.getFloorId(), user.getProject().getProjectId()));
		LOGGER.info("Sending User Preferred seats and Seats by floor !");

		return ResponseEntity.status(HttpStatus.OK).body(seatBookingResponse);
	}

	@DeleteMapping("/cancelMyBooking/{userId}")
	public ResponseEntity<String> cancelMyBooking(@PathVariable("userId") int userId) {
		LOGGER.info("Handling request for deleting user booking");
		userService.cancelMyBooking(userId);

		return ResponseEntity.status(HttpStatus.OK).body("Booking cancled successfully");
	}

	@GetMapping("/userProfile")
	public ResponseEntity<User> userProfile(@AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.findUserByEmail(userDetails.getUsername());

		return ResponseEntity.ok(user);
	}

	@PostMapping("/bookseat")
	public ResponseEntity<String> seatBooking(@RequestBody BookingModel booking,
			@AuthenticationPrincipal UserDetails userDetails) throws DataBaseAccessException, SQLException {
		LOGGER.info("handling the request for seat booking");
		User user = userService.findUserByEmail(userDetails.getUsername());
		int bookingId = bookingService.createBooking(booking, user);
		bookingService.createBookingMapping(booking, bookingId);
		LOGGER.info("Booking created successfully");

		return ResponseEntity.status(HttpStatus.OK).body("Seat Booked Successfully");
	}

	@GetMapping("/getAllBookings")
	public List<BookingDTO> getAllBookings() {
		LOGGER.info("handling the request for get all bookings...");

		return bookingService.getAllBookings();
	}

	@GetMapping("/attendance")
	public List<BookingMapping> getAllBookingDetails() {
		LOGGER.info("Sending User attendance details !");

		return bookingService.getAllBookingDetails();
	}

	@PutMapping("/attendanceApproval/{userId}")
	public ResponseEntity<String> markUserAttendance(@PathVariable int userId) {
		bookingService.approveUserAttendance(userId);
		LOGGER.info("Sending User attendance approval!");

		return ResponseEntity.ok("User's attendance updated Succesfully !");
	}
}