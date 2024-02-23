package com.valtech.bookmyseat.model;

import java.time.LocalDate;

import com.valtech.bookmyseat.entity.BookingType;
import com.valtech.bookmyseat.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingModel {
	private int userId;
	private String userName;
	private BookingType bookingType;
	private Boolean parkingOpted;
	private String parkingType;
	private Boolean lunch;
	private Boolean teaCoffee;
	private String teaCoffeeType;
	private Boolean additionalDesktop;
	private LocalDate startDate;
	private LocalDate endDate;
	private int floorId;
	private int seatNumber;
	private int shiftId;
	private User user;
	private Boolean markedStatus;
	private LocalDate bookingDate;
}
