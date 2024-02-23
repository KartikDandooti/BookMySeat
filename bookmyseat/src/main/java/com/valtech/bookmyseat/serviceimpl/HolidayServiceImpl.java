package com.valtech.bookmyseat.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.valtech.bookmyseat.dao.HolidayDAO;
import com.valtech.bookmyseat.entity.Holiday;
import com.valtech.bookmyseat.service.HolidayService;

@Service
public class HolidayServiceImpl implements HolidayService {
	private static final Logger LOGGER = LoggerFactory.getLogger(HolidayServiceImpl.class);

	@Autowired
	private HolidayDAO holidayDAO;

	@Override
	public List<Holiday> getAllHolidays() {
		LOGGER.debug("Fetching all holidays");

		return holidayDAO.getAllHolidays();
	}

	@Override
	public String addHolidayByAdmin(Holiday holiday) {
		LOGGER.debug("Adding new holidays");
		holidayDAO.addHoliday(holiday);

		return "Holiday Added succesfully";
	}

	@Override
	public String deleteHoliday(int holidayId) throws DataAccessException {
		LOGGER.info("deleting the holidays from the database");
		boolean success = holidayDAO.deleteHoliday(holidayId);
		if (success) {
			LOGGER.debug("Holiday Details with Id : {} has been deleted successfully", holidayId);

			return "Holiday Deatils with Id " + holidayId + " has been deleted successfully";
		} else {
			return "Failed to delete the Holiday details with id " + holidayId;
		}
	}

	@Override
	public List<Holiday> getHoildayBydate() {
		LOGGER.debug("List of holidays");

		return holidayDAO.getHolidayByDate();
	}
}