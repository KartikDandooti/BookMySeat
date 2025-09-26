package com.valtech.team3.bookmyseatbackend.service;

import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.Seat;
import com.valtech.team3.bookmyseatbackend.models.BookingModel;

/**
 * This is an interface for the SeatService class.
 */
public interface SeatService {

	/**
	 * Retrieves a list of seats from the data source based on the specified floor
	 * identifier.
	 *
	 * @param floorId the unique identifier of the floor for which to retrieve seats
	 * @return a list containing seat objects associated with the specified floor
	 *         identifier
	 */
	List<Seat> getAvailableSeatsByFloorOnDate(BookingModel bookingModel);

	/**
	 * Retrieves a list of seats based on the specified floor identifier.
	 *
	 * @param floorId the unique identifier of the floor for which to retrieve seats
	 * @return a list containing seat objects associated with the specified floor
	 *         identifier
	 */
	List<Seat> getAllSeatsByFloor(int floorId);

	/**
	 * Retrieves a list of reserved seats.
	 *
	 * @return a list containing seat objects which are reserved
	 */
	List<Seat> getAllReservedSeats();

	/**
	 * Retrieves a list of all reserved seats associated with users.
	 * 
	 * This method is responsible for fetching a list of all seats that are
	 * currently reserved by users. It retrieves information about seats that have
	 * been reserved by users, typically for events, meetings, or other purposes.
	 * 
	 * @return A List of Seat objects representing all reserved seats associated
	 *         with users. The list may be empty if there are no reserved seats, but
	 *         it will never be null.
	 * 
	 * @see Seat
	 */
	List<Seat> getAllUserReservedSeats();

	/**
	 * Retrieves a list of all reserved seats associated with projects.
	 * 
	 * This method fetches a list of all seats that are currently reserved for
	 * projects. It retrieves information about seats that have been allocated for
	 * specific projects, such as team rooms, project spaces, or reserved areas
	 * within a workspace.
	 * 
	 * @return A List of Seat objects representing all reserved seats associated
	 *         with projects. The list may be empty if there are no reserved seats
	 *         for projects, but it will never be null.
	 * 
	 * @see Seat
	 */
	List<Seat> getAllProjectReservedSeats();
}