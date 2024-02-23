package com.valtech.bookmyseat.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.valtech.bookmyseat.model.BookingDTO;

public class BookingDTORowMapper implements RowMapper<BookingDTO> {

	@Override
	public BookingDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		BookingDTO booking = new BookingDTO();
		booking.setBookingStartDate(rs.getDate("booking_start_date").toLocalDate());
		booking.setBookingEndDate(rs.getDate("booking_end_date").toLocalDate());
		booking.setSeatId(rs.getInt("seat_id"));
		booking.setFloorId(rs.getInt("floor_id"));

		return booking;
	}
}
