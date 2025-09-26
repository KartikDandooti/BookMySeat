package com.valtech.team3.bookmyseatbackend.dao;

import java.time.LocalDate;
import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.Seat;

/**
 * This is an interface for the SeatDAO class.
 */
public interface SeatDAO {

	/**
	 * Retrieves the seat with the specified ID.
	 *
	 * @param seatId The ID of the seat to retrieve.
	 * @return The seat with the specified ID, or {@code null} if not found.
	 * @throws SomeException if there is an error while retrieving the seat.
	 */
	Seat getSeatById(int seatId);

	/**
	 * Retrieves a list of seats from the data source based on the specified floor
	 * identifier.
	 *
	 * @param floorId the unique identifier of the floor for which to retrieve seats
	 * @return a list containing seat objects associated with the specified floor
	 *         identifier
	 */
	List<Seat> getSeatsByFloor(int floorId);

	/**
	 * Finds available seats by floor for a specified date range.
	 * 
	 * This method retrieves a list of available seats on a specified floor within
	 * the given date range. It allows users to search for seats that are not
	 * reserved or occupied during the specified period.
	 * 
	 * @param floorId   The unique identifier of the floor where seats are to be
	 *                  searched.
	 * @param startDate The start date of the date range for which available seats
	 *                  are to be found.
	 * @param endDate   The end date of the date range for which available seats are
	 *                  to be found.
	 * 
	 * @return A List of Seat objects representing available seats on the specified
	 *         floor within the provided date range. The list may be empty if no
	 *         available seats are found, but it will never be null.
	 */
	List<Seat> findAvailableSeatsByFloorOnDate(int floorId, LocalDate startDate, LocalDate endDate);

	/**
	 * Updates the restriction associated with a specific seat.
	 * 
	 * This method updates the restriction associated with the seat identified by
	 * the provided seat ID. It assigns a new restriction ID to the seat,
	 * effectively changing its access permissions or limitations.
	 * 
	 * @param seatId        The unique identifier of the seat to be updated.
	 * @param restrictionId The new restriction ID to be assigned to the seat.
	 */
	void updateSeatRestriction(int seatId, Integer restrictionId);

	/**
	 * Retrieves a list of all reserved seats.
	 * 
	 * This method fetches a list of all seats that are currently reserved,
	 * regardless of whether the reservation is associated with a user, project, or
	 * any other entity.
	 * 
	 * @return A List of Seat objects representing all reserved seats. The list may
	 *         be empty if there are no reserved seats, but it will never be null.
	 */
	List<Seat> getAllReservedSeats();

	/**
	 * Retrieves a list of all seats reserved by users.
	 * 
	 * This method fetches a list of all seats that are currently reserved by users.
	 * It retrieves information about seats that have been reserved by individual
	 * users for various purposes.
	 * 
	 * @return A List of Seat objects representing all seats reserved by users. The
	 *         list may be empty if there are no user-reserved seats, but it will
	 *         never be null.
	 */
	List<Seat> getAllUserReservedSeats();

	/**
	 * Retrieves a list of all seats reserved for projects.
	 * 
	 * This method fetches a list of all seats that are currently reserved for
	 * projects. It retrieves information about seats that have been allocated for
	 * specific projects, such as team rooms, project spaces, or reserved areas
	 * within a workspace.
	 * 
	 * @return A List of Seat objects representing all seats reserved for projects.
	 *         The list may be empty if there are no project-reserved seats, but it
	 *         will never be null.
	 */
	List<Seat> getAllProjectReservedSeats();

	/**
	 * Updates the project reservation associated with a specific restriction.
	 * 
	 * This method updates the project reservation associated with the restriction
	 * identified by the provided restriction ID. It may involve modifying the
	 * reservation status, duration, or any other relevant information related to
	 * the project reservation.
	 * 
	 * @param restrictionId The unique identifier of the restriction for which the
	 *                      project reservation is to be updated.
	 */
	void updateProjectReserveation(int restrictionId);
}