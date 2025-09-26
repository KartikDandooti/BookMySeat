package com.valtech.team3.bookmyseatbackend.entities;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false, length = 5, unique = true)
	private int employeeId;
	@Column(nullable = false, length = 20)
	private String firstName;
	@Column(length = 20)
	private String lastName;
	@Column(nullable = false, length = 50, unique = true)
	private String email;
	private long phoneNumber;
	@Column(nullable = false)
	private String password;
	@Enumerated(EnumType.STRING)
	private RegisteredStatus registeredStatus;
	@Column(nullable = false, columnDefinition = "DATETIME")
	private LocalDateTime createdDate;
	@Column(columnDefinition = "DATETIME")
	private LocalDateTime modifiedDate;

	@ManyToOne(targetEntity = Project.class, cascade = { CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "projectId", referencedColumnName = "id", nullable = false)
	private Project project;

	@ManyToOne(targetEntity = Role.class, cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "roleId", referencedColumnName = "id", nullable = false)
	private Role role;

	public User(int id, int employeeId, String firstName, String lastName, String email, long phoneNumber,
			String password, RegisteredStatus status, LocalDateTime createdDate, Project project, Role role) {
		this.id = id;
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.registeredStatus = status;
		this.createdDate = createdDate;
		this.project = project;
		this.role = role;
	}

	public User(int employeeId, String firstName, String lastName, String email, long phoneNumber, String password) {
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
	}

	public User(int id, int employeeId, String firstName, String lastName, String email, long phoneNumber,
			String password, RegisteredStatus registeredStatus) {
		this.id = id;
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.registeredStatus = registeredStatus;
	}

	public User(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, employeeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;

		return Objects.equals(email, other.email) && employeeId == other.employeeId;
	}
}