package com.valtech.bookmyseat.daoimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.valtech.bookmyseat.entity.Project;

@ExtendWith(MockitoExtension.class)
class ProjectDaoImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	private ProjectDaoImpl projectDaoImpl;

	@SuppressWarnings({ "unchecked" })
	@Test
	void testGetProjectByName() {
		Project project = new Project();
		project.setProjectId(1);
		project.setProjectName("Test Project");
		when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq("Test Project"))).thenReturn(project);
		Project result = projectDaoImpl.getProjectByName("Test Project");
		verify(jdbcTemplate).queryForObject(anyString(), any(RowMapper.class), eq("Test Project"));
		assertEquals(project, result);
	}

	@Test
	void testCreateProject() {
		Project project = new Project();
		project.setProjectName("Test Project");
		project.setCreatedDate(LocalDate.now());
		String expectedQuery = "INSERT INTO PROJECT (PROJECT_NAME, CREATED_DATE) VALUES (?, ?)";
		projectDaoImpl.createProject(project);
		assertNotNull(expectedQuery);
	}

	@SuppressWarnings("unchecked")
	@Test
	void testGetAllProjects() {
		Project project = new Project();
		project.setProjectId(1);
		project.setProjectName("ProjectA");
		Project project1 = new Project();
		project1.setProjectId(2);
		project1.setProjectName("ProjectB");
		List<Project> mockProjects = new ArrayList<>();
		mockProjects.add(project);
		mockProjects.add(project1);
		when(jdbcTemplate.query(eq("SELECT * FROM PROJECT"), any(BeanPropertyRowMapper.class)))
				.thenReturn(mockProjects);
		List<Project> result = projectDaoImpl.getAllProjects();
		assertEquals(2, result.size());
		assertEquals("ProjectA", result.get(0).getProjectName());
		assertEquals("ProjectB", result.get(1).getProjectName());
	}

	@Test
	void testDeleteProjectById() {
		int projectIdToDelete = 123;
		projectDaoImpl.deleteProjectById(projectIdToDelete);
		String expectedDeleteQuery = "DELETE FROM PROJECT WHERE PROJECT_ID = ?";
		verify(jdbcTemplate).update(expectedDeleteQuery, projectIdToDelete);
	}

	@Test
	void testUpdateProject() {
		Project project = new Project();
		project.setProjectName("New Project Name");
		project.setCreatedDate(LocalDate.now());
		project.setModifiedDate(LocalDate.now());
		project.setCreatedBy(123);
		project.setModifiedBy(123);
		int projectId = 123;
		projectDaoImpl.updateProject(project, projectId);
		verify(jdbcTemplate).update(
				"UPDATE PROJECT SET PROJECT_NAME = ?, MODIFIED_DATE = ?, MODIFIED_BY = ? WHERE PROJECT_ID = ?",
				project.getProjectName(), project.getModifiedDate(), project.getModifiedBy(), projectId);
	}
}