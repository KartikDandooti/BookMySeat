package com.valtech.bookmyseat.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBookingHistoryModel {
	private LocalDate startDate;
	private LocalDate endDate;
	private String bookingType;
	private String parkingType;
	private String teaCoffeeType;
	private boolean bookingStatus;
	private boolean additionalDesktop;
	private boolean lunch;
	private int seatId;
	private String floorName;
	private String shiftName;
	private LocalTime startTime;
	private LocalTime endTime;
}