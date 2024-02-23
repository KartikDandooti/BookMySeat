package com.valtech.bookmyseat.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import com.valtech.bookmyseat.dao.ProjectDAO;
import com.valtech.bookmyseat.dao.RoleDAO;
import com.valtech.bookmyseat.entity.User;

public class UserPrefernceMapper implements RowMapper<User> {

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private ProjectDAO projectDAO;

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setUserId(rs.getInt("user_id"));
		user.setEmailId(rs.getString("email_id"));
		user.setFirstName(rs.getString("first_name"));
		user.setLastName(rs.getString("last_name"));
		user.setPassword(rs.getString("password"));
		user.setPhoneNumber(rs.getLong("phone_number"));
		user.setRole(roleDAO.getUserRoleByRoleID(rs.getInt("role_id")));
		user.setProject(projectDAO.getProjectById(rs.getInt("project_id")));

		return user;
	}
}
