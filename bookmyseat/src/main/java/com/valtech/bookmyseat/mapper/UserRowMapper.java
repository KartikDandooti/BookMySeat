package com.valtech.bookmyseat.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.valtech.bookmyseat.entity.ApprovalStatus;
import com.valtech.bookmyseat.entity.Project;
import com.valtech.bookmyseat.entity.Role;
import com.valtech.bookmyseat.entity.User;

public class UserRowMapper implements RowMapper<User> {

	@Nullable
	private LocalDate getLocalDateOrNull(ResultSet rs, String columnName) throws SQLException {
		Date sqlDate = rs.getDate(columnName);
		return Objects.nonNull(sqlDate) ? sqlDate.toLocalDate() : null;
	}

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		Role role = new Role();
		role.setRoleId(rs.getInt("role_id"));
		role.setRoleName(rs.getString("role_name"));
		User user = new User();
		user.setUserId(rs.getInt("user_id"));
		user.setEmailId(rs.getString("email_id"));
		user.setFirstName(rs.getString("first_name"));
		user.setLastName(rs.getString("last_name"));
		user.setPassword(rs.getString("password"));
		user.setApprovalStatus(ApprovalStatus.valueOf(rs.getString("approval_status")));
		user.setPhoneNumber(rs.getLong("phone_number"));
		user.setRegisteredDate(getLocalDateOrNull(rs, "registered_date"));
		user.setModifiedDate(getLocalDateOrNull(rs, "modified_date"));
		user.setCreatedBy(rs.getInt("created_by"));
		user.setModifiedBy(rs.getInt("modified_by"));
		Project project = new Project();
		project.setProjectId(rs.getInt("project_id"));
		project.setProjectName(rs.getString("project_name"));
		user.setProject(project);
		user.setRole(role);

		return user;
	}
}