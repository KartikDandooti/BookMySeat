package com.valtech.team3.bookmyseatbackend.service;

import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.BookingMapping;
import com.valtech.team3.bookmyseatbackend.models.AdminDashboardModel;
import com.valtech.team3.bookmyseatbackend.models.BookingMappingModel;
import com.valtech.team3.bookmyseatbackend.models.BookingModel;

public interface BookingMappingService {

	/**
	 * Creates a mapping between the provided booking and the specified booking ID
	 * in the database.
	 *
	 * This method creates an association between the given booking details and the
	 * specified booking ID in the database, allowing for the tracking and
	 * management of the booking-related data.
	 *
	 * @param booking   The booking details to be mapped.
	 * @param bookingId The ID of the booking to which the provided booking details
	 *                  will be associated.
	 * @throws SomeExceptionType if there is an error while creating the booking
	 *                           mapping.
	 */
	void createBookingMapping(BookingModel booking, int bookingId);

	/**
	 * 
	 * Retrieves a list of sub-bookings (booking mappings) associated with the
	 * specified booking ID.
	 *
	 * @param bookingId The ID of the booking for which to retrieve the
	 *                  sub-bookings.
	 * @return A list of sub-bookings associated with the specified booking ID.
	 */
	List<BookingMapping> getSubBookingsByBookingId(int bookingId);

	/**
	 * Edits the specified booking mapping and its sub-bookings.
	 *
	 * @param bookingId         The ID of the booking to which the sub-booking
	 *                          belongs.
	 * @param updatedSubBooking The updated details of the sub-booking to be edited.
	 * @param bookingMappingId  The ID of the booking mapping (sub-booking) to be
	 *                          edited.
	 * @throws IllegalArgumentException If the updated sub-booking is null, or if
	 *                                  the booking mapping or shift details are not
	 *                                  found.
	 * @throws IllegalArgumentException If the vehicle type is required for parking
	 *                                  but not provided, or if an invalid vehicle
	 *                                  type is provided.
	 */
	void editBookingAndSubBookings(BookingMappingModel updatedSubBooking, int bookingMappingId);

	/*
	 * Updates the attendance of the user with the specified employee ID.
	 *
	 * @param employeeId The ID of the employee whose attendance is being updated.
	 */
	void updateUserAttendence(int employeeId);

	/**
	 * Checks if there is any booking available for the current date for the
	 * specified employee.
	 *
	 * @param employeeId The ID of the employee to check for booking availability
	 *                   for current date.
	 * @return {@code true} if there is a booking available for the current date,
	 *         {@code false} otherwise.
	 */
	Boolean isBookingAvailableForCurrentDate(int employeeId);

	/**
	 * Checks if the attendance is marked for the employee with the specified ID.
	 *
	 * @param employeeId The ID of the employee to check for attendance whether
	 *                   marked or not.
	 * @return {@code true} if the attendance is marked for the employee,
	 *         {@code false} otherwise.
	 */
	Boolean isAttendanceMarkedForCurrentDate(int employeeId);

	/**
	 * Retrieves a list of all booking Employee IDs for the current date.
	 *
	 * @return A list of integers representing the booking Employee IDs for the
	 *         current date.
	 */
	List<BookingMapping> getAllBookingsForCurrentDate();

	/**
	 * Retrieves a list of all booking mappings that have their attendance marked
	 * for the current date.
	 *
	 * @return A list of {@code BookingMapping} objects representing the booking
	 *         mappings that have their attendance marked for the current date.
	 */
	List<BookingMapping> getAllBookingsAttendanceMarkedForCurrentDate();

	/**
	 * Retrieves the admin's dashboard model.
	 *
	 * @return The AdminDashboardModel object representing the administrative
	 *         dashboard.
	 */
	AdminDashboardModel getAdminDashboardModel();
}