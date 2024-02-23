package com.valtech.bookmyseat.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatModel {
	private int seatId;
	private boolean seatStatus;
	private int floorId;
	private int reservedId;
}