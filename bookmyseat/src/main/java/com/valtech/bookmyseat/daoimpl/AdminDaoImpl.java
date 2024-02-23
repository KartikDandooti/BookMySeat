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
import com.valtech.bookmyseat.mapper.AdminDashBoardMapper;
import com.valtech.bookmyseat.mapper.BookingDetailsOfUserForAdminReportMapper;
import com.valtech.bookmyseat.mapper.ReservedMapper;
import com.valtech.bookmyseat.mapper.UserRequestsMapper;
import com.valtech.bookmyseat.model.AdminDashBoardModel;
import com.valtech.bookmyseat.model.BookingDetailsOfUserForAdminReport;
import com.valtech.bookmyseat.model.UserRequestsModel;

@Repository
public class AdminDaoImpl implements AdminDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<AdminDashBoardModel> fetchAdminDashboardDetails() throws DataBaseAccessException {
		LOGGER.info("Excuting the query to fetch Admin dashboard Details");
		String sql = "SELECT " + "COUNT(DISTINCT b.seat_id) AS seats_booked, " + "SUM(bm.lunch) AS total_lunch_booked, "
				+ "SUM(CASE WHEN bm.tea_coffee_type = 'TEA' THEN 1 ELSE 0 END) AS total_tea_booked, "
				+ "SUM(CASE WHEN bm.tea_coffee_type = 'COFFEE' THEN 1 ELSE 0 END) AS total_coffee_booked, "
				+ "SUM(bm.parking) AS total_parking_booked, "
				+ "SUM(CASE WHEN bm.parking_type = 'TWO_WHEELER' THEN 1 ELSE 0 END) AS total_two_wheeler_parking_booked, "
				+ "SUM(CASE WHEN bm.parking_type = 'FOUR_WHEELER' THEN 1 ELSE 0 END) AS total_four_wheeler_parking_booked, "
				+ "SUM(CASE WHEN bm.additional_desktop = 1 THEN 1 ELSE 0 END) AS total_desktop_booked "
				+ "FROM booking b " + "JOIN booking_mapping bm ON b.booking_id = bm.booking_id "
				+ "WHERE b.start_date <= CURDATE() AND b.end_date >= CURDATE()";
		LOGGER.debug("excuting the SQL Query to fetch Admin dashboard Details :{}", sql);

		return jdbcTemplate.query(sql, new AdminDashBoardMapper());
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
	public List<BookingDetailsOfUserForAdminReport> getAllBookingDetailsOfUserForAdminReport() {
		LOGGER.info("executing the query to fetch the bookinig details of all user");
		String sql = "SELECT CONCAT(U.first_name, ' ', U.last_name) AS user_name, U.user_id, "
				+ "S.seat_number, F.floor_name, SH.start_time, SH.end_time,B.start_date, "
				+ "B.end_date,B.booking_type,BM.tea_coffee_type AS tea_coffee_type, "
				+ "BM.parking_type AS parking_type,BM.lunch AS lunch FROM user U JOIN "
				+ "booking B ON U.user_id = B.user_id " + "JOIN "
				+ "booking_mapping BM ON B.booking_id = BM.booking_id AND BM.booking_date = CURDATE() " + "JOIN "
				+ "seat S ON B.seat_id = S.seat_id JOIN floor F ON S.floor_id = F.floor_id JOIN "
				+ "shift SH ON B.shift_id = SH.shift_id;";
		LOGGER.debug("Excuting the SQL query to fetch All Booking details:{}", sql);

		return jdbcTemplate.query(sql, new BookingDetailsOfUserForAdminReportMapper());
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