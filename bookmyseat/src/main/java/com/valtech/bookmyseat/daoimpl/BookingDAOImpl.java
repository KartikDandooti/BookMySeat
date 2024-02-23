package com.valtech.bookmyseat.daoimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.valtech.bookmyseat.dao.BookingDAO;
import com.valtech.bookmyseat.dao.UserDAO;
import com.valtech.bookmyseat.entity.Booking;
import com.valtech.bookmyseat.entity.BookingMapping;
import com.valtech.bookmyseat.entity.BookingType;
import com.valtech.bookmyseat.entity.Floor;
import com.valtech.bookmyseat.entity.Seat;
import com.valtech.bookmyseat.entity.Shift;
import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.exception.DataBaseAccessException;
import com.valtech.bookmyseat.mapper.AttendanceRowMapper;
import com.valtech.bookmyseat.mapper.BookedSeatRowMapper;
import com.valtech.bookmyseat.mapper.BookingDTORowMapper;
import com.valtech.bookmyseat.model.BookedSeatModel;
import com.valtech.bookmyseat.model.BookingDTO;
import com.valtech.bookmyseat.model.BookingModel;

@Repository
public class BookingDAOImpl implements BookingDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(BookingDAOImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UserDAO userDAO;

	public class BookingRowMapper implements RowMapper<Booking> {

		@Override
		public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
			Booking booking = new Booking();
			booking.setBookingId(rs.getInt("BOOKING_ID"));
			booking.setStartDate(rs.getDate("START_DATE").toLocalDate());
			booking.setEndDate(rs.getDate("END_DATE").toLocalDate());
			booking.setBookingType(BookingType.valueOf(rs.getString("BOOKING_TYPE")));
			booking.setBookingStatus(rs.getBoolean("BOOKING_STATUS"));
			booking.setUser(userDAO.findUserByuserId(rs.getInt("USER_ID")));

			Floor floor = new Floor();
			floor.setFloorId(rs.getInt("floor_id"));
			Seat seat = new Seat();
			seat.setSeatId(rs.getInt("seat_id"));
			seat.setSeatNumber(rs.getInt("seat_number"));
			seat.setFloor(floor);
			booking.setSeat(seat);

			return booking;
		}
	}

	@Override
	public List<Booking> userPreferredSeats(int floorId, int projectId) {
		String selectQuery = "SELECT B.*, S.FLOOR_ID, S.SEAT_NUMBER " + "FROM BOOKING B "
				+ "JOIN BOOKING_MAPPING BM ON B.BOOKING_ID = BM.BOOKING_ID " + "JOIN SEAT S ON B.SEAT_ID = S.SEAT_ID "
				+ "WHERE BM.BOOKING_DATE = CURDATE() " + "AND S.FLOOR_ID = ? "
				+ "AND B.USER_ID IN (SELECT U.USER_ID FROM USER U WHERE U.PROJECT_ID = ?)";

		return jdbcTemplate.query(selectQuery, new BookingRowMapper(), floorId, projectId);
	}

	@Override
	public int createBooking(BookingModel booking, User user, Seat seat, Shift shift) throws DataBaseAccessException {
		LOGGER.info("excuting the query to insert the new booking");
		String insertQuery = "INSERT INTO BOOKING(BOOKING_TYPE , START_DATE, END_DATE, USER_ID, SHIFT_ID, SEAT_ID, BOOKING_STATUS) VALUES (?,?,?,?,?,?,?)";
		jdbcTemplate.update(insertQuery, booking.getBookingType().toString(), booking.getStartDate(),
				booking.getEndDate(), user.getUserId(), shift.getShiftId(), seat.getSeatId(), true);
		LOGGER.debug("Excuting the query to insert new booking for user:{} with query;{}", user.getUserId(),
				insertQuery);
		return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
	}

	@Override
	public void createBookingMapping(BookingModel booking, int bookingId) {
		String insertQuery = "INSERT INTO BOOKING_MAPPING (BOOKING_DATE, BOOKING_ID, ADDITIONAL_DESKTOP, LUNCH, TEA_COFFEE, TEA_COFFEE_TYPE, PARKING, PARKING_TYPE) VALUES (?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(insertQuery, booking.getBookingDate(), bookingId, booking.getAdditionalDesktop(),
				booking.getLunch(), booking.getTeaCoffee(), booking.getTeaCoffeeType(), booking.getParkingOpted(),
				booking.getParkingType());
	}

	@Override
	public List<BookingDTO> getAllBookings() throws DataBaseAccessException {
		LOGGER.info("excuting the query to fetch all booking detalis");
		String sql = "SELECT b.start_date AS booking_start_date, " + " b.end_date AS booking_end_date, "
				+ "       b.seat_id, " + " s.floor_id " + "FROM booking b "
				+ "JOIN seat s ON b.seat_id = s.seat_id WHERE b.booking_status=TRUE";
		LOGGER.debug("excuting the query to fetch all booking, query:{}", sql);

		return jdbcTemplate.query(sql, new BookingDTORowMapper());
	}

	@Override
	public List<BookingMapping> getAllBookingDetails() throws DataAccessException {
		String sql = "SELECT " + "b.booking_id, " + "b.booking_type, " + "b.start_date, " + "b.end_date, "
				+ "s.seat_id, " + "l.location_name, " + "l.location_id, " + "f.floor_id, " + "shift.shift_name, "
				+ "shift.start_time, " + "shift.end_time, " + "u.first_name, " + "u.user_id, " + "p.project_name, "
				+ "p.project_id, " + "u.approval_status, " + "bm.marked_status " + "FROM " + "booking b " + "JOIN "
				+ "booking_mapping bm ON b.booking_id = bm.booking_id " + "JOIN " + "seat s ON b.seat_id = s.seat_id "
				+ "JOIN " + "floor f ON s.floor_id = f.floor_id " + "JOIN "
				+ "location l ON f.location_id = l.location_id " + "JOIN " + "shift ON b.shift_id = shift.shift_id "
				+ "JOIN " + "user u ON b.user_id = u.user_id " + "JOIN " + "project p ON u.project_id = p.project_id "
				+ "WHERE " + "bm.booking_date = CURDATE()";
		LOGGER.debug("Fetching User Attendance details");

		return jdbcTemplate.query(sql, new AttendanceRowMapper());
	}

	@Override
	public void approvalAttendance(int userId) {
		String sql = "UPDATE booking_mapping AS bm " + "INNER JOIN booking AS b ON bm.booking_id = b.booking_id "
				+ "SET bm.marked_status = ?, " + "bm.booked_date = CURDATE() " + "WHERE b.user_id = ?";
		LOGGER.debug("Approving the attendance");
		jdbcTemplate.update(sql, true, userId);
	}

	@Override
	public boolean hasAlreadyBookedForDate(int userId, LocalDate startDate, LocalDate endDate) throws DataBaseAccessException{
		String selectQuery = "SELECT COUNT(*) FROM BOOKING WHERE USER_ID = ? AND ((START_DATE <= ? AND END_DATE >= ?) OR (START_DATE >= ? AND END_DATE <= ?)) AND BOOKING_STATUS = true";
		int count = jdbcTemplate.queryForObject(selectQuery, Integer.class, userId, startDate, endDate, startDate,
				endDate);

		return count > 0;
	}

	@Override
	public List<BookedSeatModel> getAllBookedSeat() throws DataBaseAccessException {
		LOGGER.info("excuting the query to fetch the booked seats");
		String sql = "SELECT S.seat_number,S.floor_id,B.booking_id,B.booking_status,B.start_date,B.end_date,U.user_id "
				+ "FROM user U " + "JOIN booking B ON U.user_id = B.user_id " + "JOIN seat S ON S.seat_id=B.seat_id ";
		LOGGER.debug("excuting the query to fetch the booked seats query:{}",sql);
		
		return jdbcTemplate.query(sql, new BookedSeatRowMapper());
	}

}