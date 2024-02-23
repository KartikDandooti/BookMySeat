package com.valtech.bookmyseat.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestsModel {
	private int userID;
	private String name;
	private String emailID;
	private LocalDate regsiterdDate;
	private String approvalStatus;
}
