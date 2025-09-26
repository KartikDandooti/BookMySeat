package com.valtech.team3.bookmyseatbackend.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.team3.bookmyseatbackend.dao.ShiftDetailsDAO;
import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;
import com.valtech.team3.bookmyseatbackend.exceptions.DataBaseAccessException;
import com.valtech.team3.bookmyseatbackend.service.ShiftDetailsService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ShiftDetailsServiceImpl implements ShiftDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShiftDetailsServiceImpl.class);

	@Autowired
	private ShiftDetailsDAO shiftDetailsDAO;

	@Override
	public List<ShiftDetails> getAllShiftDetails() {
		try {
			LOGGER.info("Fetching Shift Details");

			return shiftDetailsDAO.getAllShiftDetails();
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to get all shifts !");
		}

	}
}