package com.valtech.team3.bookmyseatbackend.models;

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
public class UserModel {
	private int employeeId;
	private String firstName;
	private String lastName;
	private String email;
	private long phoneNumber;
	private String password;
	private String projectName;
	private String registeredStatus;
	private int userId;
	private int projectId;

	public UserModel(int employeeId, String firstName, String lastName, String email, long phoneNumber, String password, String projectName) {
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.projectName = projectName;
	}

	public User getUserDetails() {
		return new User(employeeId, firstName, lastName, email, phoneNumber, password);
	}

	public void setUserDetails(User user) {
		this.employeeId = user.getEmployeeId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.phoneNumber = user.getPhoneNumber();
		this.password = user.getPassword();
	}
}