package com.valtech.bookmyseat.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.valtech.bookmyseat.entity.BookingType;
import com.valtech.bookmyseat.model.BookingModel;

public class BookingDetailsMapper implements RowMapper<BookingModel> {

	@Override
	public BookingModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		BookingModel bookingDetails = new BookingModel();
		bookingDetails.setUserId(rs.getInt("user_id"));
		bookingDetails.setUserName(rs.getString("user_name"));
		bookingDetails.setParkingOpted(rs.getBoolean("parking_opted"));
		bookingDetails.setLunch(rs.getBoolean("lunch"));
		bookingDetails.setTeaCoffee(rs.getBoolean("tea_coffee"));
		bookingDetails.setAdditionalDesktop(rs.getBoolean("additional_desktop"));
		bookingDetails.setStartDate(rs.getDate("start_date").toLocalDate());
		bookingDetails.setEndDate(rs.getDate("end_date").toLocalDate());
		bookingDetails.setBookingType(BookingType.valueOf(rs.getString("booking_type")));
		bookingDetails.setParkingType(rs.getString("parking_type") != null ? rs.getString("parking_type") : null);
		bookingDetails
				.setTeaCoffeeType(rs.getString("tea_coffee_type") != null ? rs.getString("tea_coffee_type") : null);

		return bookingDetails;
	}
}
