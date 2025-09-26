package com.valtech.team3.bookmyseatbackend.dao.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.valtech.team3.bookmyseatbackend.dao.impl.ProjectDAOImpl;
import com.valtech.team3.bookmyseatbackend.entities.Project;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class ProjectDAOImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;
	@InjectMocks
	private ProjectDAOImpl projectDAO;

	@Test
	void testMapRow() throws SQLException {
		ResultSet resultSet = mock(ResultSet.class);
		LocalDateTime currentDate = LocalDateTime.now();
		when(resultSet.getInt("id")).thenReturn(1);
		when(resultSet.getString("project_name")).thenReturn("Sample Project");
		when(resultSet.getDate("start_date")).thenReturn(java.sql.Date.valueOf(LocalDate.now()));
		when(resultSet.getTimestamp("created_date")).thenReturn(Timestamp.valueOf(currentDate));
		when(resultSet.getTimestamp("modified_date")).thenReturn(Timestamp.valueOf(currentDate));

		ProjectDAOImpl daoImpl = new ProjectDAOImpl();
		ProjectDAOImpl.ProjectRowMapper projectRowMapper = daoImpl.new ProjectRowMapper();
		Project project = projectRowMapper.mapRow(resultSet, 1);

		assertEquals(1, project.getId());
		assertEquals("Sample Project", project.getProjectName());
		assertEquals(LocalDate.now(), project.getStartDate());
		assertEquals(currentDate, project.getCreatedDate());
		assertEquals(currentDate, project.getModifiedDate());
	}

	@Test
	void testGetAllProjects() {
		List<Project> expectedProjects = Collections.singletonList(createSampleProject());
		String selectQuery = "SELECT * FROM PROJECT";

		when(jdbcTemplate.query(eq(selectQuery), any(RowMapper.class))).thenReturn(expectedProjects);

		List<Project> actualProjects = projectDAO.getAllProjects();

		assertEquals(expectedProjects, actualProjects);
	}

	@Test
	void testCreateProject() {
		Project project = createSampleProject();
		String insertQuery = "INSERT INTO PROJECT (PROJECT_NAME, START_DATE, CREATED_DATE) VALUES (?, ?, ?)";

		projectDAO.createProject(project);

		verify(jdbcTemplate).update(eq(insertQuery), any(), any(), any());
	}

	@Test
	void testUpdateProject() {
		Project project = createSampleProject();
		int projectId = 1;
		String updateQuery = "UPDATE PROJECT SET PROJECT_NAME = ?, MODIFIED_DATE = ? WHERE ID = ?";

		projectDAO.updateProject(project);

		verify(jdbcTemplate).update(eq(updateQuery), any(), any(), eq(projectId));
	}

	@Test
	void testGetProjectById() {
		int projectId = 1;
		Project expectedProject = createSampleProject();
		String selectQuery = "SELECT * FROM PROJECT WHERE ID = ?";
		when(jdbcTemplate.queryForObject(eq(selectQuery), any(RowMapper.class), eq(projectId))).thenReturn(expectedProject);

		Project actualProject = projectDAO.getProjectById(projectId);

		assertEquals(expectedProject, actualProject);
	}

	@Test
	void testGetProjectByProjectName() {
		String projectName = "RMG";
		Project expectedProject = createSampleProject();
		String selectQuery = "SELECT * FROM PROJECT WHERE PROJECT_NAME = ?";
		when(jdbcTemplate.queryForObject(eq(selectQuery), any(RowMapper.class), eq(projectName))).thenReturn(expectedProject);

		Project actualProject = projectDAO.getProjectByProjectName(projectName);

		assertEquals(expectedProject, actualProject);
	}

	@Test
	void testGetAllProjectNames() {
		List<String> expectedProjectNames = Arrays.asList("RMG", "Others");
		String selectQuery = "SELECT PROJECT_NAME FROM PROJECT";
		when(jdbcTemplate.queryForList(eq(selectQuery), eq(String.class))).thenReturn(expectedProjectNames);

		List<String> actualProjectNames = projectDAO.getAllProjectNames();

		assertEquals(expectedProjectNames, actualProjectNames);
	}

	private Project createSampleProject() {
		Project project = new Project();
		project.setId(1);
		project.setProjectName("RMG");
		project.setStartDate(LocalDate.now());
		project.setCreatedDate(LocalDateTime.now());
		project.setModifiedDate(LocalDateTime.now());

		return project;
	}

	@Test
	void testIsProjectExists() {
		String projectName = "Test Project";

		when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(projectName))).thenReturn(1);

		boolean projectExists = projectDAO.isProjectExists(projectName);

		assertTrue(projectExists);
		verify(jdbcTemplate).queryForObject(anyString(), eq(Integer.class), eq(projectName));
	}
}