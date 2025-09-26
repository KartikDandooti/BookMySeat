package com.valtech.team3.bookmyseatbackend.models;

import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.Floor;
import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;

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
public class SeatBookingRequest {
	List<ShiftDetails> shiftDetails;
	List<Floor> floors;
}