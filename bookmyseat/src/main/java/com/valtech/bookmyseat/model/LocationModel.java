package com.valtech.bookmyseat.model;

import java.time.LocalDate;

import com.valtech.bookmyseat.entity.Location;

public class LocationModel {
	private String locationName;
	private int locationId;
	@SuppressWarnings("unused")
	private LocalDate createdDate;

	public Location getLocationDetails() {
		Location location = new Location();
		location.setLocationId(locationId);
		location.setLocationName(locationName);

		return location;
	}
}