package com.valtech.team3.bookmyseatbackend.service;

import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;

public interface UserShiftsService {

	/**
	 * Adds shifts to a user.
	 *
	 * @param userId  the ID of the user
	 * @param shiftId the ID of the shift to be added
	 */
	void addShiftsToUser(int userId, int shiftId);

	/**
	 * Retrieves all shift details for a given user ID.
	 *
	 * @param userId the ID of the user
	 * @return a list of ShiftDetails objects representing the shift details for the
	 *         user
	 */
	List<ShiftDetails> getAllShiftDetailsByUserId(int userId);

	/**
	 * Deletes all shifts for a given user ID.
	 *
	 * @param userId the ID of the user
	 */
	void deleteShiftsForUser(int userId);
}