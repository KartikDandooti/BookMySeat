package com.valtech.team3.bookmyseatbackend.service.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.valtech.team3.bookmyseatbackend.models.JwtAuthResponse;
import com.valtech.team3.bookmyseatbackend.models.LoginModel;
import com.valtech.team3.bookmyseatbackend.security.JwtTokenProvider;
import com.valtech.team3.bookmyseatbackend.service.impl.AuthenticationServiceImpl;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private JwtTokenProvider jwtTokenProvider;
	@Mock
	private Authentication authentication;
	@Mock
	private SecurityContext securityContext;
	@InjectMocks
	private AuthenticationServiceImpl authenticationService;

	@Test
	void testAuthenticatesUserWithValidCredentialsAndRole() {
		LoginModel loginModel = new LoginModel();
		loginModel.setEmail("murali.kr@valtech.com");
		loginModel.setPassword("Murali@123");

		Authentication authentication = new UsernamePasswordAuthenticationToken(loginModel.getEmail(), loginModel.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("EMPLOYEE")));
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(securityContext);

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
		when(jwtTokenProvider.generateToken(authentication)).thenReturn("jwt_token");

		JwtAuthResponse response = authenticationService.login(loginModel);

		assertEquals("jwt_token", response.getAccessToken());
		assertEquals("EMPLOYEE", response.getRole());
	}

	@Test
	void testReturnsAppropriateTokenAndRoleForDifferentUseroles() {
		LoginModel loginModel = new LoginModel();
		loginModel.setEmail("laxman.kuddemmi@valtech.com");
		loginModel.setPassword("Laxman@123");

		Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
		Authentication authentication = new UsernamePasswordAuthenticationToken(loginModel.getEmail(), loginModel.getPassword(), authorities);
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

		String token = "valid_token";
		when(jwtTokenProvider.generateToken(authentication)).thenReturn(token);

		JwtAuthResponse response = authenticationService.login(loginModel);

		assertEquals(token, response.getAccessToken());
		assertEquals("ADMIN", response.getRole());
	}
}