package com.valtech.bookmyseat.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int projectId;

	@Column(unique = true, nullable = false, length = 30)
	private String projectName;

	@Column(nullable = false)
	private LocalDate createdDate;

	@Column(nullable = false)
	private LocalDate modifiedDate;

	@Column(nullable = false)
	private int createdBy;

	@Column(nullable = false)
	private int modifiedBy;

	@OneToMany(targetEntity = User.class, cascade = CascadeType.ALL, mappedBy = "project")
	private List<User> user;
}
