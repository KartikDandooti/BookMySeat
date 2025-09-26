package com.valtech.team3.bookmyseatbackend.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false, length = 50)
	private String projectName;
	@Column(nullable = false)
	private LocalDate startDate;
	@Column(nullable = false, columnDefinition = "DATETIME")
	private LocalDateTime createdDate;
	@Column(columnDefinition = "DATETIME")
	private LocalDateTime modifiedDate;

	public Project(int id, String projectName, LocalDate startDate, LocalDateTime createdDate) {
		this.id = id;
		this.projectName = projectName;
		this.startDate = startDate;
		this.createdDate = createdDate;
	}

	public Project(int projectId, String projectName, LocalDate startDate) {
		this.id = projectId;
		this.projectName = projectName;
		this.startDate = startDate;
	}
}