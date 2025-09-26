package com.valtech.team3.bookmyseatbackend.service;

import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;

public interface ShiftDetailsService {

	/**
	 * Retrieves a list of all shift details.
	 *
	 * @return A list containing all shift details available in the system.
	 */
	List<ShiftDetails> getAllShiftDetails();
}