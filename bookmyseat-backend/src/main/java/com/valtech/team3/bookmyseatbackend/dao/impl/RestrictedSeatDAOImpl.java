package com.valtech.team3.bookmyseatbackend.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.valtech.team3.bookmyseatbackend.dao.ProjectDAO;
import com.valtech.team3.bookmyseatbackend.dao.RestrictedSeatDAO;
import com.valtech.team3.bookmyseatbackend.dao.UserDAO;
import com.valtech.team3.bookmyseatbackend.entities.RestrictedSeat;

@Repository
public class RestrictedSeatDAOImpl implements RestrictedSeatDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ProjectDAO projectDAO;

	public class RestrictedSeatRowMapper implements RowMapper<RestrictedSeat> {

		@Override
		public RestrictedSeat mapRow(ResultSet rs, int rowNum) throws SQLException {
			RestrictedSeat restrictedSeat = new RestrictedSeat();
			restrictedSeat.setId(rs.getInt("id"));
			restrictedSeat.setUser(rs.getInt("user_id") != 0 ? userDAO.getUserById(rs.getInt("user_id")) : null);
			restrictedSeat.setProject(rs.getInt("project_id") != 0 ? projectDAO.getProjectById(rs.getInt("project_id")) : null);

			return restrictedSeat;
		}
	}

	@Override
	public RestrictedSeat getRestrictedSeatById(int restrictedSeatId) {
		String selectQuery = "SELECT * FROM RESTRICTED_SEAT WHERE ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new RestrictedSeatRowMapper(), restrictedSeatId);
	}

	@Override
	public Integer createUserRestriction(int userId) {
		String insertQuery = "INSERT INTO RESTRICTED_SEAT (USER_ID,CREATED_DATE) VALUES (?,?)";
		jdbcTemplate.update(insertQuery, userId, LocalDateTime.now());

		return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
	}

	@Override
	public void removeRestriction(int restrictionId) {
		String deleteQuery = "DELETE FROM RESTRICTED_SEAT WHERE ID = ?";
		jdbcTemplate.update(deleteQuery, restrictionId);
	}

	@Override
	public Boolean isExistsRestrictionForUser(int userId) {
		String selectQuery = "SELECT COUNT(*) FROM RESTRICTED_SEAT WHERE USER_ID = ?";
		Integer count = jdbcTemplate.queryForObject(selectQuery, Integer.class, userId);

		return Objects.requireNonNull(count) > 0;
	}

	@Override
	public Boolean isExistsRestrictionForProject(int projectId) {
		String selectQuery = "SELECT COUNT(*) FROM RESTRICTED_SEAT WHERE PROJECT_ID = ?";
		Integer count = jdbcTemplate.queryForObject(selectQuery, Integer.class, projectId);

		return Objects.requireNonNull(count) > 0;
	}

	@Override
	public Integer createProjectRestriction(int projectId) {
		String insertQuery = "INSERT INTO RESTRICTED_SEAT (PROJECT_ID,CREATED_DATE) VALUES (?,?)";
		jdbcTemplate.update(insertQuery, projectId, LocalDateTime.now());

		return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
	}

	@Override
	public RestrictedSeat getRestrictedSeatByProjectId(int projectId) {
		String selectQuery = "SELECT * FROM RESTRICTED_SEAT WHERE PROJECT_ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new RestrictedSeatRowMapper(), projectId);
	}
}