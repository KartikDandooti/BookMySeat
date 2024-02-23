package com.valtech.bookmyseat.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

import com.valtech.bookmyseat.entity.Booking;
import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.model.OtpModel;
import com.valtech.bookmyseat.model.UserBookingHistoryModel;
import com.valtech.bookmyseat.model.UserForgotPasswordModel;
import com.valtech.bookmyseat.model.UserModel;
import com.valtech.bookmyseat.model.UserResetPasswordModel;
import com.valtech.bookmyseat.service.UserService;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/bookmyseat/user")
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping("/booking-history")
	public List<UserBookingHistoryModel> getBookingHistoryByUserId(@AuthenticationPrincipal UserDetails userDetails) {
		LOGGER.info("handling the request of Booking History for user...");
		User user = userService.findUserByEmail(userDetails.getUsername());
		LOGGER.debug("handling the request of Booking History for user with userId:{}", user.getUserId());

		return userService.getBookingHistoryByUserId(user.getUserId());
	}

	@GetMapping("/userdashboard/{userId}")
	public List<Booking> getDashboardDetails(@PathVariable int userId) {
		LOGGER.info("User Dashboard details");

		return userService.getUserDashboardDetails(userId);
	}

//	@PostMapping("/forgot-password")
//	public ResponseEntity<?> handleForgotPasswordRequest(@RequestBody UserModel userModel) throws MessagingException,
//			IOException, TemplateException, EmptyResultDataAccessException, NullPointerException {
//		String email = userModel.getEmailId();
//		User user = userService.findUserByEmail(email);
//		int userId = user.getUserId();
//		if (Objects.isNull(email)) {
//			LOGGER.debug("No user input for email");
//
//			return ResponseEntity.badRequest().body("Email is required");
//		} else {
//			userService.handleForgotPassword(email);
//			LOGGER.debug("Sending OTP to user whose email is: {}", email);
//			UserForgotPasswordModel forgotPasswordResponse = new UserForgotPasswordModel();
//			forgotPasswordResponse.setResponse("OTP has successfully been sent, please check your provided email");
//			forgotPasswordResponse.setUserId(userId);
//			LOGGER.debug("Sending confirmation response for user: {}", user);
//
//			return new ResponseEntity<>(forgotPasswordResponse, HttpStatus.OK);
//		}
//	}

	@PostMapping("/verify-otp/{userId}")
	public ResponseEntity<String> verifyOtp(@PathVariable int userId, @RequestBody OtpModel otpModel) {
		String otp = otpModel.getOtpValue();
		if (userService.verifyOtp(userId, otp)) {
			LOGGER.debug("OTP verified successfully for user ID: {}", userId);

			return ResponseEntity.ok("OTP verified successfully");
		} else {
			return ResponseEntity.ok("OTP is not Valid");
		}
	}

	@PutMapping("/reset-password/{userId}")
	public ResponseEntity<String> updatePassword(@PathVariable Integer userId,
			@RequestBody UserResetPasswordModel userResetPasswordModel) {
		String newPassword = userResetPasswordModel.getNewPassword();
		if (userService.updateUserForgotPassword(userId, newPassword)) {
			LOGGER.debug("Password updated successfully for user ID: {}", userId);

			return ResponseEntity.ok("Password updated successfully");
		} else {
			LOGGER.debug("Password update failed for user ID: {}", userId);

			return ResponseEntity.badRequest().body("Password update failed, please try again");
		}
	}
}