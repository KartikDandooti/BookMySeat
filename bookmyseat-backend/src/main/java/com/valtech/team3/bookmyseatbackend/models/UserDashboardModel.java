package com.valtech.team3.bookmyseatbackend.models;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDashboardModel {
	private LocalDate startDate;
	private LocalDate endDate;
	private int seatNumber;
	private String floorName;
	private String bookingRange;
	private LocalTime startTime;
	private LocalTime endTime;
	private int employeeId;
	private int bookingId;
	private boolean allowCancel;
	private int verificationCode;
}