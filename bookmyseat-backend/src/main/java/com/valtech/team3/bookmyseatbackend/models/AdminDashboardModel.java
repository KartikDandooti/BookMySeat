package com.valtech.team3.bookmyseatbackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardModel {
	private int totalAttendees;
	private int totalSeatsBooked;
	private int totalParkingSpaceBooked;
	private int fourWheelerParkingSpaceBooked;
	private int twoWheelerParkingSpaceBooked;
	private int totalEmployeesOptedForLunch;
	private int totalEmployeesOptedForDesktop;
}