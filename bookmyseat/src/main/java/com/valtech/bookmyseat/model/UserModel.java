package com.valtech.bookmyseat.model;

import java.time.LocalDate;
import java.util.List;

import com.valtech.bookmyseat.entity.ApprovalStatus;
import com.valtech.bookmyseat.entity.Booking;
import com.valtech.bookmyseat.entity.Project;
import com.valtech.bookmyseat.entity.Reserved;
import com.valtech.bookmyseat.entity.Restrain;
import com.valtech.bookmyseat.entity.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserModel {
	private int userId;
	private String emailId;
	private String firstName;
	private String lastName;
	private String password;
	private ApprovalStatus approvalStatus;
	private long phoneNumber;
	private LocalDate registeredDate;
	private LocalDate modifiedDate;
	private int createdBy;
	private int modifiedBy;
	private Role role;
	private Project project;
	private List<Booking> booking;
	private Reserved reserved;
	private Restrain restrain;
	private int projectId;

//	public User getUserDetails() {
//		return new User(userId, emailId, firstName, lastName, password, approvalStatus, phoneNumber, registeredDate,
//				modifiedDate, createdBy, modifiedBy, role, project, booking, reserved, restrain);
//	}
}
