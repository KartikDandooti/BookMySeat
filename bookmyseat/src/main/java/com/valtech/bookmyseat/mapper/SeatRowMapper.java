package com.valtech.bookmyseat.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import com.valtech.bookmyseat.dao.FloorDAO;
import com.valtech.bookmyseat.entity.Seat;

public class SeatRowMapper implements RowMapper<Seat> {

	@Autowired
	private FloorDAO floorDAO;

	@Override
	public Seat mapRow(ResultSet rs, int rowNum) throws SQLException {
		Seat seat = new Seat();
		seat.setSeatId(rs.getInt("seat_id"));
		seat.setSeatNumber(rs.getInt("seat_number"));
		seat.setSeatStatus(false);
		seat.setFloor(floorDAO.getFloorById(rs.getInt("floor_id")));

		return seat;
	}
}
