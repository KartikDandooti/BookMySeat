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
import com.valtech.team3.bookmyseatbackend.dao.RoleDAO;
import com.valtech.team3.bookmyseatbackend.dao.UserDAO;
import com.valtech.team3.bookmyseatbackend.entities.RegisteredStatus;
import com.valtech.team3.bookmyseatbackend.entities.User;

@Repository
public class UserDAOImpl implements UserDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private ProjectDAO projectDAO;

	public class UserRowMappper implements RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setEmployeeId(rs.getInt("employee_id"));
			user.setEmail(rs.getString("email"));
			user.setFirstName(rs.getString("first_name"));
			user.setLastName(rs.getString("last_name"));
			user.setPassword(rs.getString("password"));
			user.setPhoneNumber(rs.getLong("phone_number"));
			user.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
			user.setModifiedDate(
					rs.getTimestamp("modified_date") != null ? rs.getTimestamp("modified_date").toLocalDateTime()
							: null);
			user.setRegisteredStatus(RegisteredStatus.valueOf(rs.getString("registered_status")));
			user.setRole(roleDAO.getRoleById(rs.getInt("role_id")));
			user.setProject(projectDAO.getProjectById(rs.getInt("project_id")));

			return user;
		}
	}

	@Override
	public List<User> getAllUsers() {
		String selectQuery = "SELECT * FROM USER WHERE ROLE_ID = 2 OR ROLE_ID = 4";

		return jdbcTemplate.query(selectQuery, new UserRowMappper());
	}

	@Override
	public User getUserByEmail(String email) {
		String selectQuery = "SELECT * FROM USER WHERE EMAIL = ?";

		return jdbcTemplate.queryForObject(selectQuery, new UserRowMappper(), email);
	}

	@Override
	public void createUser(User user) {
		String insertQuery = "INSERT INTO USER (EMPLOYEE_ID, FIRST_NAME, LAST_NAME, EMAIL, PHONE_NUMBER, PASSWORD, CREATED_DATE, PROJECT_ID, ROLE_ID, REGISTERED_STATUS) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(insertQuery, user.getEmployeeId(), user.getFirstName(), user.getLastName(), user.getEmail(),
				user.getPhoneNumber(), user.getPassword(), user.getCreatedDate(), user.getProject().getId(),
				user.getRole().getId(), user.getRegisteredStatus().toString());
	}

	@Override
	public User getUserById(int userId) {
		String selectQuery = "SELECT * FROM USER WHERE ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new UserRowMappper(), userId);
	}

	@Override
	public User getUserByEmployeeId(int employeeId) {
		String selectQuery = "SELECT * FROM USER WHERE EMPLOYEE_ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new UserRowMappper(), employeeId);
	}

	@Override
	public Boolean isEmailExists(String email) {
		String selectQuery = "SELECT COUNT(*) FROM USER WHERE EMAIL = ?";
		Integer count = jdbcTemplate.queryForObject(selectQuery, Integer.class, email);

		return Objects.requireNonNull(count) > 0;
	}

	@Override
	public Boolean isEmployeeIdExists(int employeeId) {
		String selectQuery = "SELECT COUNT(*) FROM USER WHERE EMPLOYEE_ID = ?";
		Integer count = jdbcTemplate.queryForObject(selectQuery, Integer.class, employeeId);

		return Objects.requireNonNull(count) > 0;
	}

	@Override
	public List<User> getAdminUsers() {
		String selectQuery = "SELECT U.* FROM USER U JOIN ROLE R ON U.ROLE_ID = R.ID WHERE R.ROLE_NAME IN ('ADMIN', 'ADMIN/EMPLOYEE')";

		return jdbcTemplate.query(selectQuery, new UserRowMappper());
	}

	@Override
	public List<User> getUsersByStatus(RegisteredStatus status) {
		String selectQuery = "SELECT * FROM user WHERE registered_status = ? AND role_id IN(2,4)";
		return jdbcTemplate.query(selectQuery, new UserRowMappper(), status.name());
	}

	@Override
	public void updateUserRegistrationStatus(int id, RegisteredStatus registrationStatus) {
		String updateQuery = "UPDATE USER SET REGISTERED_STATUS = ? WHERE ID = ?";
		jdbcTemplate.update(updateQuery, registrationStatus.toString(), id);
	}

	@Override
	public void updateUserPassword(int id, String password) {
		String changePasswordQuery = "UPDATE USER SET PASSWORD=?, MODIFIED_DATE=NOW() WHERE ID=?";
		jdbcTemplate.update(changePasswordQuery, password, id);
	}

	@Override
	public void updateUserPhoneNumber(int id, long phoneNumber) {
		String updatePhoneNumberQuery = "UPDATE USER SET PHONE_NUMBER=?, MODIFIED_DATE=NOW() WHERE id=?";
		jdbcTemplate.update(updatePhoneNumberQuery, phoneNumber, id);
	}

	@Override
	public void updateUserProject(int userId, int projectId) {
		String updateQuery = "UPDATE USER SET PROJECT_ID = ? WHERE ID = ?";
		jdbcTemplate.update(updateQuery, projectId, userId);
	}

	@Override
	public User getUserByBookingId(int bookingId) {
		String selectQuery = "SELECT * FROM USER U JOIN BOOKING B ON B.USER_ID=U.ID WHERE B.ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new UserRowMappper(), bookingId);
	}
	
	@Override
	public Integer getLastInsertedUserId() {
	    String selectQuery = "SELECT LAST_INSERT_ID()";
	    
	    return jdbcTemplate.queryForObject(selectQuery, Integer.class);
	}

}