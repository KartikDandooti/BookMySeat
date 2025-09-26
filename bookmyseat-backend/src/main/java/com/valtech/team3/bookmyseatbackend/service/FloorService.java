package com.valtech.team3.bookmyseatbackend.service;

import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.Floor;

public interface FloorService {

	/**
	 * Retrieves a list of all floors from the data source.
	 *
	 * @return a list containing all floor objects available in the data source
	 */
	List<Floor> getAllFloors();
}