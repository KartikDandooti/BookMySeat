package com.valtech.team3.bookmyseatbackend.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.valtech.team3.bookmyseatbackend.dao.BookingMappingDAO;
import com.valtech.team3.bookmyseatbackend.entities.Booking;
import com.valtech.team3.bookmyseatbackend.entities.BookingMapping;
import com.valtech.team3.bookmyseatbackend.entities.Floor;
import com.valtech.team3.bookmyseatbackend.entities.Seat;
import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;
import com.valtech.team3.bookmyseatbackend.entities.User;
import com.valtech.team3.bookmyseatbackend.entities.VehicleType;
import com.valtech.team3.bookmyseatbackend.models.AdminDashboardModel;
import com.valtech.team3.bookmyseatbackend.models.BookingModel;

@Repository
public class BookingMappingDAOImpl implements BookingMappingDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public record BookingMappingRowMapper() implements RowMapper<BookingMapping> {

		@Override
		public BookingMapping mapRow(ResultSet rs, int rowNum) throws SQLException {
			BookingMapping bookingMapping = new BookingMapping();
			Booking booking = new Booking();
			User user = new User();
			user.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
			user.setFirstName(rs.getString("FIRST_NAME"));
			user.setLastName(rs.getString("LAST_NAME"));
			booking.setUser(user);
			Seat seat = new Seat();
			seat.setId(rs.getInt("SEAT_ID"));
			seat.setSeatNumber(rs.getInt("SEAT_NUMBER"));
			booking.setVerificationCode(rs.getInt("VERIFICATION_CODE"));
			booking.setSeat(seat);
			Floor floor = new Floor();
			floor.setFloorName(rs.getString("FLOOR_NAME"));
			seat.setFloor(floor);
			bookingMapping.setBooking(booking);

			return bookingMapping;
		}
	}

	public class NewBookingMappingRowMapper implements RowMapper<BookingMapping> {

		@Override
		public BookingMapping mapRow(ResultSet rs, int rowNum) throws SQLException {
			BookingMapping bookingMapping = new BookingMapping();
			bookingMapping.setId(rs.getInt("id"));
			bookingMapping.setBookedDate(rs.getDate("booked_date").toLocalDate());
			bookingMapping.setAdditionalDesktop(rs.getBoolean("additional_desktop"));
			bookingMapping.setAttendance(rs.getBoolean("attendance"));
			bookingMapping.setBeverage(rs.getBoolean("beverage"));
			bookingMapping.setLunch(rs.getBoolean("lunch"));
			bookingMapping.setParking(rs.getBoolean("parking"));
			String vehicleTypeString = rs.getString("vehicle_type");
			if (vehicleTypeString != null) {
				VehicleType vehicleType = VehicleType.valueOf(vehicleTypeString);
				bookingMapping.setVehicleType(vehicleType);
			}

			Booking booking = new Booking();
			booking.setId(rs.getInt("booking_id"));
			booking.setStartDate(rs.getDate("start_date").toLocalDate());
			booking.setEndDate(rs.getDate("end_date").toLocalDate());
			ShiftDetails shiftDetails = new ShiftDetails();
			shiftDetails.setId(rs.getInt("shift_id"));
			shiftDetails.setShiftName(rs.getString("shift_name"));
			shiftDetails.setStartTime(rs.getTime("start_time").toLocalTime());
			shiftDetails.setEndTime(rs.getTime("end_time").toLocalTime());
			bookingMapping.setBooking(booking);
			bookingMapping.setShiftDetails(shiftDetails);

			return bookingMapping;
		}
	}

	public static class AdminDashboardModelRowMapper implements RowMapper<AdminDashboardModel> {

		@Override
		public AdminDashboardModel mapRow(ResultSet rs, int rowNum) throws SQLException {
			AdminDashboardModel counts = new AdminDashboardModel();
			counts.setTotalAttendees(rs.getInt("totalAttendees"));
			counts.setTotalSeatsBooked(rs.getInt("totalSeatsBooked"));
			counts.setTotalParkingSpaceBooked(rs.getInt("totalParkingSpaceBooked"));
			counts.setFourWheelerParkingSpaceBooked(rs.getInt("fourWheelerParkingSpaceBooked"));
			counts.setTwoWheelerParkingSpaceBooked(rs.getInt("twoWheelerParkingSpaceBooked"));
			counts.setTotalEmployeesOptedForLunch(rs.getInt("totalEmployeesOptedForLunch"));
			counts.setTotalEmployeesOptedForDesktop(rs.getInt("totalEmployeesOptedForDesktop"));

			return counts;
		}
	}

	@Override
	public void createBookingMapping(BookingModel booking, int bookingId, ShiftDetails shift) {
		String insertQuery = "INSERT INTO BOOKING_MAPPING (BOOKED_DATE, ATTENDANCE, BOOKING_ID,ADDITIONAL_DESKTOP,LUNCH,BEVERAGE,PARKING,SHIFT_ID,VEHICLE_TYPE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(insertQuery, booking.getBookedDate(), booking.isAttendance(), bookingId, booking.isAdditionalDesktop(), booking.isLunch(), booking.isBeverage(), booking.isParking(),
		      shift.getId(), booking.getVehicleType());
	}

	@Override
	public BookingMapping getBookingMappingById(int bookingMappingId) {
		String selectQuery = "SELECT * FROM BOOKING_MAPPING WHERE ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new BeanPropertyRowMapper<>(BookingMapping.class), bookingMappingId);
	}

	@Override
	public List<BookingMapping> getBookingMappingsByBookingId(int bookingId) {
		String selectQuery = "SELECT BM.*, B.ID,B.START_DATE,B.END_DATE,B.BOOKING_STATUS, S.ID,S.SHIFT_NAME,S.START_TIME,S.END_TIME " + "FROM BOOKING_MAPPING BM "
		      + "JOIN BOOKING B ON BM.BOOKING_ID = B.ID " + "JOIN SHIFT_DETAILS S ON BM.SHIFT_ID = S.ID " + "WHERE BM.BOOKING_ID = ?";

		return jdbcTemplate.query(selectQuery, new NewBookingMappingRowMapper(), bookingId);
	}

	@Override
	public List<BookingMapping> getAllBookingsForCurrentDate() {
		String selectQuery = "SELECT U.EMPLOYEE_ID, U.FIRST_NAME, U.LAST_NAME, B.SEAT_ID, S.SEAT_NUMBER, F.FLOOR_NAME, B.VERIFICATION_CODE " + "FROM BOOKING B "
		      + "JOIN BOOKING_MAPPING BM ON B.ID = BM.BOOKING_ID " + "JOIN USER U ON B.USER_ID = U.ID " + "JOIN SEAT S ON B.SEAT_ID = S.id " + "JOIN FLOOR F ON S.floor_id = F.id "
		      + "WHERE BM.BOOKED_DATE = CURRENT_DATE() AND BM.ATTENDANCE = FALSE AND B.BOOKING_STATUS = TRUE";

		return jdbcTemplate.query(selectQuery, new BookingMappingRowMapper());
	}

	@Override
	public void updateBooking(BookingMapping bookingMapping, int bookingId) {
		String updateQuery = "UPDATE BOOKING_MAPPING BM " + "SET BM.MODIFIED_DATE = ?, BM.PARKING = ?, BM.BEVERAGE = ?, BM.VEHICLE_TYPE = ?," + "BM.LUNCH = ?,BM.ADDITIONAL_DESKTOP = ?, BM.SHIFT_ID = ?"
		      + " WHERE BM.ID= ?";
		jdbcTemplate.update(updateQuery, bookingMapping.getModifiedDate(), bookingMapping.isParking(), bookingMapping.isBeverage(),
		      bookingMapping.getVehicleType() != null ? bookingMapping.getVehicleType().name() : null, bookingMapping.isLunch(), bookingMapping.isAdditionalDesktop(),
		      bookingMapping.getShiftDetails().getId(), bookingId);
	}

	@Override
	public void updateAttendence(int employeeId) {
		String updateQuery = "UPDATE booking_mapping " + "SET attendance = TRUE, modified_date = NOW(), attendance_time = CURTIME() " + "WHERE booking_id IN " + "    (SELECT B.ID "
		      + "     FROM booking B " + "     JOIN user U ON B.USER_ID = U.ID " + "     WHERE U.EMPLOYEE_ID = ?) " + "AND booked_date = CURRENT_DATE();";
		jdbcTemplate.update(updateQuery, employeeId);
	}

	@Override
	public Boolean isBookingAvailableForCurrentDate(int employeeId) {
		String selectQuery = "SELECT COUNT(*) FROM BOOKING_MAPPING " + "WHERE BOOKING_ID IN (SELECT B.ID FROM BOOKING B " + "JOIN USER U ON B.USER_ID = U.ID " + "WHERE U.EMPLOYEE_ID = ?) "
		      + "AND BOOKED_DATE = CURDATE()";
		Integer count = jdbcTemplate.queryForObject(selectQuery, Integer.class, employeeId);

		return count != null && count > 0;
	}

	@Override
	public Boolean isAttendanceMarkedForCurrentDate(int employeeId) {
		String selectQuery = "SELECT COUNT(*) FROM BOOKING_MAPPING " + "WHERE BOOKING_ID IN (SELECT B.ID FROM BOOKING B " + "JOIN USER U ON B.USER_ID = U.ID " + "WHERE U.EMPLOYEE_ID = ?) "
		      + "AND BOOKED_DATE = CURRENT_DATE() AND ATTENDANCE = TRUE";
		Integer count = jdbcTemplate.queryForObject(selectQuery, Integer.class, employeeId);

		return count != null && count > 0;
	}

	@Override
	public List<BookingMapping> getAllBookingsAttendanceMarkedForCurrentDate() {
		String selectQuery = "SELECT U.EMPLOYEE_ID, U.FIRST_NAME, U.LAST_NAME, B.SEAT_ID, S.SEAT_NUMBER, F.FLOOR_NAME, B.VERIFICATION_CODE " + "FROM BOOKING B "
		      + "JOIN BOOKING_MAPPING BM ON B.ID = BM.BOOKING_ID " + "JOIN USER U ON B.USER_ID = U.ID " + "JOIN SEAT S ON B.SEAT_ID = S.id " + "JOIN FLOOR F ON S.floor_id = F.id "
		      + "WHERE BM.BOOKED_DATE = CURRENT_DATE() AND BM.ATTENDANCE = TRUE";

		return jdbcTemplate.query(selectQuery, new BookingMappingRowMapper());
	}

	@Override
	public AdminDashboardModel getAdminDashboardModel() {
		String selectQuery = "SELECT " + "SUM(CASE WHEN BM.ATTENDANCE = TRUE THEN 1 ELSE 0 END) AS totalAttendees, " + "COUNT(BM.BOOKING_ID) AS totalSeatsBooked, "
		      + "SUM(CASE WHEN BM.PARKING = TRUE THEN 1 ELSE 0 END) AS totalParkingSpaceBooked, "
		      + "SUM(CASE WHEN BM.PARKING = TRUE AND BM.VEHICLE_TYPE = 'WHEELER_4' THEN 1 ELSE 0 END) AS fourWheelerParkingSpaceBooked, "
		      + "SUM(CASE WHEN BM.PARKING = TRUE AND BM.VEHICLE_TYPE = 'WHEELER_2' THEN 1 ELSE 0 END) AS twoWheelerParkingSpaceBooked, "
		      + "SUM(CASE WHEN BM.LUNCH = TRUE THEN 1 ELSE 0 END) AS totalEmployeesOptedForLunch, " + "SUM(CASE WHEN BM.ADDITIONAL_DESKTOP = TRUE THEN 1 ELSE 0 END) AS totalEmployeesOptedForDesktop "
		      + "FROM BOOKING_MAPPING BM " + "WHERE BM.BOOKED_DATE = ?";

		return jdbcTemplate.queryForObject(selectQuery, new AdminDashboardModelRowMapper(), LocalDate.now());
	}

	@Override
	public Boolean isAttendanceMarkedForBooking(int bookingId) {
		String query = "SELECT COUNT(*) FROM BOOKING_MAPPING WHERE BOOKING_ID = ? AND ATTENDANCE = TRUE";
		int count = jdbcTemplate.queryForObject(query, Integer.class, bookingId);

		return count > 0;
	}

	@Override
	public Boolean isAttendanceMarkedForBookingMapping(int bookingMappingId) {
		String selectQuery = "SELECT IF(attendance = b'1', TRUE, FALSE) AS is_attendance_marked " + "FROM booking_mapping " + "WHERE id = ?";

		return jdbcTemplate.queryForObject(selectQuery, Boolean.class, bookingMappingId);
	}

	@Override
	public Integer getVehicleTypeCount(String vehicleType, LocalDate startDate, LocalDate endDate) {
		String query = "SELECT COUNT(*) FROM BOOKING_MAPPING BM JOIN BOOKING B ON BM.BOOKING_ID=B.ID WHERE vehicle_type = ? AND START_DATE=? AND END_DATE=?";

		return jdbcTemplate.queryForObject(query, Integer.class, vehicleType, startDate, endDate);
	}
}