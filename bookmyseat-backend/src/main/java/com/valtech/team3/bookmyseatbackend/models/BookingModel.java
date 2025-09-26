package com.valtech.team3.bookmyseatbackend.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.valtech.team3.bookmyseatbackend.entities.BookingRange;
import com.valtech.team3.bookmyseatbackend.entities.BookingStatus;
import com.valtech.team3.bookmyseatbackend.entities.User;

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
public class BookingModel {
	private int id;
	private String bookingRange;
	private LocalDate startDate;
	private LocalDate endDate;
	private LocalDate createdDate;
	private boolean bookingStatus;
	private int verificationCode;
	private User user;
	private int seatId;
	private int floorId;
	private boolean attendance;
	private LocalDate bookedDate;
	private boolean additionalDesktop;
	private boolean lunch;
	private boolean beverage;
	private boolean parking;
	private String vehicleType;
	private boolean allowCancel;
	private int shiftId;
	private int bookingId;
	private LocalDateTime modifiedDate;

	public BookingModel(BookingRange daily, LocalDate now, LocalDate plusDays, BookingStatus active, String string) {
	}
}