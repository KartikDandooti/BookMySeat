package com.valtech.team3.bookmyseatbackend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.team3.bookmyseatbackend.dao.ProjectDAO;
import com.valtech.team3.bookmyseatbackend.entities.Project;
import com.valtech.team3.bookmyseatbackend.exceptions.DataBaseAccessException;
import com.valtech.team3.bookmyseatbackend.models.ProjectModel;
import com.valtech.team3.bookmyseatbackend.service.ProjectService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ProjectServiceImpl implements ProjectService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

	@Autowired
	private ProjectDAO projectDAO;

	@Override
	public List<Project> getAllProjects() {
		try {
			LOGGER.info("Retrieving all Projects from Database !");

			return projectDAO.getAllProjects();
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to get all projects !");
		}
	}

	@Override
	public List<String> getAllProjectNames() {
		try {
			LOGGER.info("Retrieving all Projects from Database !");

			return projectDAO.getAllProjectNames();
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to get all project names !");
		}
	}

	@Override
	public void createNewProject(ProjectModel projectModel) {
		try {
			boolean isProjectExist = projectDAO.isProjectExists(projectModel.getProjectName());
			if (!isProjectExist) {
				LOGGER.info("Creating new Project by the name: {}", projectModel.getProjectName());
				Project project = projectModel.getProjectDetails();
				project.setCreatedDate(LocalDateTime.now());
				projectDAO.createProject(project);
			} else {
				throw new DataBaseAccessException("Project already exists !");
			}
		} catch (RuntimeException e) {

			throw new DataBaseAccessException("Project already exists in Database !");
		}
	}

	@Override
	public void updateProject(ProjectModel projectModel) {
		try {
			boolean isProjectExist = projectDAO.isProjectExists(projectModel.getProjectName());
			if (!isProjectExist) {
				LOGGER.info("Updating existing project");
				Project project = projectModel.getProjectDetails();
				project.setModifiedDate(LocalDateTime.now());
				projectDAO.updateProject(project);
			} else {
				throw new DataBaseAccessException("Project already exists !");
			}
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Updated project already exists !");
		}
	}
}