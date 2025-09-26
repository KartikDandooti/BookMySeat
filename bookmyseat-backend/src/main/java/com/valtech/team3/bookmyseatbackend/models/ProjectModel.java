package com.valtech.team3.bookmyseatbackend.models;

import java.time.LocalDate;

import com.valtech.team3.bookmyseatbackend.entities.Project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProjectModel {
	private int projectId;
	private String projectName;
	private LocalDate startDate;

	public Project getProjectDetails() {
		this.startDate = LocalDate.now();
		return new Project(projectId, projectName, startDate);
	}
}