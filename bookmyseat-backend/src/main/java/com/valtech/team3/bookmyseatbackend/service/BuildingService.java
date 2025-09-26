package com.valtech.team3.bookmyseatbackend.service;

import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.Building;

public interface BuildingService {

	/**
	 * Adds a new building with the specified name and location to the system.
	 *
	 * @param buildingName The name of the building to add.
	 * @param locationName The name of the location where the building is situated.
	 * @throws RuntimeException If an error occurs while adding the building.
	 */
	void addBuilding(String buildingName, String locationName) throws RuntimeException;

	/**
	 * Retrieves a list of all buildings available in the system.
	 *
	 * @return A list of Building objects representing all buildings.
	 */
	List<Building> getAllBuildings();
}