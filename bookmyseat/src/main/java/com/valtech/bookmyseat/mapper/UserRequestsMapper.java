package com.valtech.bookmyseat.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.valtech.bookmyseat.model.UserRequestsModel;

public class UserRequestsMapper implements RowMapper<UserRequestsModel> {

	@Override
	public UserRequestsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserRequestsModel userRequestsModel = new UserRequestsModel();
		userRequestsModel.setName(rs.getString("name"));
		userRequestsModel.setEmailID(rs.getString("email_id"));
		userRequestsModel.setUserID(rs.getInt("user_id"));
		userRequestsModel.setRegsiterdDate(rs.getDate("registered_date").toLocalDate());
		userRequestsModel.setApprovalStatus(rs.getString("approval_status"));

		return userRequestsModel;
	}
}
