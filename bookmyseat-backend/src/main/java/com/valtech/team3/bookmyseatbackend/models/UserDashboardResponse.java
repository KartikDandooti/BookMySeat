package com.valtech.team3.bookmyseatbackend.models;

import java.util.List;

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
public class UserDashboardResponse {
	private List<UserDashboardModel> bookings;
	private int attendanceCount;
	private User user;
}