package com.valtech.bookmyseat.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.valtech.bookmyseat.model.UserModifyBooking;

public class UpdateSeatBookingMapper implements RowMapper<UserModifyBooking> {

	@Override
	public UserModifyBooking mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserModifyBooking userModifyBooking = new UserModifyBooking();
		userModifyBooking.setUserId(rs.getInt("userId"));
		userModifyBooking.setUserName(rs.getString("userName"));
		userModifyBooking.setBookingStartDate(rs.getDate("booking_startDate").toLocalDate());
		userModifyBooking.setBookingEndDate(rs.getDate("booking_endDate").toLocalDate());
		userModifyBooking.setShiftName(rs.getString("shiftName"));
		userModifyBooking.setSeatNumber(rs.getInt("seatNumber"));
		userModifyBooking.setFloorName(rs.getString("floorName"));
		userModifyBooking.setBookingStatus(rs.getBoolean("bookingStatus"));
		userModifyBooking.setUserEmail(rs.getString("userEmail"));

		return userModifyBooking;
	}
}