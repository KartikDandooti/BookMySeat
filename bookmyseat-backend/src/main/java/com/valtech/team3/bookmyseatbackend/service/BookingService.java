package com.valtech.team3.bookmyseatbackend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.valtech.team3.bookmyseatbackend.entities.Booking;
import com.valtech.team3.bookmyseatbackend.entities.User;
import com.valtech.team3.bookmyseatbackend.models.BookingModel;
import com.valtech.team3.bookmyseatbackend.models.UserDashboardModel;

public interface BookingService {

	/**
	 * Creates a new booking entry in the database.
	 *
	 * This method creates a new booking entry in the database based on the provided
	 * booking details and associates it with the specified user.
	 *
	 * @param booking The booking details to be saved.
	 * @param user    The user associated with the booking.
	 * @return The ID of the newly created booking.
	 * @throws SomeExceptionType if there is an error while creating the booking.
	 */
	Integer createBooking(BookingModel booking, User user);

	/**
	 * Retrieves a list of bookings for the specified user before the current date.
	 *
	 * @param userId The ID of the user for whom to retrieve the bookings.
	 * @return A list of bookings before the current date for the specified user.
	 * @throws DataAccessException if there is an error accessing the data source.
	 */
	List<Booking> getAllBookings(int userId);

	/**
	 * Cancels a booking in the database with the provided booking ID.
	 * 
	 * @param bookingId the ID of the booking to cancel
	 */
	void cancelBooking(int bookingId);

	/**
	 * Retrieves the list of preferred seats for users working on a specific floor
	 * and project.
	 *
	 * @param floorId   The ID of the floor where the seats are located.
	 * @param projectId The ID of the project for which the seats are preferred.
	 * @param startDate The start date from which to retrieve the preferred seats.
	 * @return A list of Booking objects representing the preferred seats for users
	 *         working on the specified floor and project.
	 */
	List<Booking> getUserPreferredSeats(int floorId, int projectId, LocalDate startDate);

	/**
	 * Retrieves a booking by its ID.**
	 * 
	 * @param id The ID of the booking to retrieve.*@return The booking with the
	 *           specified ID, or null if no booking is found.
	 */
	Booking getBookingById(int id);

	/**
	 * Updates the seat associated with the specified booking.
	 *
	 * @param booking   The updated booking information.
	 * @param bookingId The ID of the booking to be updated.
	 */

	void updateBookingByAdmin(BookingModel booking);

	/**
	 * Retrieves a list of bookings for a specific floor within the date range
	 * specified in the BookingModel.
	 *
	 * @param bookingModel The BookingModel containing the floor ID, start date, and
	 *                     end date for filtering bookings.
	 * @return A list of bookings for the specified floor within the given date
	 *         range.
	 */
	List<Booking> getBookingsforFloorBetweenDates(BookingModel bookingModel);

	/**
	 * Retrieves the attendance count for the current month of the specified user.
	 *
	 * @param userId The ID of the user for whom the attendance count is to be
	 *               retrieved.
	 * @return The number of attendances recorded for the current month for the
	 *         specified user.
	 */
	Integer getAttendanceCountForCurrentMonth(int userId);

	/**
	 * Retrieves the dashboard information for the specified user.
	 *
	 * @param userId The ID of the user for whom the dashboard information is to be
	 *               retrieved.
	 * @return A list of UserDashboard objects containing the dashboard information
	 *         for the specified user.
	 */
	List<UserDashboardModel> getUserDashboard(int userId);

	/**
	 * Retrieves all bookings associated with the specified user ID.
	 *
	 * @param userId the ID of the user whose bookings are to be retrieved
	 * @return a list of bookings associated with the user
	 */
	List<Booking> getAllUserBookings(int userId);
}