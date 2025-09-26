package com.valtech.team3.bookmyseatbackend.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.valtech.team3.bookmyseatbackend.dao.ProjectDAO;
import com.valtech.team3.bookmyseatbackend.entities.Project;

@Repository
public class ProjectDAOImpl implements ProjectDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public class ProjectRowMapper implements RowMapper<Project> {

		@Override
		public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
			Project project = new Project();
			project.setId(rs.getInt("id"));
			project.setProjectName(rs.getString("project_name"));
			project.setStartDate(rs.getDate("start_date").toLocalDate());
			project.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
			project.setModifiedDate(rs.getTimestamp("modified_date") != null ? rs.getTimestamp("modified_date").toLocalDateTime() : null);

			return project;
		}
	}

	@Override
	public List<Project> getAllProjects() {
		String selectQuery = "SELECT * FROM PROJECT";

		return jdbcTemplate.query(selectQuery, new ProjectRowMapper());
	}

	@Override
	public void createProject(Project project) {
		String insertQuery = "INSERT INTO PROJECT (PROJECT_NAME, START_DATE, CREATED_DATE) VALUES (?, ?, ?)";
		jdbcTemplate.update(insertQuery, project.getProjectName(), project.getStartDate(), project.getCreatedDate());
	}

	@Override
	public void updateProject(Project project) {
		String updateQuery = "UPDATE PROJECT SET PROJECT_NAME = ?, MODIFIED_DATE = ? WHERE ID = ?";
		jdbcTemplate.update(updateQuery, project.getProjectName(), project.getModifiedDate(), project.getId());
	}

	@Override
	public Project getProjectById(int id) {
		String selectQuery = "SELECT * FROM PROJECT WHERE ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new ProjectRowMapper(), id);
	}

	@Override
	public Project getProjectByProjectName(String projectName) {
		String selectQuery = "SELECT * FROM PROJECT WHERE PROJECT_NAME = ?";

		return jdbcTemplate.queryForObject(selectQuery, new ProjectRowMapper(), projectName);
	}

	@Override
	public List<String> getAllProjectNames() {
		String selectQuery = "SELECT PROJECT_NAME FROM PROJECT";

		return jdbcTemplate.queryForList(selectQuery, String.class);
	}

	@Override
	public Boolean isProjectExists(String projectName) {
		String selectQuery = "SELECT COUNT(*) FROM PROJECT WHERE PROJECT_NAME = ?";
		Integer count = jdbcTemplate.queryForObject(selectQuery, Integer.class, projectName);

		return Objects.requireNonNull(count) > 0;
	}
}