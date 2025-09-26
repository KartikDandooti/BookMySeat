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

import com.valtech.team3.bookmyseatbackend.dao.BookingDAO;
import com.valtech.team3.bookmyseatbackend.dao.FloorDAO;
import com.valtech.team3.bookmyseatbackend.dao.SeatDAO;
import com.valtech.team3.bookmyseatbackend.dao.ShiftDetailsDAO;
import com.valtech.team3.bookmyseatbackend.dao.UserDAO;
import com.valtech.team3.bookmyseatbackend.entities.Booking;
import com.valtech.team3.bookmyseatbackend.entities.BookingMapping;
import com.valtech.team3.bookmyseatbackend.entities.BookingRange;
import com.valtech.team3.bookmyseatbackend.entities.Floor;
import com.valtech.team3.bookmyseatbackend.entities.Seat;
import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;
import com.valtech.team3.bookmyseatbackend.entities.User;
import com.valtech.team3.bookmyseatbackend.entities.VehicleType;
import com.valtech.team3.bookmyseatbackend.models.BookingModel;
import com.valtech.team3.bookmyseatbackend.models.UserDashboardModel;

@Repository
public class BookingDAOImpl implements BookingDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private SeatDAO seatDAO;
	@Autowired
	private FloorDAO floorDAO;
	@Autowired
	private ShiftDetailsDAO shiftDAO;

	public class UserPreferredSeatsRowMapper implements RowMapper<Booking> {

		@Override
		public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
			Booking booking = new Booking();
			booking.setId(rs.getInt("BOOKING_ID"));
			booking.setStartDate(rs.getDate("START_DATE").toLocalDate());
			booking.setEndDate(rs.getDate("END_DATE").toLocalDate());
			booking.setBookingRange(BookingRange.valueOf(rs.getString("BOOKING_RANGE")));
			User user = new User();
			user.setId(rs.getInt("USER_ID"));
			user.setFirstName(rs.getString("FIRST_NAME"));
			booking.setUser(user);
			Seat seat = new Seat();
			seat.setId(rs.getInt("SEAT_ID"));
			seat.setSeatNumber(rs.getInt("SEAT_NUMBER"));
			Floor floor = new Floor();
			floor.setId(rs.getInt("FLOOR_ID"));
			seat.setFloor(floor);
			booking.setSeat(seat);

			return booking;
		}
	}

	public class BookingRowMapper implements RowMapper<Booking> {

		@Override
		public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
			Booking booking = new Booking();
			booking.setId(rs.getInt("id"));
			booking.setStartDate(rs.getDate("start_date").toLocalDate());
			booking.setEndDate(rs.getDate("end_date").toLocalDate());
			booking.setBookingRange(BookingRange.valueOf(rs.getString("booking_range")));
			booking.setBookingStatus(rs.getBoolean("booking_status"));
			booking.setVerificationCode(rs.getInt("verification_code"));
			booking.setUser(userDAO.getUserById(rs.getInt("user_id")));
			BookingMapping bookingMapping = new BookingMapping();
			bookingMapping.setId(rs.getInt("id"));
			bookingMapping.setBookedDate(rs.getDate("booked_date").toLocalDate());
			bookingMapping.setAdditionalDesktop(rs.getBoolean("additional_desktop"));
			bookingMapping.setLunch(rs.getBoolean("lunch"));
			bookingMapping.setBeverage(rs.getBoolean("beverage"));
			bookingMapping.setParking(rs.getBoolean("parking"));
			String vehicleTypeString = rs.getString("vehicle_Type");
			if (vehicleTypeString != null) {
				VehicleType vehicleType = VehicleType.valueOf(vehicleTypeString);
				bookingMapping.setVehicleType(vehicleType);
			}

			ShiftDetails shiftDetails = shiftDAO.getShiftDetailsById(rs.getInt("shift_id"));
			bookingMapping.setShiftDetails(shiftDetails);
			Seat seat = seatDAO.getSeatById(rs.getInt("seat_id"));
			seat.setFloor(floorDAO.getFloorById(rs.getInt("floor_id")));
			booking.setSeat(seat);
			booking.setBookingMappings(bookingMapping);

			return booking;
		}
	}

	public class BookingsRowMapper implements RowMapper<Booking> {

		@Override
		public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
			Booking booking = new Booking();
			booking.setId(rs.getInt("id"));
			booking.setStartDate(rs.getDate("start_date").toLocalDate());
			booking.setEndDate(rs.getDate("end_date").toLocalDate());
			booking.setBookingRange(BookingRange.valueOf(rs.getString("booking_range")));
			booking.setBookingStatus(rs.getBoolean("booking_status"));
			booking.setVerificationCode(rs.getInt("verification_code"));
			booking.setUser(userDAO.getUserById(rs.getInt("user_id")));
			booking.setSeat(seatDAO.getSeatById(rs.getInt("seat_id")));

			return booking;
		}
	}

	@Override
	public Integer createBooking(BookingModel booking, User user, Seat seat) {
		String insertQuery = "INSERT INTO BOOKING (BOOKING_RANGE, START_DATE, END_DATE, CREATED_DATE, USER_ID, SEAT_ID, BOOKING_STATUS, VERIFICATION_CODE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(insertQuery, booking.getBookingRange(), booking.getStartDate(), booking.getEndDate(), booking.getCreatedDate(), user.getId(), seat.getId(), booking.isBookingStatus(),
		      booking.getVerificationCode());

		return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

	}

	@Override
	public Boolean hasAlreadyBookedForDate(int userId, LocalDate startDate, LocalDate endDate) {
		String selectQuery = "SELECT COUNT(*) FROM BOOKING WHERE USER_ID = ? AND ((START_DATE <= ? AND END_DATE >= ?) OR (START_DATE >= ? AND END_DATE <= ?)) AND BOOKING_STATUS = TRUE ";
		int count = jdbcTemplate.queryForObject(selectQuery, Integer.class, userId, startDate, endDate, startDate, endDate);

		return count > 0;
	}

	@Override
	public List<Booking> getAllBookings(int userid) {
		String selectQuery = "SELECT B.*, BM.*, S.FLOOR_ID FROM BOOKING_MAPPING BM JOIN BOOKING B ON BM.BOOKING_ID = B.ID JOIN SEAT S ON B.SEAT_ID=S.ID JOIN FLOOR F ON S.FLOOR_ID = F.ID JOIN SHIFT_DETAILS SH ON BM.SHIFT_ID = SH.ID WHERE USER_ID = ?";

		return jdbcTemplate.query(selectQuery, new BookingRowMapper(), userid);
	}

	@Override
	public Booking getBookingById(int bookingId) {
		String selectQuery = "SELECT B.*, BM.*, S.*, F.* " + "FROM BOOKING B " + "JOIN SEAT S ON B.SEAT_ID = S.ID " + "JOIN FLOOR F ON S.FLOOR_ID = F.ID "
		      + "JOIN BOOKING_MAPPING BM ON B.ID = BM.BOOKING_ID " + "WHERE B.ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new BookingRowMapper(), bookingId);
	}

	@Override
	public Booking getBookingByMappingId(int bookingMappingId) {
		String selectQuery = "SELECT B.* FROM BOOKING B JOIN BOOKING_MAPPING BM ON B.ID = BM.BOOKING_ID WHERE BM.ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new BookingsRowMapper(), bookingMappingId);
	}

	@Override
	public void cancelBooking(int bookingId) {
		String updateQuery = "UPDATE BOOKING SET BOOKING_STATUS = FALSE WHERE ID = ?";
		jdbcTemplate.update(updateQuery, bookingId);
	}

	@Override
	public List<Booking> userPreferredSeats(int floorId, int projectId, LocalDate startDate) {
		String selectQuery = "SELECT B.ID AS BOOKING_ID, B.START_DATE, B.END_DATE, B.SEAT_ID, B.BOOKING_RANGE, B.USER_ID, U.FIRST_NAME, S.FLOOR_ID, S.SEAT_NUMBER " + "FROM BOOKING B "
		      + "JOIN BOOKING_MAPPING BM ON B.ID = BM.BOOKING_ID " + "JOIN SEAT S ON B.SEAT_ID = S.ID " + "JOIN USER U ON B.USER_ID = U.ID " + "WHERE BM.BOOKED_DATE = ? " + "AND S.FLOOR_ID = ? "
		      + "AND U.PROJECT_ID = ? AND B.BOOKING_STATUS = TRUE";

		return jdbcTemplate.query(selectQuery, new UserPreferredSeatsRowMapper(), startDate, floorId, projectId);
	}

	@Override
	public void updateBookingByAdmin(BookingModel booking, int bookingId, int seatId) {
		String updateQuery = "UPDATE BOOKING SET SEAT_ID = ? , MODIFIED_DATE= ? WHERE ID = ? AND BOOKING_STATUS = TRUE";
		jdbcTemplate.update(updateQuery, seatId, booking.getModifiedDate(), bookingId);
	}

	@Override
	public List<Booking> findBookingsByFloorAndDates(int floorId, LocalDate startDate, LocalDate endDate) {
		String selectQuery = "SELECT * FROM BOOKING B " + "WHERE B.BOOKING_STATUS = TRUE AND B.SEAT_ID IN ( " + "    SELECT S.ID " + "    FROM SEAT S " + "    WHERE S.FLOOR_ID = ? " + ") "
		      + "AND ((B.START_DATE <= ? AND B.END_DATE >= ?) " + "OR (B.START_DATE BETWEEN ? AND ?) " + "OR (B.END_DATE BETWEEN ? AND ?))";

		return jdbcTemplate.query(selectQuery, new BookingsRowMapper(), floorId, endDate, startDate, startDate, endDate, startDate, endDate);
	}

	@Override
	public Integer getAttendanceCountForCurrentMonth(int userId) {
		String selectQuery = "SELECT COUNT(*) FROM BOOKING_MAPPING WHERE ATTENDANCE=TRUE AND BOOKING_ID IN (SELECT ID FROM BOOKING WHERE USER_ID = ?)";

		return jdbcTemplate.queryForObject(selectQuery, Integer.class, userId);
	}

	@Override
	public List<UserDashboardModel> getUserDashboard(int userId) {
		String selectQuery = "SELECT B.VERIFICATION_CODE AS VERIFICATIONCODE, B.ID AS BOOKINGID, B.START_DATE AS STARTDATE, B.END_DATE AS ENDDATE, S.SEAT_NUMBER AS SEATNUMBER, F.FLOOR_NAME AS FLOORNAME, B.BOOKING_RANGE AS BOOKINGRANGE, SD.START_TIME AS STARTTIME, SD.END_TIME AS ENDTIME, U.EMPLOYEE_ID AS EMPLOYEEID FROM BOOKING B JOIN USER U ON B.USER_ID = U.ID JOIN SEAT S ON B.SEAT_ID=S.ID JOIN FLOOR F ON S.FLOOR_ID=F.ID JOIN BOOKING_MAPPING BM ON B.ID = BM.BOOKING_ID JOIN SHIFT_DETAILS SD ON BM.SHIFT_ID = SD.ID WHERE B.USER_ID = ? AND B.END_DATE >= CURDATE() AND B.BOOKING_STATUS = b'1' AND BM.BOOKED_DATE=B.START_DATE  GROUP BY B.ID, B.START_DATE, B.END_DATE, S.SEAT_NUMBER, F.FLOOR_NAME, B.BOOKING_RANGE, SD.START_TIME, SD.END_TIME, U.EMPLOYEE_ID";

		return jdbcTemplate.query(selectQuery, new BeanPropertyRowMapper<>(UserDashboardModel.class), userId);
	}

	@Override
	public List<Booking> getAllUserBookings(int id) {
		String selectQuery = "SELECT * FROM BOOKING WHERE END_DATE >= CURDATE() AND USER_ID = ?";

		return jdbcTemplate.query(selectQuery, new BookingsRowMapper(), id);
	}
}