package com.valtech.bookmyseat.dao;

import java.util.List;

import com.valtech.bookmyseat.entity.Holiday;

public interface HolidayDAO {

	/**
	 * Adds a new holiday to the database.
	 * 
	 * @param holiday The Holiday object representing the holiday to be added.
	 */
	void addHoliday(Holiday holiday);

	/**
	 * Deletes a holiday from the database based on the provided holiday ID.
	 * 
	 * @param holidayId The unique identifier of the holiday to be deleted.
	 * @return {@code true} if the holiday was successfully deleted, {@code false}
	 *         otherwise.
	 */
	boolean deleteHoliday(int holidayId);

	/**
	 * Retrieves a list of holidays based on their dates.
	 * 
	 * @return A list of Holiday objects representing the holidays found in the
	 *         database, sorted by date.
	 */
	List<Holiday> getHolidayByDate();

	/**
	 * Retrieves a list of all holidays.
	 *
	 * @return A List of Holiday objects representing all holidays in the system.
	 */
	List<Holiday> getAllHolidays();
}
