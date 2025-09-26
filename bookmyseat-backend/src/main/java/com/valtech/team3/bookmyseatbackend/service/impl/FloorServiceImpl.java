package com.valtech.team3.bookmyseatbackend.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.team3.bookmyseatbackend.dao.FloorDAO;
import com.valtech.team3.bookmyseatbackend.entities.Floor;
import com.valtech.team3.bookmyseatbackend.exceptions.DataBaseAccessException;
import com.valtech.team3.bookmyseatbackend.service.FloorService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class FloorServiceImpl implements FloorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FloorServiceImpl.class);

	@Autowired
	private FloorDAO floorDAO;

	@Override
	public List<Floor> getAllFloors() {
		try {
			LOGGER.info("Retrieving all Floors !");

			return floorDAO.getAllFloors();
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to get all floors !");
		}
	}
}