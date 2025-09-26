package com.valtech.team3.bookmyseatbackend.dao;

import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;

/**
 * This is an interface for the UserShiftsDAO class.
 */
public interface UserShiftsDAO {

	/**
	 * Checks if a user exists with the particular shift.
	 *
	 * @param userId  the ID of the user
	 * @param shiftId the ID of the shift
	 * @return true if the user exists with the particular shift, false otherwise
	 */
	Boolean doesUserShiftExist(int userId, int shiftId);

	/**
	 * Adds a user with the mentioned shift.
	 *
	 * @param userId  the ID of the user
	 * @param shiftId the ID of the shift
	 */
	void addUserShift(int userId, int shiftId);

	/**
	 * Retrieves all shift details for a given user ID.
	 *
	 * @param userId the ID of the user
	 * @return a list of ShiftDetails objects representing the shift details for the
	 *         user
	 */
	List<ShiftDetails> getAllShiftDetailsByUserId(int userId);

	/**
	 * Deletes all shift for a given user ID.
	 *
	 * @param userId the ID of the user
	 */
	void deleteUserShifts(int userId);
}