package com.valtech.bookmyseat.dao;

import java.sql.SQLException;
import java.util.List;

import com.valtech.bookmyseat.entity.Location;
import com.valtech.bookmyseat.exception.DataBaseAccessException;

/**
 * The LocationDAO interface represents data access operations for locations.
 * Implementations of this interface are responsible for saving location data to
 * a database.
 */
public interface LocationDAO {

	/**
	 * Saves the provided location data to the database.
	 *
	 * @param location The Location object to be saved.
	 * @throws SQLException If a database access error occurs.
	 */
	void save(Location location) throws DataBaseAccessException;

	/**
	 * 
	 * Lists the location present in the database
	 * 
	 * @return A list of Location objects representing all locations.
	 *
	 * @throws SQLException If a database access error occurs.
	 */
	List<Location> getAllLocation() throws DataBaseAccessException;

	/**
	 * Deletes a location with the specified location ID.
	 *
	 * @param locationId The ID of the location to be deleted.
	 */
	void deleteLocation(int locationId);

	/**
	 * Updates the location information for the location with the specified ID.
	 *
	 * @param location   The updated location object containing the new information.
	 * @param locationId The ID of the location to be updated.
	 */
	void updateLocation(Location location, int locationId);
}