package com.valtech.bookmyseat.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.valtech.bookmyseat.entity.Holiday;

/**
 * HolidayService interface defines methods related to holiday management.
 */
public interface HolidayService {

	/**
	 * Retrieves a list of holidays for a given month.
	 *
	 * @param holiday_month The month for which holidays are to be retrieved.
	 * @return A list of holidays for the specified month.
	 * 
	 *         /** Adds a new holiday by an admin user.
	 *
	 * @param holiday The holiday object to be added.
	 * @return A message indicating the result of the addition operation.
	 */
	String addHolidayByAdmin(Holiday holiday);

	/**
	 * Deletes a holiday by its ID.
	 *
	 * @param holidayId The ID of the holiday to be deleted.
	 * @return A message indicating the result of the deletion operation.
	 * @throws DataAccessException if an error occurs while accessing the data
	 *                             layer.
	 */
	String deleteHoliday(int holidayId) throws DataAccessException;

	/**
	 * Retrieves a list of holidays based on the date.
	 *
	 * @return A list of holidays.
	 */
	List<Holiday> getHoildayBydate();
	/**
	 * Retrieves a list of all holidays.
	 * 
	 * This method fetches a list of all holidays available in the system.
	 * Holidays typically include public holidays, special observances, or 
	 * any other designated non-working days.
	 * 
	 * @return A list containing objects of type {@code Holiday}, representing 
	 *         all holidays available in the system.
	 */
	List<Holiday> getAllHolidays();

}
