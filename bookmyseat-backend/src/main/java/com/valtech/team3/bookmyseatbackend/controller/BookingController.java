package com.valtech.team3.bookmyseatbackend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valtech.team3.bookmyseatbackend.entities.BookingMapping;
import com.valtech.team3.bookmyseatbackend.entities.User;
import com.valtech.team3.bookmyseatbackend.models.BookingMappingModel;
import com.valtech.team3.bookmyseatbackend.models.BookingModel;
import com.valtech.team3.bookmyseatbackend.models.SeatBookingRequest;
import com.valtech.team3.bookmyseatbackend.models.SeatBookingResponse;
import com.valtech.team3.bookmyseatbackend.service.BookingMappingService;
import com.valtech.team3.bookmyseatbackend.service.BookingService;
import com.valtech.team3.bookmyseatbackend.service.FloorService;
import com.valtech.team3.bookmyseatbackend.service.SeatService;
import com.valtech.team3.bookmyseatbackend.service.UserService;
import com.valtech.team3.bookmyseatbackend.service.UserShiftsService;

@RestController
@RequestMapping("/bookmyseat")
@CrossOrigin(origins = "*")
public class BookingController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);
	private static final String SUCCESS_MESSAGE = "Booking created Successfully !";

	@Autowired
	private BookingService bookingService;
	@Autowired
	private FloorService floorService;
	@Autowired
	private SeatService seatService;
	@Autowired
	private UserService userService;
	@Autowired
	private BookingMappingService bookingMappingService;
	@Autowired
	private UserShiftsService userShiftService;

	@GetMapping("/user/seatbooking")
	public ResponseEntity<SeatBookingRequest> getShiftsandFloors(@AuthenticationPrincipal UserDetails userDetails) {
		LOGGER.info("Redirecting User to create booking");
		SeatBookingRequest seatBookingRequest = new SeatBookingRequest();
		LOGGER.info("Retrieving All Shifts From controller !");
		User user = userService.getUserByEmail(userDetails.getUsername());
		seatBookingRequest.setShiftDetails(userShiftService.getAllShiftDetailsByUserId(user.getId()));
		LOGGER.info("Retrieving All Floors From controller !");
		seatBookingRequest.setFloors(floorService.getAllFloors());

		return ResponseEntity.status(HttpStatus.OK).body(seatBookingRequest);
	}

	@PostMapping("/user/seatbooking")
	public ResponseEntity<String> seatBooking(@RequestBody BookingModel booking,
			@AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.getUserByEmail(userDetails.getUsername());
		LOGGER.info("Fetching user details by email ");
		int bookingId = bookingService.createBooking(booking, user);
		bookingMappingService.createBookingMapping(booking, bookingId);
		LOGGER.info("Booking created successfully");

		return ResponseEntity.status(HttpStatus.OK).body(SUCCESS_MESSAGE);
	}

	@PostMapping("/user/floors")
	public ResponseEntity<SeatBookingResponse> getSeatsandPreference(@RequestBody BookingModel bookingModel,
			@AuthenticationPrincipal UserDetails userDetails) {
		SeatBookingResponse seatBookingResponse = new SeatBookingResponse();
		LOGGER.info("Floor Selected is: {}", bookingModel.getFloorId());
		seatBookingResponse.setAvailableSeats((seatService.getAvailableSeatsByFloorOnDate(bookingModel)));
		seatBookingResponse.setBookedSeats(bookingService.getBookingsforFloorBetweenDates(bookingModel));
		User user = userService.getUserByEmail(userDetails.getUsername());
		seatBookingResponse.setPreferredSeats(bookingService.getUserPreferredSeats(bookingModel.getFloorId(),
				user.getProject().getId(), bookingModel.getStartDate()));
		LOGGER.info("Sending User Preferred seats and Seats by floor !");

		return ResponseEntity.status(HttpStatus.OK).body(seatBookingResponse);
	}

	@GetMapping("/user/edit/{bookingId}")
	public ResponseEntity<Map<String, Object>> getBookingDetails(@PathVariable("bookingId") int bookingId) {
		LOGGER.info("Fetching booking details for booking ID: {}", bookingId);
		List<BookingMapping> bookingMappings = bookingMappingService.getSubBookingsByBookingId(bookingId);
		LOGGER.debug("Found {} booking mappings for booking ID: {}", bookingMappings.size(), bookingId);
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("bookingMappings", bookingMappings);
		LOGGER.info("Returning booking details for booking ID: {}", bookingId);

		return ResponseEntity.status(HttpStatus.OK).body(responseBody);
	}

	@PutMapping("/user/edit/{bookingId}/{bookingMappingId}")
	public ResponseEntity<String> editBookingAndSubBookings(@PathVariable("bookingMappingId") int bookingMappingId,
			@RequestBody BookingMappingModel updatedBookingMapping) {
		LOGGER.info("Redirecting to edit page");
		bookingMappingService.editBookingAndSubBookings(updatedBookingMapping, bookingMappingId);
		LOGGER.info("Booking updated successfully for  bookingMappingId: {}", bookingMappingId);

		return ResponseEntity.status(HttpStatus.OK).body("Booking updated successfully");
	}

	@PutMapping("/user/cancel/{bookingId}")
	public ResponseEntity<String> cancelBooking(@PathVariable int bookingId) {
		bookingService.cancelBooking(bookingId);
		LOGGER.info("Booking cancelled successfully !");

		return ResponseEntity.status(HttpStatus.OK)
				.body("Booking with ID " + bookingId + " The Booking has been Cancelled !");
	}
}