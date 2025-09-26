package com.valtech.team3.bookmyseatbackend.service;

import com.valtech.team3.bookmyseatbackend.models.JwtAuthResponse;
import com.valtech.team3.bookmyseatbackend.models.LoginModel;

public interface AuthenticationService {

	/**
	 * Authenticating User Details
	 *
	 * @param email    of user.
	 * @param password of user.
	 * @return JwtAuthResponse object which has fields Jwt token, token type,
	 *         UserRole.
	 * 
	 */
	JwtAuthResponse login(LoginModel loginModel);
}