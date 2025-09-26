package com.valtech.team3.bookmyseatbackend.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.valtech.team3.bookmyseatbackend.dao.UserShiftsDAO;
import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;

@Repository
public class UserShiftsDAOImpl implements UserShiftsDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public class UserShiftsRowMapper implements RowMapper<ShiftDetails> {

		@Override
		public ShiftDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
			ShiftDetails shiftDetails = new ShiftDetails();
			shiftDetails.setId(rs.getInt("SHIFT_ID"));
			shiftDetails.setShiftName(rs.getString("SHIFT_NAME"));
			Time startTime = rs.getTime("START_TIME");
			Time endTime = rs.getTime("END_TIME");
			shiftDetails.setStartTime(startTime != null ? startTime.toLocalTime() : null);
			shiftDetails.setEndTime(endTime != null ? endTime.toLocalTime() : null);

			return shiftDetails;
		}
	}

	@Override
	public Boolean doesUserShiftExist(int userId, int shiftId) {
		String selectQuery = "SELECT COUNT(*) FROM USER_SHIFTS WHERE USER_ID = ? AND SHIFT_ID = ?";
		int count = jdbcTemplate.queryForObject(selectQuery, Integer.class, userId, shiftId);

		return count > 0;
	}

	@Override
	public void addUserShift(int userId, int shiftId) {
		String insertQuery = "INSERT INTO USER_SHIFTS (USER_ID, SHIFT_ID, CREATED_DATE) VALUES (?, ?, NOW())";
		jdbcTemplate.update(insertQuery, userId, shiftId);
	}

	@Override
	public void deleteUserShifts(int userId) {
		String deleteQuery = "DELETE FROM USER_SHIFTS WHERE USER_ID = ?";
		jdbcTemplate.update(deleteQuery, userId);
	}

	@Override
	public List<ShiftDetails> getAllShiftDetailsByUserId(int userId) {
		String selectQuery = "SELECT SD.ID AS SHIFT_ID, SD.SHIFT_NAME, SD.START_TIME, SD.END_TIME " + "FROM USER_SHIFTS US " + "JOIN SHIFT_DETAILS SD " + "ON US.SHIFT_ID = SD.ID "
		      + "WHERE US.USER_ID = ?";

		return jdbcTemplate.query(selectQuery, new UserShiftsRowMapper(), userId);
	}
}