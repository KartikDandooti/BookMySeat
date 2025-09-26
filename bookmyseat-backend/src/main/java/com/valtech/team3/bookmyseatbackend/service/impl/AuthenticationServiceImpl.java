package com.valtech.team3.bookmyseatbackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.team3.bookmyseatbackend.models.JwtAuthResponse;
import com.valtech.team3.bookmyseatbackend.models.LoginModel;
import com.valtech.team3.bookmyseatbackend.security.JwtTokenProvider;
import com.valtech.team3.bookmyseatbackend.service.AuthenticationService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public JwtAuthResponse login(LoginModel loginModel) {
		LOGGER.info("Authenticating User Details ");
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginModel.getEmail(), loginModel.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		LOGGER.info("Generating JWT token for User");
		String token = jwtTokenProvider.generateToken(authentication);
		String role = authentication.getAuthorities().stream().map(r -> r.getAuthority()).findFirst().orElse("");
		JwtAuthResponse response = new JwtAuthResponse();
		LOGGER.info("Sending Authentiaction response");
		response.setAccessToken(token);
		response.setRole(role);

		return response;
	}
}