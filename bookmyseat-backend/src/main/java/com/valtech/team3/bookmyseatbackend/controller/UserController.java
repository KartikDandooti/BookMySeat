package com.valtech.team3.bookmyseatbackend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valtech.team3.bookmyseatbackend.entities.Booking;
import com.valtech.team3.bookmyseatbackend.entities.Holiday;
import com.valtech.team3.bookmyseatbackend.entities.PasswordToken;
import com.valtech.team3.bookmyseatbackend.entities.User;
import com.valtech.team3.bookmyseatbackend.models.ChangePasswordModel;
import com.valtech.team3.bookmyseatbackend.models.UserDashboardModel;
import com.valtech.team3.bookmyseatbackend.models.UserDashboardResponse;
import com.valtech.team3.bookmyseatbackend.models.UserModel;
import com.valtech.team3.bookmyseatbackend.service.BookingService;
import com.valtech.team3.bookmyseatbackend.service.EmailService;
import com.valtech.team3.bookmyseatbackend.service.HolidayService;
import com.valtech.team3.bookmyseatbackend.service.ProjectService;
import com.valtech.team3.bookmyseatbackend.service.UserService;

@RestController
@RequestMapping("/bookmyseat")
@CrossOrigin(origins = "*")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	private static final String SUCCESS_MESSAGE = "User Registered Successfully !";

	@Autowired
	private UserService userService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private BookingService bookingService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private HolidayService holidayService;

	@GetMapping("/registration")
	public ResponseEntity<List<String>> getRegisterUser() {
		LOGGER.info("Getting All Project Names !");

		return ResponseEntity.status(HttpStatus.OK).body(projectService.getAllProjectNames());
	}

	@PostMapping("/registration")
	public ResponseEntity<String> postRegisterUser(@RequestBody UserModel user) {
		LOGGER.info("Registering User !");
		userService.saveRegisteredUser(user);
		LOGGER.info("User registered successfully !");

		return ResponseEntity.status(HttpStatus.OK).body(SUCCESS_MESSAGE);
	}

	@PutMapping("/user/changepassword")
	public ResponseEntity<String> changeUserPassword(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ChangePasswordModel changePasswordModel) {
		LOGGER.info("Redirecting to Change Password Page");
		if (changePasswordModel.getCurrentPassword() == null || changePasswordModel.getNewPassword() == null) {
			LOGGER.error("Current password or new password cannot be null");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password or new password cannot be null");
		}

		User user = userService.getUserByEmail(userDetails.getUsername());
		LOGGER.debug("Changing Password of user with id: {}", user.getId());
		boolean passwordChanged = userService.changeUserPassword(user.getId(), changePasswordModel.getCurrentPassword(), changePasswordModel.getNewPassword());
		if (passwordChanged) {
			LOGGER.info("Password changed Successfully!");

			return ResponseEntity.ok("Password changed successfully!");
		} else {
			LOGGER.error("Password cannot be Changed");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to change password. Please enter correct old password");
		}
	}

	@GetMapping("/user/profile")
	public ResponseEntity<User> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
		LOGGER.info("Getting User details for their Profile !");

		return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByEmail(userDetails.getUsername()));
	}

	@PutMapping("/user/editprofile")
	public ResponseEntity<String> updateUserProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserModel userModel) {
		LOGGER.info("Editing User details !");
		User user = userService.getUserByEmail(userDetails.getUsername());

		return ResponseEntity.status(HttpStatus.OK).body(userService.changeUserPhoneNumber(user.getId(), userModel.getPhoneNumber()));
	}

	@GetMapping("/user/dashboard")
	public ResponseEntity<UserDashboardResponse> getUserDashboard(@AuthenticationPrincipal UserDetails userDetails) {
		LOGGER.info("Fetching All Future Bookings and attendance For the User");
		User user = userService.getUserByEmail(userDetails.getUsername());
		List<UserDashboardModel> userDashboard = bookingService.getUserDashboard(user.getId());
		int attendanceCount = bookingService.getAttendanceCountForCurrentMonth(user.getId());
		UserDashboardResponse response = new UserDashboardResponse();
		response.setBookings(userDashboard);
		response.setAttendanceCount(attendanceCount);
		response.setUser(user);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/user/bookinghistory")
	public ResponseEntity<List<Booking>> getBookingBeforeCurrentDate(@AuthenticationPrincipal UserDetails userDetails) {
		LOGGER.info("Redirecting user to booking history page");
		User user = userService.getUserByEmail(userDetails.getUsername());
		LOGGER.info("Fetching User Details");
		List<Booking> bookings = bookingService.getAllBookings(user.getId());
		LOGGER.info("Dispalying the booking history of specific user !");

		return ResponseEntity.status(HttpStatus.OK).body(bookings);
	}

	@PostMapping("/forgotpassword")
	public ResponseEntity<String> processForgotPassword(@RequestBody UserModel userModel) {
		User user = userService.getUserByEmail(userModel.getEmail());
		String token = userService.generateResetToken(user);
		LOGGER.info("Sending Mail for password reset");

		return ResponseEntity.status(HttpStatus.OK).body(emailService.sendChangePasswordEmail(user, token));
	}

	@GetMapping("/resetpassword/{token}")
	public ResponseEntity<PasswordToken> resetPasswordForm(@PathVariable String token) {
		LOGGER.info("Redirecting to password reset page");

		return ResponseEntity.status(HttpStatus.OK).body(userService.getPasswordTokenByToken(token));
	}

	@PutMapping("/resetpassword/{token}")
	public ResponseEntity<String> processResetPassword(@RequestBody ChangePasswordModel changePasswordModel) {
		LOGGER.info("Updating the user password");
		User user = userService.getUserByEmail(changePasswordModel.getEmail());

		return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserPassword(user.getId(), changePasswordModel.getNewPassword()));
	}

	@GetMapping("/getFutureHolidays")
	public ResponseEntity<List<Holiday>> getAllFutureHolidays() {
		LOGGER.info("Getting all future holidays");

		return ResponseEntity.status(HttpStatus.OK).body(holidayService.getAllFutureHolidays());
	}
}