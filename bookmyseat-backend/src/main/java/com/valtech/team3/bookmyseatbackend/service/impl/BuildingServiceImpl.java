package com.valtech.team3.bookmyseatbackend.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.team3.bookmyseatbackend.dao.BuildingDAO;
import com.valtech.team3.bookmyseatbackend.entities.Building;
import com.valtech.team3.bookmyseatbackend.exceptions.DataBaseAccessException;
import com.valtech.team3.bookmyseatbackend.service.BuildingService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BuildingServiceImpl implements BuildingService {

	private static final Logger logger = LoggerFactory.getLogger(BuildingServiceImpl.class);

	@Autowired
	private BuildingDAO buildingDAO;

	@Override
	public void addBuilding(String buildingName, String locationName) {
		try {
			if (buildingDAO.isBuildingNameExists(buildingName)) {
				throw new DataBaseAccessException("Building with name already exists !");
			}
			buildingDAO.createBuilding(buildingName, locationName);
			logger.info("New building added: Name - {}, Location - {}", buildingName, locationName);
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to add building !");
		}
	}

	@Override
	public List<Building> getAllBuildings() {
		try {
			return buildingDAO.getAllBuildings();
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to fetch all the buildings !");
		}
	}
}