package com.valtech.bookmyseat.dao;

import java.time.LocalDate;
import java.util.List;

import com.valtech.bookmyseat.entity.Seat;

/**
 * This interface provides methods for accessing seat data in the database.
 */
public interface SeatDAO {

	/**
	 * Retrieves the list of available seats on a specific floor within the
	 * specified date range.
	 * 
	 * @param floorId   The ID of the floor where the seats are located.
	 * @param startDate The start date of the date range to check seat availability.
	 * @param endDate   The end date of the date range to check seat availability.
	 * @return A list of available Seat objects on the specified floor within the
	 *         given date range.
	 */
	List<Seat> findAvailableSeatsByFloorOnDate(int floorId, LocalDate startDate, LocalDate endDate);

	/**
	 * Retrieves the Seat object associated with the provided seat ID.
	 *
	 * @param seatId The ID of the seat for which the Seat object is retrieved.
	 * @return The Seat object corresponding to the provided seat ID, or null if no
	 *         such seat is found.
	 */
	Seat findSeatById(int seatNumber, int floorId);
}