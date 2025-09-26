package com.valtech.team3.bookmyseatbackend.dao;

import java.time.LocalDate;
import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.BookingMapping;
import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;
import com.valtech.team3.bookmyseatbackend.models.AdminDashboardModel;
import com.valtech.team3.bookmyseatbackend.models.BookingModel;

/**
 * This is an interface for the BookingMappingDAO class.
 */
public interface BookingMappingDAO {

	/**
	 * Creates a mapping between the provided booking, booking ID, and shift details
	 * in the database.
	 *
	 * This method creates an association between the given booking details, booking
	 * ID, and shift details in the database, allowing for the tracking and
	 * management of the booking-related data along with shift details.
	 *
	 * @param booking   The booking details to be mapped.
	 * @param bookingId The ID of the booking to which the provided booking details
	 *                  will be associated.
	 * @param shift     The shift details associated with the booking.
	 * @throws SomeExceptionType if there is an error while creating the booking
	 *                           mapping.
	 */
	void createBookingMapping(BookingModel booking, int bookingId, ShiftDetails shift);

	/**
	 * Retrieves a list of all booking Employee IDs for the current date.
	 *
	 * @return A list of integers representing the booking Employee IDs for the
	 *         current date.
	 */
	List<BookingMapping> getAllBookingsForCurrentDate();

	/**
	 * Updates the attendance status of an employee with the specified employee ID.
	 *
	 * @param employeeId The ID of the employee whose attendance status needs to be
	 *                   updated.
	 */
	void updateAttendence(int employeeId);

	/**
	 * Checks if a booking is available for the current date for the specified
	 * employee.
	 *
	 * @param employeeId The ID of the employee to check for booking availability.
	 * @return true if a booking is available for the current date for the specified
	 *         employee, false otherwise.
	 */
	Boolean isBookingAvailableForCurrentDate(int employeeId);

	/**
	 * Checks if the attendance is marked for the specified employee.
	 *
	 * @param employeeId The ID of the employee to check for attendance marking.
	 * @return true if the attendance is marked for the specified employee, false
	 *         otherwise.
	 */
	Boolean isAttendanceMarkedForCurrentDate(int employeeId);

	/**
	 * Retrieves the booking mapping with the specified ID.
	 *
	 * @param bookingMappingId The ID of the booking mapping to retrieve.
	 * @return The booking mapping with the specified ID, or null if not found.
	 */
	BookingMapping getBookingMappingById(int bookingMappingId);

	/**
	 * Retrieves a list of booking mappings associated with the specified booking
	 * ID.
	 *
	 * @param bookingId The ID of the booking for which to retrieve the mapping.
	 * @return A list of booking mappings associated with the specified booking ID.
	 */
	List<BookingMapping> getBookingMappingsByBookingId(int bookingId);

	/**
	 * Retrieves a list of all booking mappings that have their attendance marked
	 * for the current date.
	 *
	 * @return A list of {@code BookingMapping} objects representing the booking
	 *         mappings that have their attendance marked for the current date.
	 */
	List<BookingMapping> getAllBookingsAttendanceMarkedForCurrentDate();

	/**
	 * Updates the details of the specified BookingMapping for the given booking ID.
	 *
	 * @param bookingMapping The BookingMapping object containing the updated
	 *                       details.
	 * @param bookingId      The ID of the booking for which to update the
	 *                       BookingMapping.
	 */
	void updateBooking(BookingMapping bookingMapping, int bookingId);

	/**
	 * Retrieves the AdminDashboardModel object.
	 *
	 * This method retrieves an instance of the AdminDashboardModel, which
	 * represents the data and functionality related to the admin dashboard. The
	 * AdminDashboardModel encapsulates various data points and operations necessary
	 * for presenting and managing administrative dashboard information, such as
	 * user statistics, system status, and administrative controls.
	 *
	 * @return An instance of AdminDashboardModel containing the necessary data and
	 *         operations for the admin dashboard. The returned object may provide
	 *         methods to retrieve statistics, manage users, configure system
	 *         settings, or perform other administrative tasks.
	 *
	 * @see AdminDashboardModel
	 */
	AdminDashboardModel getAdminDashboardModel();

	/**
	 * Checks if attendance is marked for the specified booking.
	 *
	 * @param bookingId The ID of the booking to check for attendance marking.
	 * @return {@code true} if attendance is marked for the booking, {@code false}
	 *         otherwise.
	 */
	Boolean isAttendanceMarkedForBooking(int bookingId);

	/**
	 * Retrieves the count of bookings with a specified vehicle type within a given
	 * date range.
	 *
	 * @param vehicleType The type of vehicle for which the count is requested.
	 * @param startDate   The start date of the date range.
	 * @param endDate     The end date of the date range.
	 * @return The count of bookings with the specified vehicle type within the
	 *         given date range.
	 */
	Integer getVehicleTypeCount(String vehicleType, LocalDate startDate, LocalDate endDate);

	/**
	 * Checks if attendance is marked for the specified booking mapping.
	 *
	 * @param bookingMappingId the ID of the booking mapping to check
	 * @return true if attendance is marked for the specified booking mapping, false
	 *         otherwise
	 */
	Boolean isAttendanceMarkedForBookingMapping(int bookingMappingId);
}