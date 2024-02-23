package com.valtech.bookmyseat.dao;

import java.util.List;

import com.valtech.bookmyseat.entity.Project;

/**
 * This interface defines the operations that can be performed on a Project data
 * access object (DAO). It provides methods for creating, retrieving, updating,
 * and deleting projects.
 */
public interface ProjectDAO {

	/**
	 * Creates a new project in the data store.
	 * 
	 * @param project The project object to be created.
	 */
	void createProject(Project project);

	/**
	 * Retrieves a list of all projects from the data store.
	 * 
	 * @return A list of Project objects representing all projects.
	 */
	List<Project> getAllProjects();

	/**
	 * Retrieves a project by its name from the data store.
	 * 
	 * @param projectName The name of the project to retrieve.
	 * @return The Project object with the specified name, or null if not found.
	 */
	Project getProjectByName(String projectName);

	/**
	 * Deletes a project from the data store by its ID.
	 * 
	 * @param projectId The ID of the project to delete.
	 */
	void deleteProjectById(int projectId);

	/**
	 * Updates an existing project in the data store.
	 * 
	 * @param project   The updated project object.
	 * @param projectId The ID of the project to update.
	 */
	void updateProject(Project project, int projectId);

	/**
	 * Retrieves the project with the specified project ID.
	 * 
	 * @param projectId The ID of the project to retrieve.
	 * @return The project object corresponding to the provided project ID, or null
	 *         if no project is found.
	 */
	Project getProjectById(int projectId);
}
