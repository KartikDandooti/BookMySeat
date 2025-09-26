package com.valtech.team3.bookmyseatbackend.dao;

import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.Building;

/**
 * This is an interface for the BuildingDAO class.
 */
public interface BuildingDAO {

	/**
	 * Retrieves a building from the data source based on its unique identifier.
	 *
	 * @param id the unique identifier of the building to retrieve
	 * @return the building object corresponding to the given identifier, or null if
	 *         no building with the specified ID is found
	 */
	Building getBuildingById(int id);

	/**
	 * Retrieves all buildings from the database.
	 * 
	 * @return A list of Building objects representing all buildings.
	 */
	List<Building> getAllBuildings();

	/**
	 * Creates a new building with the specified name and location.
	 * 
	 * @param buildingName The name of the building to create.
	 * @param locationName The name of the location where the building is located.
	 */
	void createBuilding(String buildingName, String locationName);

	/**
	 * Checks if a building with the specified name already exists in the database.
	 * 
	 * @param buildingName The name of the building to check.
	 * @return True if a building with the given name exists, otherwise false.
	 */
	Boolean isBuildingNameExists(String buildingName);
}