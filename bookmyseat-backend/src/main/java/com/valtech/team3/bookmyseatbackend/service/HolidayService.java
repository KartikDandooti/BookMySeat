package com.valtech.team3.bookmyseatbackend.service;

import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.Holiday;
import com.valtech.team3.bookmyseatbackend.models.HolidayModel;

public interface HolidayService {

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
	void createNewHoliday(HolidayModel holiday);

	/**
	 * Updates an existing holiday based on the provided holiday model.
	 *
	 * @param holiday The HolidayModel object containing the updated details of the
	 *                holiday.
	 */
	void updateHoliday(HolidayModel holiday);

	/**
	 * Retrieves a list of all future holidays.
	 *
	 * @return A list of Holiday objects representing all future holidays.
	 */
	List<Holiday> getAllFutureHolidays();

}