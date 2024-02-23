package com.valtech.bookmyseat.model;

import java.time.LocalDate;

import com.valtech.bookmyseat.entity.Project;

public class ProjectModel {
	private String projectName;
	private LocalDate createdDate = LocalDate.now();
	private LocalDate modifiedDate = LocalDate.now();
	private int createdBy;
	private int modifiedBy;

	public String getProjectName() {
		return projectName;
	}

	public Project getProjectDetails() {
		Project project = new Project();
		project.setProjectName(projectName);
		project.setCreatedDate(createdDate);
		project.setModifiedDate(modifiedDate);
		project.setCreatedBy(createdBy);
		project.setModifiedBy(modifiedBy);

		return project;
	}
}
