package com.valtech.bookmyseat.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.valtech.bookmyseat.entity.Shift;

/**
 * Service interface for managing shift details.
 */
public interface ShiftDetailsService {

	/**
	 * Retrieves all shift details.
	 * 
	 * @return List of Shift objects representing all shift details.
	 * @throws SQLException if there is an error accessing the database
	 */
	List<Shift> getAllShiftDetails() throws DataAccessException;

	/**
	 * Deletes a shift based on its ID.
	 * 
	 * @param shiftId The ID of the shift to delete.
	 * @return A message indicating the result of the deletion operation.
	 * @throws SQLException if there is an error accessing the database
	 */
	String deleteShift(int shiftId) throws DataAccessException;

	/**
	 * Adds a new shift time.
	 * 
	 * @param shift The Shift object representing the shift time to add.
	 * @return A message indicating the result of the addition operation.
	 * @throws SQLException if there is an error accessing the database
	 */
	String addShiftTime(Shift shift) throws DataAccessException;

	/**
	 * Updates an existing shift time.
	 * 
	 * @param shiftId The ID of the shift to update.
	 * @param shift   The Shift object representing the updated shift time.
	 * @return A message indicating the result of the update operation.
	 * @throws SQLException if there is an error accessing the database
	 */
	String updateShiftTime(int shiftId, Shift shift) throws DataAccessException;
}
