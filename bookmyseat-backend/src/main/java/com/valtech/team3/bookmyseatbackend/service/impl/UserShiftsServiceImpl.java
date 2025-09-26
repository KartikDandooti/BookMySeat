package com.valtech.team3.bookmyseatbackend.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.team3.bookmyseatbackend.dao.UserShiftsDAO;
import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;
import com.valtech.team3.bookmyseatbackend.exceptions.FailedToAddShiftException;
import com.valtech.team3.bookmyseatbackend.exceptions.FailedToDeleteShiftsException;
import com.valtech.team3.bookmyseatbackend.exceptions.ShiftsNotFoundException;
import com.valtech.team3.bookmyseatbackend.service.UserShiftsService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserShiftsServiceImpl implements UserShiftsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserShiftsServiceImpl.class);

	@Autowired
	private UserShiftsDAO userShiftsDAO;

	@Override
	public void addShiftsToUser(int userId, int shiftId) {
		LOGGER.info("Mapping user with the particular shift");
		LOGGER.debug("Mapping user with the particular shift with userId:{} and shiftId:{} ", userId, shiftId);
		try {
			userShiftsDAO.addUserShift(userId, shiftId);
			LOGGER.info("User added with the shift");
		} catch (RuntimeException e) {
			throw new FailedToAddShiftException("Failed to add shift for user.");
		}
	}

	@Override
	public List<ShiftDetails> getAllShiftDetailsByUserId(int userId) {
		LOGGER.info("Fetching shift details based on userId");
		List<ShiftDetails> shiftDetails = userShiftsDAO.getAllShiftDetailsByUserId(userId);
		LOGGER.debug("Fetching shift details based on userId:{}", userId);
		if (shiftDetails.isEmpty()) {
			throw new ShiftsNotFoundException("No shifts found for user.");
		}

		LOGGER.info("Sending all shift details based on userId");

		return shiftDetails;
	}

	@Override
	public void deleteShiftsForUser(int userId) {
		LOGGER.info("Deleting existing shifts for user with ID: {}", userId);
		try {
			userShiftsDAO.deleteUserShifts(userId);
			LOGGER.info("Shifts deleted successfully for user.");
		} catch (RuntimeException e) {
			throw new FailedToDeleteShiftsException("Failed to delete shifts for user.");
		}
	}
}