package com.valtech.bookmyseat.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashBoardModel {
	private LocalDate date;
	private int seatsBooked;
	private int totalLunchBooked;
	private int totalTeaBooked;
	private int totalCoffeeBooked;
	private int totalParkingBooked;
	private int totalTwoWheelerParkingBooked;
	private int totalFourWheelerParkingBooked;
	private int totalDesktopBooked;
}
