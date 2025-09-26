package com.valtech.team3.bookmyseatbackend.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.valtech.team3.bookmyseatbackend.dao.ShiftDetailsDAO;
import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;

@Repository
public class ShiftDetailsDAOImpl implements ShiftDetailsDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public class ShiftDetailsRowMapper implements RowMapper<ShiftDetails> {

		@Override
		public ShiftDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
			ShiftDetails shiftDetails = new ShiftDetails();
			shiftDetails.setId(rs.getInt("id"));
			shiftDetails.setShiftName(rs.getString("shift_name"));
			shiftDetails.setStartTime(rs.getTime("start_time").toLocalTime());
			shiftDetails.setEndTime(rs.getTime("end_time").toLocalTime());

			return shiftDetails;
		}
	}

	@Override
	public ShiftDetails getShiftDetailsById(int shiftId) {
		String selectQuery = "SELECT * FROM SHIFT_DETAILS WHERE ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new ShiftDetailsRowMapper(), shiftId);
	}

	@Override
	public List<ShiftDetails> getAllShiftDetails() {
		String selectQuery = "SELECT * FROM SHIFT_DETAILS";

		return jdbcTemplate.query(selectQuery, new ShiftDetailsRowMapper());
	}
}