package com.valtech.bookmyseat.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.valtech.bookmyseat.entity.Booking;
import com.valtech.bookmyseat.entity.Floor;
import com.valtech.bookmyseat.entity.Seat;
import com.valtech.bookmyseat.entity.Shift;

public class UserDashboardMapper implements RowMapper<Booking> {

	@Nullable
	private LocalDate getLocalDateOrNull(ResultSet rs, String columnName) throws SQLException {
		Date sqlDate = rs.getDate(columnName);
		return Objects.nonNull(sqlDate) ? sqlDate.toLocalDate() : null;
	}

	@Nullable
	private LocalTime getLocalTimeOrNull(ResultSet rs, String columnName) throws SQLException {
		Time sqlTime = rs.getTime(columnName);
		return Objects.nonNull(sqlTime) ? sqlTime.toLocalTime() : null;
	}

	@Override
	public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
		Booking booking = new Booking();
		booking.setBookingId(rs.getInt("booking_id"));
		booking.setStartDate(getLocalDateOrNull(rs, "start_date"));
		booking.setEndDate(getLocalDateOrNull(rs, "end_date"));
		Shift shift = new Shift();
		shift.setShiftName(rs.getString("shift_name"));
		shift.setStartTime(getLocalTimeOrNull(rs, "start_time"));
		shift.setEndTime(getLocalTimeOrNull(rs, "end_time"));
		Seat seat = new Seat();
		seat.setSeatId(rs.getInt("seat_id"));
		Floor floor = new Floor();
		floor.setFloorId(rs.getInt("floor_id"));
		floor.setFloorName(rs.getString("floor_name"));
		seat.setFloor(floor);
		booking.setShift(shift);
		booking.setSeat(seat);

		return booking;
	}
}
