package com.valtech.bookmyseat.dao;

import java.sql.SQLException;
import java.util.List;

import com.valtech.bookmyseat.entity.Shift;

/**
 * Interface representing data access operations for managing shifts.
 */
public interface ShiftDAO {

	/**
	 * Retrieves details of all shifts.
	 *
	 * @return List of Shift objects containing details of all shifts.
	 */
	List<Shift> getAllShiftDetails();

	/**
	 * Deletes shift details based on the shift ID.
	 *
	 * @param shiftId The ID of the shift to be deleted.
	 * @return true if the shift details were successfully deleted, false otherwise.
	 */
	boolean deleteShiftDetailsById(int shiftId);

	/**
	 * Adds a new shift time.
	 *
	 * @param shift The Shift object representing the details of the new shift time.
	 * @throws SQLException If there is an error occurred during the database
	 *                      operation.
	 */
	void addShiftTime(Shift shift) throws SQLException;

	/**
	 * Updates an existing shift time.
	 *
	 * @param id    The ID of the shift to be updated.
	 * @param shift The Shift object containing the updated shift details.
	 * @throws SQLException If there is an error occurred during the database
	 *                      operation.
	 */
	void updateShiftTime(int id, Shift shift) throws SQLException;

	/**
	 * Retrieves the Shift object associated with the provided shift ID.
	 *
	 * @param shiftId The ID of the shift for which the Shift object is retrieved.
	 * @return The Shift object corresponding to the provided shift ID, or null if no such shift is found.
	 */
	Shift findShiftByShiftId(int shiftId);

}