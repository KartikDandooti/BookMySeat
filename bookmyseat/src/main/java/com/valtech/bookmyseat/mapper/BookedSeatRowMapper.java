package com.valtech.bookmyseat.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.valtech.bookmyseat.model.BookedSeatModel;

public class BookedSeatRowMapper implements RowMapper<BookedSeatModel> {

	@Override
	public BookedSeatModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		BookedSeatModel bookedSeatModel = new BookedSeatModel();
		bookedSeatModel.setBookingStatus(rs.getBoolean("booking_status"));
		bookedSeatModel.setEndDate(rs.getDate("end_date").toLocalDate());
		bookedSeatModel.setStartDate(rs.getDate("start_date").toLocalDate());
		bookedSeatModel.setFloorId(rs.getInt("floor_id"));
		bookedSeatModel.setSeatNumber(rs.getInt("seat_number"));
		bookedSeatModel.setUserId(rs.getInt("user_id"));

		return bookedSeatModel;
	}
}