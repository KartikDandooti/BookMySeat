package com.valtech.bookmyseat.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.valtech.bookmyseat.model.UserBookingHistoryModel;

public class UserHistoryBookingMapper implements RowMapper<UserBookingHistoryModel> {

	@Override
	public UserBookingHistoryModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserBookingHistoryModel userBookingHistory = new UserBookingHistoryModel();
		userBookingHistory.setStartDate(rs.getDate("start_date").toLocalDate());
		userBookingHistory.setEndDate(rs.getDate("end_date").toLocalDate());
		userBookingHistory.setBookingType((rs.getString("booking_type")) != null ? rs.getString("booking_type") : null);
		userBookingHistory.setParkingType((rs.getString("parking_type")) != null ? rs.getString("parking_type") : null);
		userBookingHistory
				.setTeaCoffeeType((rs.getString("tea_coffee_type")) != null ? rs.getString("tea_coffee_type") : null);
		userBookingHistory.setBookingStatus(rs.getBoolean("booking_status"));
		userBookingHistory.setAdditionalDesktop(rs.getBoolean("additional_desktop"));
		userBookingHistory.setLunch(rs.getBoolean("lunch"));
		userBookingHistory.setSeatId(rs.getInt("seat_id"));
		userBookingHistory.setFloorName(rs.getString("floor_name"));
		userBookingHistory.setShiftName(rs.getString("shift_name"));
		userBookingHistory.setStartTime(rs.getTime("start_time").toLocalTime());
		userBookingHistory.setEndTime(rs.getTime("end_time").toLocalTime());

		return userBookingHistory;
	}
}