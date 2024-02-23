package com.valtech.bookmyseat.daoimpl;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.valtech.bookmyseat.dao.AdminDAO;
import com.valtech.bookmyseat.entity.Reserved;
import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.exception.DataBaseAccessException;
import com.valtech.bookmyseat.mapper.BookingDetailsMapper;
import com.valtech.bookmyseat.mapper.BookingMapper;
import com.valtech.bookmyseat.mapper.ReservedMapper;
import com.valtech.bookmyseat.mapper.UserRequestsMapper;
import com.valtech.bookmyseat.model.AdminDashBoardModel;
import com.valtech.bookmyseat.model.BookingModel;
import com.valtech.bookmyseat.model.UserRequestsModel;

@Repository
public class AdminDaoImpl implements AdminDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<AdminDashBoardModel> fetchDailyBookingDetails() throws DataBaseAccessException {
		LOGGER.info("Excuting the query to fetch Daily booking details");
		String sql = "SELECT CURDATE() AS Date,COUNT(DISTINCT seat_id) AS seats_booked, "
				+ "SUM(lunch) AS total_lunch_booked, "
				+ "SUM(CASE WHEN tea_coffee = 'TEA' THEN 1 ELSE 0 END) AS total_tea_booked, "
				+ "SUM(CASE WHEN tea_coffee = 'COFFEE' THEN 1 ELSE 0 END) AS total_coffee_booked, "
				+ "SUM(parking) AS total_parking_booked, "
				+ "SUM(CASE WHEN parking_type = 'TWO_WHEELER' THEN 1 ELSE 0 END) AS total_two_wheeler_parking_booked, "
				+ "SUM(CASE WHEN parking_type = 'FOUR_WHEELER' THEN 1 ELSE 0 END) AS total_four_wheeler_parking_booked, "
				+ "SUM(CASE WHEN additional_desktop = true THEN 1 ELSE 0 END) AS total_desktop_booked "
				+ "FROM booking " + "WHERE start_date <= CURDATE() AND end_date >= CURDATE()";
		LOGGER.debug("excuting the SQL Query to fetch Daily booking details :{}", sql);

		return jdbcTemplate.query(sql, new BookingMapper());
	}

	@Override
	public List<UserRequestsModel> fetchUserRequests() throws DataBaseAccessException {
		LOGGER.info("executing the query to fetch the user request...");
		String sql = "SELECT CONCAT(first_name, ' ', last_name) AS name,user_id,email_id,registered_date,approval_status"
				+ " FROM user" + " WHERE approval_status IN ('PENDING', 'APPROVED', 'REJECTED') AND role_id=2";
		LOGGER.debug("excuting the SQL query to fetch user requests:{}", sql);

		return jdbcTemplate.query(sql, new UserRequestsMapper());
	}

	@Override
	public int updateUserRequests(User user, int userId) throws DataBaseAccessException {
		LOGGER.info("Excuting the query to update the user approval status for User:{} and approvalStatus:{}",
				user.getUserId(), user.getApprovalStatus());
		String sql = "UPDATE user SET approval_status = ?,modified_by=?,modified_date=? WHERE user_id = ?";
		LOGGER.debug("Excuting the SQL query to update user approval request:{}", sql);

		return jdbcTemplate.update(sql, String.valueOf(user.getApprovalStatus()), userId, LocalDate.now(),
				user.getUserId());
	}

	@Override
	public List<BookingModel> getAllBookingDetails() {
		LOGGER.info("executing the query to fetch the bookinig details");
		String query = "SELECT CONCAT(U.first_name, ' ', U.last_name) AS user_name, U.user_id, "
				+ "B.parking AS parking_opted, B.parking_type, B.lunch, B.tea_coffee,B.tea_coffee_type, B.additional_desktop, B.start_date,B.end_date,B.booking_type "
				+ "FROM user U " + "JOIN booking B ON U.user_id = B.user_id";
		LOGGER.debug("Excuting the SQL query to fetch All Booking details:{}", query);

		return jdbcTemplate.query(query, new BookingDetailsMapper());
	}

	@Override
	public List<Reserved> reserveSeat() throws DataBaseAccessException {
		LOGGER.info("Retrieving the details for reserved seats");
		String sql = "SELECT user.user_id, user.first_name, reserved.reserved_status, reserved.reserved_id, seat.seat_id, seat.seat_number, seat.floor_id "
				+ "FROM user " + "JOIN reserved ON user.user_id = reserved.user_id "
				+ "JOIN seat ON reserved.seat_id = seat.seat_id " + "WHERE reserved.reserved_status = true";

		return jdbcTemplate.query(sql, new ReservedMapper());
	}

	@Override
	public void reserveSeatInDB(int userId, int floorId, int seatId) throws DataBaseAccessException {
		LOGGER.info("Reserving the seat for the user");
		String sql = "INSERT INTO reserved (user_id, seat_id, reserved_status) VALUES (?, ?, true)";
		jdbcTemplate.update(sql, userId, seatId);
	}
}