package com.valtech.team3.bookmyseatbackend.dao;

import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;

/**
 * This is an interface for the SHiftDeatilsDAO class.
 */
public interface ShiftDetailsDAO {

	/**
	 * Retrieves the shift details with the specified ID.
	 * 
	 * @param shiftId The ID of the shift details to retrieve.
	 * @return The shift details with the specified ID, or {@code null} if not
	 *         found.
	 * @throws SomeException if there is an error while retrieving the shift
	 *                       details.
	 */
	ShiftDetails getShiftDetailsById(int shiftId);

	/**
	 * Retrieve a list of all shift details.
	 * 
	 * @return A list containing all shift details available in the system.
	 */
	List<ShiftDetails> getAllShiftDetails();
}