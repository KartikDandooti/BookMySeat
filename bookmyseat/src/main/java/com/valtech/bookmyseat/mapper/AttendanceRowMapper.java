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

import com.valtech.bookmyseat.entity.ApprovalStatus;
import com.valtech.bookmyseat.entity.Booking;
import com.valtech.bookmyseat.entity.BookingMapping;
import com.valtech.bookmyseat.entity.BookingType;
import com.valtech.bookmyseat.entity.Location;
import com.valtech.bookmyseat.entity.Project;
import com.valtech.bookmyseat.entity.Seat;
import com.valtech.bookmyseat.entity.Shift;
import com.valtech.bookmyseat.entity.User;

public class AttendanceRowMapper implements RowMapper<BookingMapping> {

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
	public BookingMapping mapRow(ResultSet rs, int rowNum) throws SQLException {
		Booking booking = new Booking();
		booking.setBookingId(rs.getInt("booking_id"));
		booking.setBookingType(BookingType.valueOf(rs.getString("booking_type")));
		booking.setStartDate(getLocalDateOrNull(rs, "start_date"));
		booking.setEndDate(getLocalDateOrNull(rs, "end_date"));
		BookingMapping bookingMapping = new BookingMapping();
		bookingMapping.setMarkedStatus(rs.getBoolean("marked_status"));
		bookingMapping.setBooking(booking);
		Shift shift = new Shift();
		shift.setShiftName(rs.getString("shift_name"));
		shift.setStartTime(getLocalTimeOrNull(rs, "start_time"));
		shift.setEndTime(getLocalTimeOrNull(rs, "end_time"));
		Seat seat = new Seat();
		seat.setSeatId(rs.getInt("seat_id"));
		Project project = new Project();
		project.setProjectId(rs.getInt("project_id"));
		project.setProjectName(rs.getString("project_name"));
		User user = new User();
		user.setUserId(rs.getInt("user_id"));
		user.setFirstName(rs.getString("first_name"));
		user.setApprovalStatus(ApprovalStatus.valueOf(rs.getString("approval_status")));
		user.setProject(project);
		Location location = new Location();
		location.setLocationId(rs.getInt("location_id"));
		location.setLocationName(rs.getString("location_name"));
		booking.setSeat(seat);
		booking.setShift(shift);
		booking.setUser(user);

		return bookingMapping;
	}
}