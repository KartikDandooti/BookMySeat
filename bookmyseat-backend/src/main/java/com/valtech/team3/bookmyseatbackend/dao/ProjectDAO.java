package com.valtech.team3.bookmyseatbackend.dao;

import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.Project;

/**
 * This is an interface for the ProjectDAO class.
 */
public interface ProjectDAO {

	/**
	 * Creates a new project with the provided details.
	 *
	 * @param project The project object containing the details of the project to be
	 *                created.
	 */
	void createProject(Project project);

	/**
	 * Retrieves a project by its unique ID.
	 *
	 * @param id The unique ID of the project to retrieve.
	 * @return The project corresponding to the provided ID, or null if not found.
	 */
	Project getProjectById(int id);

	/**
	 * Retrieves a list of all projects.
	 *
	 * @return A list containing all projects in the database.
	 */
	List<Project> getAllProjects();

	/**
	 * Retrieves the project with the specified project name from the database.
	 *
	 * @param projectName The name of the project to retrieve.
	 * @return The project with the specified name, or null if not found.
	 */
	Project getProjectByProjectName(String projectName);

	/**
	 * Retrieves the names of all projects from the database.
	 *
	 * @return A list containing the names of all projects.
	 */
	List<String> getAllProjectNames();

	/**
	 * Updates an existing project with new information.
	 *
	 * @param project The Project object containing the updated information for the
	 *                project.
	 */
	void updateProject(Project project);

	/**
	 * Checks if a project with the given project name already exists.
	 *
	 * @param projectName The name of the project to check for existence.
	 * @return {@code true} if the project exists, {@code false} otherwise.
	 */
	Boolean isProjectExists(String projectName);
}