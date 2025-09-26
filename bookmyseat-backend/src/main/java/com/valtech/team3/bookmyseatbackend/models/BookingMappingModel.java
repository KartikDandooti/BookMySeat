package com.valtech.team3.bookmyseatbackend.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
public class BookingMappingModel {
	private int id;
	private boolean attendance;
	private LocalDate bookedDate;
	private boolean additionalDesktop;
	private boolean beverage;
	private boolean lunch;
	private boolean parking;
	private int shiftId;
	private String vehicleType;
	private LocalDateTime modifiedDate;
	private int bookingId;
}