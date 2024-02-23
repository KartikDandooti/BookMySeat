package com.valtech.bookmyseat.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDetailsOfUserForAdminReport {
	private String userName;
	private int userId;
	private int seatNumber;
	private String floorName;
	private Date startTime;
	private Date endTime;
	private Date startDate;
	private Date endDate;
	private String bookingType;
	private String teaCoffeeType;
	private String parkingType;
	private boolean lunch;
}
