package com.valtech.team3.bookmyseatbackend.service.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.valtech.team3.bookmyseatbackend.dao.ProjectDAO;
import com.valtech.team3.bookmyseatbackend.entities.Project;
import com.valtech.team3.bookmyseatbackend.exceptions.DataBaseAccessException;
import com.valtech.team3.bookmyseatbackend.models.ProjectModel;
import com.valtech.team3.bookmyseatbackend.service.impl.ProjectServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

	@Mock
	private ProjectDAO projectDAO;
	@InjectMocks
	private ProjectServiceImpl projectService;

	@Test
	void testGetAllProjects() {
		Project project1 = new Project();
		Project project2 = new Project();
		List<Project> expectedProjects = Arrays.asList(project1, project2);
		when(projectDAO.getAllProjects()).thenReturn(expectedProjects);

		List<Project> actualProjects = projectService.getAllProjects();

		assertEquals(expectedProjects, actualProjects);
	}

	@Test
	void testGetAllProjectNames() {
		List<String> expectedProjectNames = Arrays.asList("RMG", "Others");
		when(projectDAO.getAllProjectNames()).thenReturn(expectedProjectNames);

		List<String> actualProjectNames = projectService.getAllProjectNames();

		assertEquals(expectedProjectNames, actualProjectNames);
	}

	@Test
	void testCreateNewProject() {
		ProjectModel projectModel = new ProjectModel();
		projectModel.setProjectName("RMG");

		projectService.createNewProject(projectModel);

		verify(projectDAO).createProject(any(Project.class));
	}

	@Test
	void testUpdateProject() {
		ProjectModel projectModel = new ProjectModel();
		projectModel.setProjectName("Human Rescources");

		projectService.updateProject(projectModel);

		verify(projectDAO).updateProject(any(Project.class));
	}

	@Test
	void testUpdateProject_ProjectExists() {
		ProjectModel projectModel = new ProjectModel();
		projectModel.setProjectName("ExistingProject");

		when(projectDAO.isProjectExists(projectModel.getProjectName())).thenReturn(true);

		assertThrows(DataBaseAccessException.class, () -> projectService.updateProject(projectModel));

		verify(projectDAO, never()).updateProject(any());
	}

	@Test
	void testUpdateProject_RuntimeException() {
		ProjectModel projectModel = new ProjectModel();
		projectModel.setProjectName("NewProject");

		when(projectDAO.isProjectExists(projectModel.getProjectName())).thenThrow(new RuntimeException());

		assertThrows(DataBaseAccessException.class, () -> projectService.updateProject(projectModel));

		verify(projectDAO, never()).updateProject(any());
	}

	@Test
	void testGetAllProjects_RuntimeException() {
		when(projectDAO.getAllProjects()).thenThrow(new RuntimeException("Simulated DAO exception"));

		assertThrows(DataBaseAccessException.class, () -> projectService.getAllProjects());
	}

	@Test
	void testGetAllProjectNames_RuntimeException() {
		when(projectDAO.getAllProjectNames()).thenThrow(new RuntimeException("Simulated DAO exception"));

		assertThrows(DataBaseAccessException.class, () -> projectService.getAllProjectNames());
	}

	@Test
	void testCreateNewProject_RuntimeException() {
		ProjectModel projectModel = new ProjectModel();
		projectModel.setProjectName("New Project");

		doThrow(new RuntimeException()).when(projectDAO).createProject(any(Project.class));

		assertThrows(DataBaseAccessException.class, () -> projectService.createNewProject(projectModel));
	}
}