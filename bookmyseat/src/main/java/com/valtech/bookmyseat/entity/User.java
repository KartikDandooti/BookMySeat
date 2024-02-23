package com.valtech.bookmyseat.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	private int userId;

	@Column(unique = true, nullable = false, length = 50)
	private String emailId;

	@Column(nullable = false, length = 20)
	private String firstName;

	@Column(nullable = false, length = 20)
	private String lastName;

	@Column(nullable = false, length = 80)
	private String password;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ApprovalStatus approvalStatus;

	@Column(nullable = false)
	private long phoneNumber;

	@Column(nullable = false)
	private LocalDate registeredDate;

	@Column(nullable = false)
	private LocalDate modifiedDate;

	@Column(nullable = false)
	private int createdBy;

	@Column(nullable = false)
	private int modifiedBy;

	@ManyToOne(targetEntity = Role.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "roleId", nullable = false)
	private Role role;

	@ManyToOne(targetEntity = Project.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "projectId")
	private Project project;

	@OneToMany(targetEntity = Booking.class, cascade = CascadeType.ALL, mappedBy = "user")
	private List<Booking> booking;

	@OneToOne(targetEntity = Reserved.class, cascade = CascadeType.ALL, mappedBy = "user")
	private Reserved reserved;

	@ManyToOne(targetEntity = Restrain.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "restrainId")
	private Restrain restrain;

	@OneToMany(targetEntity = Otp.class, cascade = CascadeType.ALL, mappedBy = "user")
	private List<Otp> otp;
}