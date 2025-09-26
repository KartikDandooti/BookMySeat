package com.valtech.team3.bookmyseatbackend.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.team3.bookmyseatbackend.dao.HolidayDAO;
import com.valtech.team3.bookmyseatbackend.entities.Holiday;
import com.valtech.team3.bookmyseatbackend.exceptions.DataBaseAccessException;
import com.valtech.team3.bookmyseatbackend.models.HolidayModel;
import com.valtech.team3.bookmyseatbackend.service.HolidayService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class HolidayServiceImpl implements HolidayService {

	private static final Logger LOGGER = LoggerFactory.getLogger(HolidayServiceImpl.class);

	@Autowired
	public HolidayDAO holidayDAO;

	@Override
	public List<Holiday> getAllHolidays() {
		try {
			LOGGER.info("Retrieving holidays from the database !");

			return holidayDAO.getAllHolidays();

		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Error retrieving holiday's from database !");
		}
	}

	@Override
	public void createNewHoliday(HolidayModel holiday) {
		try {
			boolean isHolidayExist = holidayDAO.isHolidayExists(holiday.getHolidayDate(), holiday.getHolidayName());
			if (!isHolidayExist) {
				LOGGER.info("Creating new Holiday !");
				holidayDAO.createHoliday(holiday);
			} else {
				throw new DataBaseAccessException("Holiday already exists !");
			}
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Holiday already Exists !");
		}
	}

	@Override
	public void updateHoliday(HolidayModel holiday) {
		try {
			LOGGER.info("Updating existing Holiday !");
			holidayDAO.updateHoliday(holiday);
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Error Updating existing holiday !");
		}
	}

	@Override
	public List<Holiday> getAllFutureHolidays() {
		try {
			LOGGER.info("Getting all future Holidays !");

			return holidayDAO.getAllFutureHolidays();
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Error getting future Holidays !");
		}
	}
}