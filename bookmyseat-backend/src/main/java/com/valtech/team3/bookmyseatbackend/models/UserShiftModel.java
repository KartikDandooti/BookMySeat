package com.valtech.team3.bookmyseatbackend.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserShiftModel {
	private int userId;
	private List<Integer> shiftIds;
}