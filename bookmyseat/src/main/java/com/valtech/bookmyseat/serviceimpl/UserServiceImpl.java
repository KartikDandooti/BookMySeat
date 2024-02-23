package com.valtech.bookmyseat.serviceimpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.valtech.bookmyseat.configuration.JwtTokenProvider;
import com.valtech.bookmyseat.dao.UserDAO;
import com.valtech.bookmyseat.entity.Booking;
import com.valtech.bookmyseat.entity.Otp;
import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.exception.CustomDataAccessException;
import com.valtech.bookmyseat.exception.DataBaseAccessException;
import com.valtech.bookmyseat.exception.DuplicateEmailException;
import com.valtech.bookmyseat.exception.DuplicateUserIdException;
import com.valtech.bookmyseat.exception.EmailException;
import com.valtech.bookmyseat.exception.InvalidEmailIdException;
import com.valtech.bookmyseat.exception.UserNotFoundException;
import com.valtech.bookmyseat.model.JwtAuthResponse;
import com.valtech.bookmyseat.model.LoginRequest;
import com.valtech.bookmyseat.model.UserBookingHistoryModel;
import com.valtech.bookmyseat.model.UserModel;
import com.valtech.bookmyseat.model.UserModifyBooking;
import com.valtech.bookmyseat.service.EmailService;
import com.valtech.bookmyseat.service.UserService;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	private static final int OTP_LENGTH = 4;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	private Random random = new Random();

	@Override
	public JwtAuthResponse login(LoginRequest loginRequest) throws InvalidEmailIdException {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmailId(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String role = authentication.getAuthorities().stream().map(r -> r.getAuthority()).findFirst().orElse("");
		String token = jwtTokenProvider.generateToken(authentication);
		JwtAuthResponse response = new JwtAuthResponse();
		response.setAccessToken(token);
		response.setRole(role);
		response.setStatus(true);

		return response;
	}

	@Override
	public User findUserByEmail(String emailId) {
		return userDAO.getUserByEmail(emailId);
	}

	@Override
	public User createUserDetail(User user) throws DuplicateUserIdException, DuplicateEmailException, EmailException {
		LOGGER.info("Calling the createUserDetail to store the user");
		boolean existingUserEmail = userDAO.isEmailExists(user.getEmailId());
		boolean existingUserId = userDAO.isEmployeeIdExists(user.getUserId());
		if (existingUserEmail) {
			LOGGER.error("throwing DuplicateEmailIdException");
			throw new DuplicateEmailException("User with EmailId already Exists !: ");
		} else if (existingUserId) {
			LOGGER.error("throwing DuplicateUserIdException");
			throw new DuplicateUserIdException("User with UserId already Exists !: ");
		}
		emailService.sendApprovalEmailToAdmin(user);

		return userDAO.userRegistration(user);
	}

	@Override
	public String createUser(UserModel userModel) throws DuplicateEmailException, EmailException {
		boolean existingUserEmail = userDAO.isEmailExists(userModel.getEmailId());
		boolean existingUserId = userDAO.isEmployeeIdExists(userModel.getUserId());
		if (existingUserEmail && existingUserId) {
			LOGGER.error("User with EmailId and UserId already exists!");
			throw new DuplicateEmailException("User with EmailId and UserId already exists!");
		} else if (existingUserEmail) {
			LOGGER.error("User with EmailId already exists!");
			throw new DuplicateEmailException("User with EmailId already exists!");
		} else if (existingUserId) {
			LOGGER.error("User with  UserId already exists!");
			throw new DuplicateEmailException("User with UserId already exists!");
		} else {
			try {
				emailService.sendEmailToUser(userModel);
			} catch (Exception e) {
				LOGGER.error("Failed to send email to the user");
				throw new EmailException("Failed to send email to the user: " + e.getMessage());
			}
			return userDAO.createUser(userModel);
		}
	}

	@Override
	public void updateUserProfileByAdmin(UserModel userModel) {
		LOGGER.info("updating user profile");
		userDAO.updateUser(userModel);
	}

	@Override
	public User getUserById(int userId) {
		LOGGER.debug("Fetching UserId: {}", userId);

		return userDAO.getUserById(userId);
	}

	@Override
	public List<Map<String, Object>> getUserSeatInfo() {
		LOGGER.info("Attempting to retrieve user seat info");
		List<Map<String, Object>> userSeatInfo = userDAO.getUserSeatInfo();
		if (Objects.nonNull(userSeatInfo)) {
			LOGGER.info("Successfully retrieved user seat info");

			return userSeatInfo;
		} else {
			throw new DataBaseAccessException("Failed to retrieve user seat info");
		}
	}

	@Override
	public List<User> getAllUser(int userId) {
		return userDAO.getAllUserDetails(userId);
	}

	@Override
	public void updateUserSeat(int seatNumber, int floorId, int bookingId) throws EmailException {
		LOGGER.info("Successfully updated user seat");
		int seatId = userDAO.getSeatIdByNumberAndFloor(seatNumber, floorId);
		userDAO.updateUserSeat(seatId, bookingId);
		int userId = userDAO.getUserIdByBookingId(bookingId);
		List<UserModifyBooking> modifyBooking = userDAO.getBookingDeatilsOfUser(userId, seatId);
		UserModifyBooking modifyBookingOfUser = modifyBooking.get(0);
		emailService.sendUpdateSeatEmailToUser(modifyBookingOfUser);
		userDAO.updateUserSeat(seatId, bookingId);
	}

	@Override
	public void cancelUserSeat(int bookingId) {
		LOGGER.info("Successfully deleted user seat");
		userDAO.cancelUserBooking(bookingId);
	}

	@Override
	public List<Booking> getUserDashboardDetails(int userId) {
		LOGGER.debug("User dashboard details");

		return userDAO.getUserDashboardDetails(userId);
	}

	@Override
	public void cancelMyBooking(int userId) {
		LOGGER.info("Successfully canceled user booking");
		userDAO.cancelMyBooking(userId);
	}

	@Override
	public List<UserBookingHistoryModel> getBookingHistoryByUserId(int userId) throws DataBaseAccessException {
		LOGGER.info("Fetching the Booking history of user...");
		List<UserBookingHistoryModel> bookingHistories = userDAO.getBookingHistoryByUserId(userId);
		if (bookingHistories.isEmpty())
			throw new DataBaseAccessException("error occured while fetching booking history");
		LOGGER.info("Successfully Fetched the Booking history of user...");

		return bookingHistories;
	}

	public String updateUserPassword(int userId, String password) {
		try {
			LOGGER.info("Updating user phone number !");
			userDAO.updateUserPassword(userId, passwordEncoder.encode(password));

			return "Password changed successfully!";
		} catch (CustomDataAccessException e) {
			throw new DataBaseAccessException("Failed to Update Password. Please try again later !");
		}
	}

	@Override
	public boolean changeUserPassword(int userId, String currentPassword, String newPassword) {
		User user = userDAO.findUserByuserId(userId);
		LOGGER.debug("User with id:{} exist!", userId);
		if (Objects.isNull(user)) {
			LOGGER.error("User doesn't exist!");
			throw new UserNotFoundException("User not Found!");
		}
		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			return false;
		}
		userDAO.updateUserPassword(userId, passwordEncoder.encode(newPassword));

		return true;
	}

	@Override
	public String generateRandomOtp() {
		StringBuilder otp = new StringBuilder();
		for (int i = 0; i < OTP_LENGTH; i++) {
			otp.append(random.nextInt(10));
		}
		String generatedOtp = otp.toString();
		LOGGER.debug("Generated random OTP: {}", generatedOtp);

		return generatedOtp;
	}

	@Override
	public boolean generateUserOtp(String emailId) {
		LOGGER.debug("Generating OTP for email: {}", emailId);
		User user = userDAO.getUserByEmail(emailId);
		if (Objects.isNull(user)) {
			throw new NullPointerException("User not found for email: " + emailId);
		}
		Otp otp = new Otp();
		otp.setUser(user);
		otp.setOtpValue(generateRandomOtp());
		LOGGER.debug("Generated OTP for email {}: {}", emailId, otp.getOtpValue());
		userDAO.saveOtp(otp);

		return true;
	}

	@Override
	public String handleForgotPassword(String emailId) throws MessagingException, IOException, TemplateException {
		LOGGER.debug("Handling forgot password request for email: {}", emailId);
		User user = userDAO.getUserByEmail(emailId);
		if (Objects.nonNull(user)) {
			boolean otpGenerationResult = generateUserOtp(emailId);
			LOGGER.debug("OTP generation result for user {}: {}", user.getUserId(), otpGenerationResult);
			if (otpGenerationResult) {
				String otpValue = userDAO.getLatestOtpByUserId(user.getUserId());
				LOGGER.debug("Most recent OTP requested by user {}: {}", user.getUserId(), otpValue);
				if (Objects.nonNull(otpValue)) {
					emailService.sendOtpMailToUser(user, otpValue);
					LOGGER.debug("Sent OTP email to user {}", user.getUserId());

					return "OTP has successfully been sent, please check your provided email";
				} else {
					LOGGER.debug("No OTP value found for user {}", user.getUserId());

					return "Please check your email";
				}
			} else {
				LOGGER.debug("Failed to generate OTP for user {}", user.getUserId());

				return "Failed to generate OTP";
			}
		} else {
			LOGGER.debug("User with email {} doesn't exist", emailId);

			return "User doesn't exist";
		}
	}

	@Override
	public boolean verifyOtp(int userId, String enteredOtp) {
		LOGGER.debug("Verifying OTP for user {}: {}", userId, enteredOtp);
		String latestOtp = userDAO.getLatestOtpByUserId(userId);
		if (enteredOtp.equals(latestOtp)) {
			return isOtpValid(userId);
		} else {
			LOGGER.debug("Invalid OTP for user {}", userId);

			return false;
		}
	}

	public boolean isOtpValid(int userId) {
		LocalDateTime otpCreationTime = userDAO.getLatestOtpCreationTimeByUserId(userId);
		LOGGER.debug("Retrieved latest OTP creation time for user {}: {}", userId, otpCreationTime);
		LocalDateTime currentTime = LocalDateTime.now();
		long minutesDifference = ChronoUnit.MINUTES.between(otpCreationTime, currentTime);
		boolean isValid = minutesDifference < 2;
		LOGGER.debug("OTP validity for user {}: {}", userId, isValid);

		return isValid;
	}

	@Override
	public boolean updateUserForgotPassword(int userId, String newPassword) {
		LOGGER.debug("Updating password for user {}: New password = {}", userId, newPassword);
		userDAO.updateUseForgetPassword(userId, newPassword);
		int rowsAffected = userDAO.getRowsAffected();
		boolean isUpdated = rowsAffected > 0;
		LOGGER.debug("Password update result for user {}: Rows affected = {}, Updated = {}", userId, rowsAffected,
				isUpdated);

		return isUpdated;
	}
}