package com.valtech.bookmyseat.dao;

import java.util.List;

import com.valtech.bookmyseat.entity.BookingMapping;
import com.valtech.bookmyseat.model.BookingMappingModel;

public interface BookingMappingDAO {

	/**
	 * Creates a new booking mapping entry based on the provided
	 * BookingMappingModel.
	 * 
	 * @param bookingMappingModel The BookingMappingModel object containing the
	 *                            details of the booking mapping to be created.
	 */
	void createBookingMapping(BookingMappingModel bookingMappingModel);

	/**
	 * Retrieves a list of all booking Employee IDs for the current date.
	 *
	 * @return A list of integers representing the booking Employee IDs for the
	 *         current date.
	 */
	List<BookingMapping> getAllBookingforCurrentDate();

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
	boolean isBookingAvailableForCurrentDate(int employeeId);

	/**
	 * Checks if the attendance is marked for the specified employee.
	 *
	 * @param employeeId The ID of the employee to check for attendance marking.
	 * @return true if the attendance is marked for the specified employee, false
	 *         otherwise.
	 */
	boolean isAttendanceMarked(int employeeId);

	/**
	 * Retrieves a list of booking mappings associated with the specified booking
	 * ID.
	 * 
	 * @param bookingId The ID of the booking for which to retrieve the booking
	 *                  mappings.
	 * @return A list of {@code BookingMapping} objects associated with the
	 *         specified booking ID.
	 */
	List<BookingMapping> getBookingMappingsByBookingId(int bookingId);
}