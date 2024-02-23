package com.valtech.bookmyseat.dao;

import java.time.LocalDate;
import java.util.List;

import com.valtech.bookmyseat.entity.Booking;
import com.valtech.bookmyseat.entity.BookingMapping;
import com.valtech.bookmyseat.entity.Seat;
import com.valtech.bookmyseat.entity.Shift;
import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.exception.DataBaseAccessException;
import com.valtech.bookmyseat.model.BookingDTO;
import com.valtech.bookmyseat.model.BookingModel;

public interface BookingDAO {

	/**
	 * Gives list of booking based on project, so that employee can book seat next
	 * to his co-worker.
	 *
	 * @param floorId   floor id which user selected to book seat
	 * @param projectId project id under which user is working
	 * @return list of bookings
	 */
	List<Booking> userPreferredSeats(int floorId, int projectId);

	/**
	 * Creates a new booking with the provided details.
	 *
	 * @param booking The booking model containing booking details such as
	 *                additional desktop, end date, lunch, parking, etc.
	 * @param user    The user for whom the booking is being made.
	 * @param seat    The seat for which the booking is being made.
	 * @param shift   The shift during which the booking will take place.
	 * @return True if the booking was successfully created, false otherwise.
	 * @throws DataBaseAccessException If any error occurs during the booking
	 *                                 creation process.
	 */
	int createBooking(BookingModel booking, User user, Seat seat, Shift shift) throws DataBaseAccessException;

	/**
	 * Retrieves a list of all bookings from the database.
	 *
	 * @return A list of BookingDTO objects representing all bookings.
	 * @throws DataBaseAccessException If an error occurs while accessing the
	 *                                 database.
	 */
	List<BookingDTO> getAllBookings() throws DataBaseAccessException;

	/**
	 * Retrieves all booking details for a given user.
	 *
	 * @param userId The ID of the user for whom booking details are to be
	 *               retrieved.
	 * @return A list of all booking details associated with the given user.
	 */
	List<BookingMapping> getAllBookingDetails();

	/**
	 * Approves the attendance for a specific user.
	 *
	 * @param userId The ID of the user whose attendance is to be approved.
	 */
	void approvalAttendance(int userId);

	boolean hasAlreadyBookedForDate(int userId, LocalDate startDate, LocalDate endDate);

	void createBookingMapping(BookingModel booking, int bookingId);

	List<Seat> getAllBookedSeat();
}