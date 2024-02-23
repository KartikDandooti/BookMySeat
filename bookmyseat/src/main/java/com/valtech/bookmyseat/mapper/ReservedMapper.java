package com.valtech.bookmyseat.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.valtech.bookmyseat.entity.Floor;
import com.valtech.bookmyseat.entity.Reserved;
import com.valtech.bookmyseat.entity.Seat;
import com.valtech.bookmyseat.entity.User;

public class ReservedMapper implements RowMapper<Reserved> {

	@Override
	public Reserved mapRow(ResultSet rs, int rowNum) throws SQLException {
		Reserved reserved = new Reserved();
		reserved.setReservedStatus(rs.getBoolean("reserved_status"));
		reserved.setReservedId(rs.getInt("reserved_id"));
		User user = new User();
		user.setUserId(rs.getInt("user_id"));
		user.setFirstName(rs.getString("first_name"));
		Seat seat = new Seat();
		seat.setSeatId(rs.getInt("seat_id"));
		seat.setSeatNumber(rs.getInt("seat_number"));
		Floor floor = new Floor();
		floor.setFloorId(rs.getInt("floor_id"));
		reserved.setUser(user);
		seat.setFloor(floor);
		reserved.setSeat(seat);

		return reserved;
	}
}
