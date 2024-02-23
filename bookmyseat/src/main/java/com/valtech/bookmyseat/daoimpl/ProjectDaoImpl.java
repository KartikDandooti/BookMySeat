package com.valtech.bookmyseat.daoimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.valtech.bookmyseat.dao.ProjectDAO;
import com.valtech.bookmyseat.entity.Project;
import com.valtech.bookmyseat.mapper.ProjectRowMapper;

@Repository
public class ProjectDaoImpl implements ProjectDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Project getProjectById(int projectId) {
		String selectQuery = "SELECT * FROM PROJECT WHERE PROJECT_ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new ProjectRowMapper(), projectId);
	}

	@Override
	public Project getProjectByName(String projectName) {
		LOGGER.info("Getting project by Project Name");
		String selectQuery = "SELECT * FROM PROJECT WHERE PROJECT_NAME = ?";

		return jdbcTemplate.queryForObject(selectQuery, new BeanPropertyRowMapper<>(Project.class), projectName);
	}

	@Override
	public void createProject(Project project) {
		LOGGER.info("Creating a new Project");
		String insertQuery = "INSERT INTO PROJECT (PROJECT_NAME, CREATED_DATE, MODIFIED_DATE, CREATED_BY, MODIFIED_BY) VALUES (?,?,?,?,?)";
		jdbcTemplate.update(insertQuery, project.getProjectName(), project.getCreatedDate(), project.getModifiedDate(),
				project.getCreatedBy(), project.getModifiedBy());
	}

	@Override
	public List<Project> getAllProjects() {
		LOGGER.info("Retieving all projects from Database");
		String selectQuery = "SELECT * FROM PROJECT";

		return jdbcTemplate.query(selectQuery, new BeanPropertyRowMapper<>(Project.class));
	}

	@Override
	public void deleteProjectById(int projectId) {
		LOGGER.info("Deleting project with Id {}", projectId);
		String deleteQuery = "DELETE FROM PROJECT WHERE PROJECT_ID = ?";
		jdbcTemplate.update(deleteQuery, projectId);
	}

	@Override
	public void updateProject(Project project, int projectId) {
		LOGGER.info("Updating Project with Id {}", projectId);
		String updateQuery = "UPDATE PROJECT SET PROJECT_NAME = ?, MODIFIED_DATE = ?, MODIFIED_BY = ? WHERE PROJECT_ID = ?";
		jdbcTemplate.update(updateQuery, project.getProjectName(), project.getModifiedDate(), project.getModifiedBy(),
				projectId);
	}
}
