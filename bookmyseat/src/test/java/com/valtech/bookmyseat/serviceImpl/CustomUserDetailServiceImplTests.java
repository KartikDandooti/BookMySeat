package com.valtech.bookmyseat.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.valtech.bookmyseat.dao.UserDAO;
import com.valtech.bookmyseat.entity.ApprovalStatus;
import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.serviceimpl.CustomUserDetailServiceImpl;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceImplTests {

	@Mock
	private UserDAO userDAO;

	@InjectMocks
	private CustomUserDetailServiceImpl customUserDetailService;

	@Test
	void testLoadUserByUsername_UserFoundAndApproved() {
		String username = "test@example.com";
		User user = new User();
		user.setEmailId(username);
		user.setApprovalStatus(ApprovalStatus.APPROVED);
		when(userDAO.getUserByEmail(username)).thenReturn(user);
		UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
		assertEquals(username, userDetails.getUsername());
	}

	@Test
	void testLoadUserByUsername_UserNotFound() {
		String username = "nonexistent@example.com";
		when(userDAO.getUserByEmail(username)).thenReturn(null);
		assertThrows(UsernameNotFoundException.class, () -> customUserDetailService.loadUserByUsername(username));
	}

	@Test
	void testLoadUserByUsername_UserFoundButNotApproved() {
		String username = "unapproved@example.com";
		User user = new User();
		user.setEmailId(username);
		user.setApprovalStatus(ApprovalStatus.PENDING);
		when(userDAO.getUserByEmail(username)).thenReturn(user);
		assertThrows(UsernameNotFoundException.class, () -> customUserDetailService.loadUserByUsername(username));
	}
}