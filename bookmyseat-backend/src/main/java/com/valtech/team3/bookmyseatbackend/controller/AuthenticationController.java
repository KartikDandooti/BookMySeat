package com.valtech.team3.bookmyseatbackend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valtech.team3.bookmyseatbackend.entities.Role;
import com.valtech.team3.bookmyseatbackend.entities.User;
import com.valtech.team3.bookmyseatbackend.models.JwtAuthResponse;
import com.valtech.team3.bookmyseatbackend.models.LoginModel;
import com.valtech.team3.bookmyseatbackend.service.AuthenticationService;
import com.valtech.team3.bookmyseatbackend.service.UserService;

@RestController
@RequestMapping("/bookmyseat")
@CrossOrigin(origins = "*")
public class AuthenticationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginModel loginModel) {
		LOGGER.debug("Handling login request for the user with email: {} and passowrd: {}", loginModel.getEmail(), loginModel.getPassword());
		JwtAuthResponse jwtAuthResponse = authenticationService.login(loginModel);

		return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
	}

	@GetMapping("/userInfo")
	public ResponseEntity<Role> sendUserRole(@AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.getUserByEmail(userDetails.getUsername());

		return ResponseEntity.ok(userService.getUserRole(user.getId()));
	}
}