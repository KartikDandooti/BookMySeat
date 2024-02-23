package com.valtech.bookmyseat.service;

import java.util.List;

import com.valtech.bookmyseat.entity.Seat;
import com.valtech.bookmyseat.model.BookingModel;

public interface SeatService {

	/**
	 * Retrieves a list of seats from the data source based on the specified floor
	 * identifier.
	 *
	 * @param floorId the unique identifier of the floor for which to retrieve seats
	 * @return a list containing seat objects associated with the specified floor
	 *         identifier
	 */
	List<Seat> getAvailableSeatsByFloorOnDate(BookingModel bookingModel);
}