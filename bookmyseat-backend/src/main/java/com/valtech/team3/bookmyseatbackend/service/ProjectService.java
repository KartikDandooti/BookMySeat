package com.valtech.team3.bookmyseatbackend.service;

import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.Project;
import com.valtech.team3.bookmyseatbackend.models.ProjectModel;

public interface ProjectService {

	/**
	 * Retrieves a list of all projects.
	 *
	 * @return A list containing all projects available in the system.
	 */
	List<Project> getAllProjects();

	/**
	 * Retrieves the names of all projects from the database.
	 *
	 * @return A list containing the names of all projects.
	 */
	List<String> getAllProjectNames();

	/**
	 * Creates a new project based on the provided project model.
	 *
	 * @param projectModel The ProjectModel object containing the details of the new
	 *                     project.
	 */
	void createNewProject(ProjectModel projectModel);

	/**
	 * Updates an existing project based on the provided project model.
	 *
	 * @param projectModel The ProjectModel object containing the updated
	 *                     information for the project.
	 */
	void updateProject(ProjectModel projectModel);
}