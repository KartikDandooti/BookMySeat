package com.valtech.bookmyseat.daoimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.valtech.bookmyseat.dao.ProjectDAO;
import com.valtech.bookmyseat.dao.RoleDAO;
import com.valtech.bookmyseat.dao.UserDAO;
import com.valtech.bookmyseat.entity.ApprovalStatus;
import com.valtech.bookmyseat.entity.Booking;
import com.valtech.bookmyseat.entity.Otp;
import com.valtech.bookmyseat.entity.Project;
import com.valtech.bookmyseat.entity.Role;
import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.exception.DataBaseAccessException;
import com.valtech.bookmyseat.mapper.ModifyUserMapper;
import com.valtech.bookmyseat.mapper.UpdateSeatBookingMapper;
import com.valtech.bookmyseat.mapper.UserDashboardMapper;
import com.valtech.bookmyseat.mapper.UserHistoryBookingMapper;
import com.valtech.bookmyseat.mapper.UserPrefernceMapper;
import com.valtech.bookmyseat.mapper.UserRowMapper;
import com.valtech.bookmyseat.model.UserBookingHistoryModel;
import com.valtech.bookmyseat.model.UserModel;
import com.valtech.bookmyseat.model.UserModifyBooking;

@Repository
public class UserDaoImpl implements UserDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private int rowsAffected;

	@Override
	public User getUserByEmail(String emailId) {
		LOGGER.info("executing the query to fetching the user details based on his emailId");
		String selectQuery = "SELECT "
				+ "U.USER_ID, U.EMAIL_ID, U.FIRST_NAME, U.LAST_NAME, U.PASSWORD, U.APPROVAL_STATUS, "
				+ "U.PHONE_NUMBER, U.REGISTERED_DATE, U.MODIFIED_DATE, U.PROJECT_ID, P.PROJECT_NAME, "
				+ "R.ROLE_ID, R.ROLE_NAME, U.CREATED_BY, U.MODIFIED_BY " + "FROM USER U "
				+ "JOIN `ROLE` R ON U.ROLE_ID = R.ROLE_ID " + "JOIN PROJECT P ON U.PROJECT_ID = P.PROJECT_ID "
				+ "WHERE U.EMAIL_ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new UserRowMapper(), (Object) emailId);
	}

	@Override
	public User userRegistration(User user) {
		LOGGER.info("Executing the query to store the user details in the database");
		String createQuery = "INSERT INTO USER (USER_ID, EMAIL_ID, FIRST_NAME, LAST_NAME, PASSWORD, APPROVAL_STATUS, PHONE_NUMBER, REGISTERED_DATE, MODIFIED_DATE, CREATED_BY, MODIFIED_BY, ROLE_ID,PROJECT_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Role role = roleDAO.getUserRoleByRoleID(2);
		Project project = projectDAO.getProjectById(6);
		jdbcTemplate.update(createQuery, user.getUserId(), user.getEmailId(), user.getFirstName(), user.getLastName(),
				passwordEncoder.encode(user.getPassword()), ApprovalStatus.PENDING.name(), user.getPhoneNumber(),
				LocalDateTime.now(), LocalDateTime.now(), user.getUserId(), user.getUserId(), role.getRoleId(),
				project.getProjectId());

		return user;
	}

	@Override
	public User findUserByuserId(int userId) {
		LOGGER.info("executing the query to fetching the user details based on his UserId");

		String selectQuery = "SELECT "
				+ "U.USER_ID, U.EMAIL_ID, U.FIRST_NAME, U.LAST_NAME, U.PASSWORD, U.APPROVAL_STATUS, "
				+ "U.PHONE_NUMBER, U.REGISTERED_DATE, U.MODIFIED_DATE, U.PROJECT_ID, P.PROJECT_NAME, "
				+ "R.ROLE_ID, R.ROLE_NAME, U.CREATED_BY, U.MODIFIED_BY " + "FROM USER U "
				+ "JOIN `ROLE` R ON U.ROLE_ID = R.ROLE_ID " + "JOIN PROJECT P ON U.PROJECT_ID = P.PROJECT_ID "
				+ "WHERE U.USER_ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new UserRowMapper(), userId);
	}

	@Override
	public User getById(int userId) {
		String selectQuery = "SELECT * FROM USER WHERE USER_ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new UserPrefernceMapper(), userId);
	}

	@Override
	public String createUser(UserModel userModel) {
		LOGGER.debug("Executing the query For creating user profile");
		String newUser = "INSERT INTO USER (USER_ID, EMAIL_ID, FIRST_NAME, LAST_NAME, PASSWORD, APPROVAL_STATUS, PHONE_NUMBER, REGISTERED_DATE, MODIFIED_DATE, CREATED_BY, MODIFIED_BY, ROLE_ID,PROJECT_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String firstThreeLetters = userModel.getFirstName().substring(0,
				Math.min(3, userModel.getFirstName().length()));
		String result = firstThreeLetters.substring(0, 1).toUpperCase() + firstThreeLetters.substring(1);
		long phoneNumberLong = userModel.getPhoneNumber();
		String phoneNumber = Long.toString(phoneNumberLong);
		String lastFourDigits = phoneNumber.substring(phoneNumber.length() - 4);
		String finalPassword = result + "@" + lastFourDigits;
		String password = passwordEncoder.encode(finalPassword);
		Role role = roleDAO.getUserRoleByRoleID(2);
		jdbcTemplate.update(newUser, userModel.getUserId(), userModel.getEmailId(), userModel.getFirstName(),
				userModel.getLastName(), password, ApprovalStatus.APPROVED.name(), userModel.getPhoneNumber(),
				LocalDateTime.now(), LocalDateTime.now(), userModel.getUserId(), userModel.getUserId(),
				role.getRoleId(), userModel.getProjectId());

		return "User created successfully";
	}

	@Override
	public boolean isEmailExists(String emailId) {
		LOGGER.info("Executing the query to check if the user with an emailId exists or not");
		String query = "SELECT COUNT(*) FROM USER WHERE EMAIL_ID = ?";
		try {
			Integer count = jdbcTemplate.queryForObject(query, Integer.class, emailId);

			return Objects.nonNull(count) && count > 0;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error("Error occurred while checking email existence: {}", e.getMessage());

			return false;
		}
	}

	@Override
	public boolean isEmployeeIdExists(int userId) {
		LOGGER.info("Executing the query to check if the user with a UserId exists or not");
		String query = "SELECT COUNT(*) FROM user WHERE user_id = ?";
		try {
			Integer count = jdbcTemplate.queryForObject(query, Integer.class, userId);

			return Objects.nonNull(count) && count > 0;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error("Error occurred while checking employee ID existence: {}", e.getMessage());

			return false;
		}
	}

	@Override
	public void updateUser(UserModel userModel) {
		LOGGER.info("Executing the query to update userdeatils");
		String sql = "UPDATE USER SET phone_number=?,project_id=?,modified_date=? WHERE user_id=?";
		LOGGER.debug("updating user deatils");
		jdbcTemplate.update(sql, userModel.getPhoneNumber(), userModel.getProjectId(), LocalDate.now(),
				userModel.getUserId());
	}

	@Override
	public User getUserById(int userId) {
		LOGGER.debug("Executing the query to fetch userId");
		String sql = "SELECT user_id, phone_number, project_id FROM USER WHERE user_id=?";

		return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
			User user = new User();
			user.setUserId(rs.getInt("user_id"));
			user.setPhoneNumber(rs.getLong("phone_number"));

			return user;
		}, (Object) userId);
	}

	@Override
	public List<Map<String, Object>> getUserSeatInfo() throws DataBaseAccessException {
		LOGGER.info("Fetching user seat info from the database.");
		String sql = "SELECT booking.booking_id, booking.booking_status, user.first_name, user.user_id, seat.seat_number, seat.floor_id, booking.start_date, booking.end_date "
				+ "FROM booking " + "JOIN user ON booking.user_id = user.user_id "
				+ "JOIN seat ON booking.seat_id = seat.seat_id";
		List<Map<String, Object>> userSeatInfo = jdbcTemplate.queryForList(sql);
		LOGGER.info("Successfully retrieved user seat info.");

		return userSeatInfo;
	}

	@Override
	public List<User> getAllUserDetails(int userId) {
		String sql = " SELECT u.user_id, u.first_name, u.last_name, u.email_id, u.phone_number FROM USER u WHERE u.user_id =?";
		List<User> userDetailsList = jdbcTemplate.query(sql, new ModifyUserMapper(), (Object) userId);
		LOGGER.info("Retrieved {} shift details from the database", userDetailsList.size());

		return userDetailsList;
	}

	@Override
	public Integer getSeatIdByNumberAndFloor(int seatNumber, int floorId) {
		String sql = "SELECT seat_id FROM seat WHERE seat_number = ? AND floor_id = ?";
		try {
			return jdbcTemplate.queryForObject(sql, Integer.class, seatNumber, floorId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Integer getUserIdByBookingId(int bookingId) {
		String sql = "SELECT user_id FROM booking WHERE booking_id = ?";
		try {
			return jdbcTemplate.queryForObject(sql, Integer.class, bookingId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public void updateUserSeat(int seatId, int bookingId) {
		String sql = "UPDATE BOOKING SET SEAT_ID = ? WHERE BOOKING_ID = ?";
		rowsAffected = jdbcTemplate.update(sql, seatId, bookingId);
		if (rowsAffected == 0) {
			throw new IllegalArgumentException("Booking not found with id: " + bookingId);
		}
	}

	@Override
	public void cancelUserBooking(int bookingId) {
		String query = "UPDATE BOOKING SET BOOKING_STATUS = FALSE WHERE BOOKING_ID = ?";
		rowsAffected = jdbcTemplate.update(query, bookingId);
		if (rowsAffected == 0) {
			throw new IllegalArgumentException("Booking not found with id: " + bookingId);
		}
	}

	@Override
	public List<Booking> getUserDashboardDetails(int userId) throws DataAccessException {
		String sql = "SELECT b.booking_id, b.start_date, b.end_date, shift.shift_name, shift.start_time, shift.end_time, b.seat_id, s.floor_id, f.floor_name, b.lunch, b.tea_coffee, b.tea_coffee_type "
				+ "FROM booking b " + "JOIN seat s ON b.seat_id = s.seat_id "
				+ "JOIN shift ON shift.shift_id = b.shift_id " + "JOIN floor f ON s.floor_id = f.floor_id "
				+ "WHERE b.user_id = ?";
		LOGGER.debug("Fetching User dashboard details");

		return jdbcTemplate.query(sql, new UserDashboardMapper(), (Object) userId);
	}

	@Override
	public List<UserModifyBooking> getBookingDeatilsOfUser(int userId, int seatId) {
		String sql = "SELECT " + "u.user_Id AS userId, " + "CONCAT(u.first_Name, ' ', u.last_Name) AS userName, "
				+ "b.start_Date AS booking_startDate, " + "b.end_Date AS booking_endDate, "
				+ "s.shift_Name AS shiftName, " + "st.seat_Number AS seatNumber, " + "f.floor_Name AS floorName, "
				+ "b.booking_status AS bookingStatus, " + "u.email_Id AS userEmail " + "FROM " + "User u "
				+ "INNER JOIN " + "Booking b ON u.user_Id = b.user_Id " + "INNER JOIN "
				+ "Shift s ON b.shift_Id = s.shift_Id " + "INNER JOIN " + "Seat st ON b.seat_Id = st.seat_Id "
				+ "INNER JOIN " + "Floor f ON st.floor_Id = f.floor_Id " + "WHERE "
				+ "u.user_Id = ? AND st.seat_Id = ?";

		return jdbcTemplate.query(sql, new UpdateSeatBookingMapper(), (Object) userId, seatId);
	}

	@Override
	public void cancelMyBooking(int userId) {
		LOGGER.info("deleting the booking of a particular user based on user_id from the booking_mapping table");
		String deleteMappingQry = "DELETE FROM booking_mapping WHERE booking_id IN (SELECT booking_id FROM booking WHERE user_id = ?)";
		jdbcTemplate.update(deleteMappingQry, userId);
		LOGGER.info("deleting the booking of a particular user based on user_id from the booking table");
		String deleteBookingQry = "DELETE FROM booking WHERE user_id = ?";
		jdbcTemplate.update(deleteBookingQry, userId);
	}

	@Override
	public List<UserBookingHistoryModel> getBookingHistoryByUserId(int userId) throws DataBaseAccessException {
		LOGGER.info("Excuting the query to fetch Booking history of user...");
		String sql = "SELECT b.start_date, b.end_date, b.booking_type, b.parking_type, "
				+ "b.tea_coffee_type, b.booking_status, b.additional_desktop, b.lunch, "
				+ "s.seat_id, f.floor_name, sh.shift_name, sh.start_time, sh.end_time " + "FROM Booking b "
				+ "JOIN Seat s ON b.seat_id = s.seat_id " + "JOIN Floor f ON s.floor_id = f.floor_id "
				+ "JOIN Shift sh ON b.shift_id = sh.shift_id " + "WHERE b.user_id = ?";
		LOGGER.debug("Excuting the query to fetch Booking history of user:{} query:{}", userId, sql);

		return jdbcTemplate.query(sql, new UserHistoryBookingMapper(), userId);
	}

	public void updateUserPassword(int userId, String password) throws DataAccessException {
		String changePasswordQuery = "UPDATE USER SET PASSWORD=? WHERE user_id=?";
		jdbcTemplate.update(changePasswordQuery, password, userId);
	}

	@Override
	public LocalDateTime getLatestOtpCreationTimeByUserId(int userId) {
		String userOtpTime = "SELECT otp_creation_time FROM otp WHERE user_id = ? ORDER BY otp_creation_time DESC LIMIT 1";
		LOGGER.debug("Fetching creation time of latest OTP generated for user ID: {}", userId);
		LocalDateTime otpCreationTime = jdbcTemplate.queryForObject(userOtpTime, LocalDateTime.class, userId);
		LOGGER.debug("Last generated OTP for {} is: {} ", userId, otpCreationTime);

		return otpCreationTime;
	}

	@Override
	public String getLatestOtpByUserId(int userId) {
		String userOtp = "SELECT otp_value FROM otp WHERE user_id = ? ORDER BY otp_creation_time DESC LIMIT 1";
		LOGGER.debug("Fetching latest OTP generated for user ID: {}", userId);
		String otpValue = jdbcTemplate.queryForObject(userOtp, String.class, userId);
		LOGGER.debug("Last generated OTP for {} is: {} ", userId, otpValue);

		return otpValue;
	}

	@Override
	public void saveOtp(Otp otp) {
		String saveOtp = "INSERT INTO otp (user_id, otp_value) VALUES (?, ?)";
		rowsAffected = jdbcTemplate.update(saveOtp, otp.getUser().getUserId(), otp.getOtpValue());
		LOGGER.debug("Saving otpValue {} and userId {} into the otp table ", otp.getOtpValue(),
				otp.getUser().getUserId());
	}

	@Override
	public int getRowsAffected() {
		LOGGER.debug("Getting rows affected: {}", rowsAffected);

		return rowsAffected;
	}

	@Override
	public void updateUseForgetPassword(int userId, String newPassword) {
		String updateUserPasswordQuery = "UPDATE user SET password = ? WHERE user_id = ?";
		rowsAffected = jdbcTemplate.update(updateUserPasswordQuery, passwordEncoder.encode(newPassword), userId);
		LOGGER.debug("Password updated for user ID {}: Rows affected = {}", userId, rowsAffected);
	}
}