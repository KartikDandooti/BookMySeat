package com.valtech.team3.bookmyseatbackend.models;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HolidayModel {
	private int id;
	private String holidayName;
	private LocalDate holidayDate;
}