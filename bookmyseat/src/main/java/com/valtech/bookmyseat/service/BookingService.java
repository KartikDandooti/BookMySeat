package com.valtech.bookmyseat.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.valtech.bookmyseat.entity.Booking;
import com.valtech.bookmyseat.entity.BookingMapping;
import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.exception.DataBaseAccessException;
import com.valtech.bookmyseat.model.BookingDTO;
import com.valtech.bookmyseat.model.BookingModel;

/**
 * This interface provides methods for managing bookings.
 */
public interface BookingService {

	/**
	 * Retrieves the list of preferred seats for a user on a specific floor and
	 * project.
	 * 
	 * @param floorId   The ID of the floor where the seats are located.
	 * @param projectId The ID of the project associated with the seats.
	 * @return A list of Booking objects representing the preferred seats.
	 */
	List<Booking> getUserPreferredSeats(int floorId, int projectId);

	/**
	 * Retrieves all booking details associated with a specific user.
	 *
	 * @param userId The ID of the user for whom booking details are to be
	 *               retrieved.
	 * @return A list containing all booking details for the specified user.
	 */
	List<BookingMapping> getAllBookingDetails();

	/**
	 * Approves the attendance record for a specific user.
	 *
	 * @param userId The ID of the user whose attendance record is being approved.
	 */
	void approveUserAttendance(int userId);

	/**
	 * Creates a new booking with the provided details.
	 *
	 * @param booking The booking model containing booking details such as
	 *                additional desktop, end date, lunch, parking, etc.
	 * @param user    The user for whom the booking is being made.
	 * @throws DataBaseAccessException If an error occurs while accessing the
	 *                                 database.
	 * @throws SQLException 
	 */
	int createBooking(BookingModel booking, User user) throws DataBaseAccessException, SQLException;

	/**
	 * Retrieves a list of all bookings from the database.
	 *
	 * @return A list of BookingDTO objects representing all bookings.
	 * @throws DataBaseAccessException If an error occurs while accessing the
	 *                                 database.
	 */
	List<BookingDTO> getAllBookings() throws DataBaseAccessException;

	void createBookingMapping(BookingModel booking, int bookingId);
}
