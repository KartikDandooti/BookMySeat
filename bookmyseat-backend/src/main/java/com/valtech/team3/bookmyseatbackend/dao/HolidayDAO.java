package com.valtech.team3.bookmyseatbackend.dao;

import java.time.LocalDate;
import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.Holiday;
import com.valtech.team3.bookmyseatbackend.models.HolidayModel;

/**
 * This is an interface for the HolidayDAO class.
 */
public interface HolidayDAO {

	/**
	 * Retrieves a list of holidays based on their dates.
	 * 
	 * @return A list of Holiday objects representing the holidays found in the
	 *         database.
	 */
	List<Holiday> getHolidayByDate();

	/**
	 * Retrieves a list of all holidays.
	 *
	 * @return A list of Holiday objects representing all holidays.
	 */
	List<Holiday> getAllHolidays();

	/**
	 * Creates a new holiday based on the provided holiday model.
	 *
	 * @param holiday The HolidayModel object containing the details of the new
	 *                holiday to be created.
	 */
	void createHoliday(HolidayModel holiday);

	/**
	 * Updates an existing holiday based on the provided holiday model.
	 *
	 * @param holiday The HolidayModel object containing the updated details of the
	 *                holiday.
	 */
	void updateHoliday(HolidayModel holiday);

	/**
	 * Checks if a holiday with the given date and name already exists.
	 *
	 * @param date        The date of the holiday.
	 * @param holidayName The name of the holiday.
	 * @return true if the holiday exists, false otherwise.
	 */
	Boolean isHolidayExists(LocalDate date, String holidayName);

	/**
	 * Retrieves a list of all future holidays.
	 *
	 * @return A list of Holiday objects representing all future holidays.
	 */
	List<Holiday> getAllFutureHolidays();
}
