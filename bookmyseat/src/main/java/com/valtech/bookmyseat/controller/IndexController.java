package com.valtech.bookmyseat.controller;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.exception.DuplicateEmailException;
import com.valtech.bookmyseat.exception.DuplicateUserIdException;
import com.valtech.bookmyseat.exception.EmailException;
import com.valtech.bookmyseat.exception.InvalidEmailIdException;
import com.valtech.bookmyseat.model.ChangePasswordModel;
import com.valtech.bookmyseat.model.JwtAuthResponse;
import com.valtech.bookmyseat.model.LoginRequest;
import com.valtech.bookmyseat.model.UserModel;
import com.valtech.bookmyseat.service.UserService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/bookmyseat")
public class IndexController {
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginRequest loginRequest)
			throws BadCredentialsException, InvalidEmailIdException {
		JwtAuthResponse jwtAuthResponse = userService.login(loginRequest);

		return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
	}

//	@PostMapping("/registration")
//	public ResponseEntity<String> createUser(@RequestBody UserModel userModel) throws EmailException {
//		try {
//			LOGGER.info("handling the registration request");
//			userService.createUserDetail(userModel.getUserDetails());
//
//			return new ResponseEntity<>("User registered Succesfully", HttpStatus.OK);
//		} catch (DuplicateEmailException e) {
//			LOGGER.error("Handling DuplicateEmailException:{}", e.getMessage());
//
//			return ResponseEntity.badRequest().body(e.getMessage());
//		} catch (DuplicateUserIdException e) {
//			LOGGER.error("Handling DuplicateUserIdException:{}", e.getMessage());
//
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//	}

	@PutMapping("/user/changepassword")
	public ResponseEntity<String> changeUserPassword(@AuthenticationPrincipal UserDetails userDetails,
			@RequestBody ChangePasswordModel changePasswordModel) {
		LOGGER.info("Redirecting to Change Password Page");
		if (Objects.isNull(changePasswordModel.getCurrentPassword())
				|| Objects.isNull(changePasswordModel.getNewPassword())) {
			LOGGER.error("Current password or new password cannot be null");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Current password or new password cannot be null");
		}
		User user = userService.findUserByEmail(userDetails.getUsername());
		LOGGER.debug("Changing Password of user with id: {}", user.getUserId());
		boolean passwordChanged = userService.changeUserPassword(user.getUserId(),
				changePasswordModel.getCurrentPassword(), changePasswordModel.getNewPassword());
		if (passwordChanged) {
			LOGGER.info("Password changed Successfully!");

			return ResponseEntity.ok("Password changed successfully!");
		} else {
			LOGGER.error("Password cannot be Changed");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Failed to change password. Please enter correct old password");
		}
	}
}
