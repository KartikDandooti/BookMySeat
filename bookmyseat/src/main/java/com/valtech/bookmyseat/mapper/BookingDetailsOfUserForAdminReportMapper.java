package com.valtech.bookmyseat.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.valtech.bookmyseat.model.BookingDetailsOfUserForAdminReport;

public class BookingDetailsOfUserForAdminReportMapper implements RowMapper<BookingDetailsOfUserForAdminReport> {

	@Override
	public BookingDetailsOfUserForAdminReport mapRow(ResultSet rs, int rowNum) throws SQLException {
		BookingDetailsOfUserForAdminReport bookingDetails = new BookingDetailsOfUserForAdminReport();
		bookingDetails.setUserName(rs.getString("user_name"));
		bookingDetails.setUserId(rs.getInt("user_id"));
		bookingDetails.setSeatNumber(rs.getInt("seat_number"));
		bookingDetails.setFloorName(rs.getString("floor_name"));
		bookingDetails.setStartTime(rs.getTime("start_time"));
		bookingDetails.setEndTime(rs.getTime("end_time"));
		bookingDetails.setStartDate(rs.getDate("start_date"));
		bookingDetails.setEndDate(rs.getDate("end_date"));
		bookingDetails.setBookingType(rs.getString("booking_type"));
		bookingDetails
				.setTeaCoffeeType(rs.getString("tea_coffee_type") != null ? rs.getString("tea_coffee_type") : null);
		bookingDetails.setParkingType(rs.getString("parking_type") != null ? rs.getString("parking_type") : null);
		bookingDetails.setLunch(rs.getBoolean("lunch"));

		return bookingDetails;
	}
}