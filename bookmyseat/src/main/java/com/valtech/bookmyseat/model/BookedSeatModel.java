package com.valtech.bookmyseat.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookedSeatModel {

	private int seatNumber;
	private int floorId;
	private int userId;
	private LocalDate startDate;
	private LocalDate endDate;
	private boolean bookingStatus;
}
