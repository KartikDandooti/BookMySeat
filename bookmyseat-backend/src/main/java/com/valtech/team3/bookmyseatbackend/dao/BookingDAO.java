package com.valtech.team3.bookmyseatbackend.dao;

import java.time.LocalDate;
import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.Booking;
import com.valtech.team3.bookmyseatbackend.entities.Seat;
import com.valtech.team3.bookmyseatbackend.entities.User;
import com.valtech.team3.bookmyseatbackend.models.BookingModel;
import com.valtech.team3.bookmyseatbackend.models.UserDashboardModel;

/**
 * This is an interface for the BookingDAO class.
 */
public interface BookingDAO {

	/**
	 * Creates a new booking entry in the database.
	 *
	 * This method creates a new booking entry in the database based on the provided
	 * booking details, associated user, and the seat.
	 *
	 * @param booking The booking details to be saved.
	 * @param user    The user associated with the booking.
	 * @param seat    The seat to be booked.
	 * @return The ID of the newly created booking.
	 * @throws SomeExceptionType if there is an error while creating the booking.
	 */
	Integer createBooking(BookingModel booking, User user, Seat seat);

	/**
	 * Checks if the user has already booked a seat for the specified date range.
	 *
	 * This method checks if the user with the given user ID has already booked a
	 * seat for any date within the specified date range, inclusive of both start
	 * and end dates.
	 *
	 * @param userId    The ID of the user to check for existing bookings.
	 * @param startDate The start date of the date range to check.
	 * @param endDate   The end date of the date range to check.
	 * @return {@code true} if the user has already booked a seat for any date
	 *         within the specified date range, {@code false} otherwise.
	 * @throws SomeExceptionType if there is an error while checking for existing
	 *                           bookings.
	 */
	Boolean hasAlreadyBookedForDate(int userId, LocalDate startDate, LocalDate endDate);

	/**
	 * Retrieves a booking from the database by its unique identifier.
	 * 
	 * @param bookingId the ID of the booking to retrieve
	 * @return the booking corresponding to the provided ID, or {@code null} if no
	 *         booking is found
	 */
	Booking getBookingById(int bookingId);

	/**
	 * Cancels a booking in the database by setting its status to CANCELED.
	 * 
	 * @param bookingId the ID of the booking to cancel
	 * @throws IllegalArgumentException if no booking with the provided ID is found
	 */
	void cancelBooking(int bookingId);

	/**
	 * Retrieves a list of bookings based on the floor and project, so that an
	 * employee can book a seat next to their co-worker.
	 *
	 * This method retrieves a list of bookings from the database based on the
	 * provided floor ID, project ID, and start date. The returned list contains all
	 * bookings that match the specified floor and project, and have a start date on
	 * or after the provided start date.
	 *
	 * @param floorId   The ID of the floor where the user wants to book a seat.
	 * @param projectId The ID of the project under which the user is working.
	 * @param startDate The start date from which to retrieve bookings.
	 * @return A list of Booking objects representing the bookings that match the
	 *         specified floor, project, and start date.
	 */
	List<Booking> userPreferredSeats(int floorId, int projectId, LocalDate startDate);

	/**
	 * Retrieves a list of all bookings from the system.
	 *
	 * @return A list of Booking objects representing all bookings.
	 */
	List<Booking> getAllBookings(int userid);

	/**
	 * Updates the seat booking associated with the provided booking ID using the
	 * information specified in the booking model.
	 *
	 * @param booking   the booking model containing the updated seat booking
	 *                  information
	 * @param bookingId the ID of the seat booking to be updated
	 */
	void updateBookingByAdmin(BookingModel booking, int bookingId, int seatId);

	/**
	 * Retrieves all bookings associated with a specific user.
	 *
	 * @param id The unique identifier of the user.
	 * @return A list of Booking objects representing the user's bookings.
	 */
	List<Booking> getAllUserBookings(int id);

	/**
	 * Retrieves a list of bookings for a specific floor within the specified date
	 * range.
	 *
	 * @param floorId   The ID of the floor for which bookings are to be retrieved.
	 * @param startDate The start date of the date range.
	 * @param endDate   The end date of the date range.
	 * @return A list of bookings for the specified floor within the given date
	 *         range.
	 */
	List<Booking> findBookingsByFloorAndDates(int floorId, LocalDate startDate, LocalDate endDate);

	/**
	 * Retrieves the attendance count for the current month of the specified user.
	 *
	 * @param userId The ID of the user for whom the attendance count is to be
	 *               retrieved.
	 * @return The attendance count for the current month of the specified user.
	 */
	Integer getAttendanceCountForCurrentMonth(int userId);

	/**
	 * Retrieves the dashboard information for the specified user.
	 *
	 * @param userId The ID of the user for whom the dashboard information is to be
	 *               retrieved.
	 * @return A list of UserDashboardModel objects containing the dashboard
	 *         information for the specified user.
	 */
	List<UserDashboardModel> getUserDashboard(int userId);

	/**
	 * Retrieves a booking based on the provided booking mapping ID.
	 *
	 * @param bookingMappingId The ID of the booking mapping associated with the
	 *                         booking.
	 * @return The booking associated with the provided booking mapping ID, or null
	 *         if no booking is found.
	 */
	Booking getBookingByMappingId(int bookingMappingId);
}