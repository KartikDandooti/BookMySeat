package com.valtech.bookmyseat.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import com.valtech.bookmyseat.entity.Role;
import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.service.CustomUserDetailService;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailTests {

	@InjectMocks
	private CustomUserDetailService customUserDetail;

	@Test
	void testGetAuthorities() {
		Role role = new Role();
		role.setRoleName("USER");
		User user = new User();
		user.setRole(role);
		CustomUserDetailService userDetail = new CustomUserDetailService(user);
		Collection<? extends GrantedAuthority> authorities = userDetail.getAuthorities();
		assertEquals(1, authorities.size());
		assertEquals("USER", authorities.iterator().next().getAuthority());
	}

	@Test
	void testGetPassword() {
		User user = new User();
		CustomUserDetailService userDetail = new CustomUserDetailService(user);
		String actualPassword = userDetail.getPassword();
		assertEquals(user.getPassword(), actualPassword);
	}

	@Test
	void testGetUsername() {
		User user = new User();
		CustomUserDetailService userDetail = new CustomUserDetailService(user);
		String actualUsername = userDetail.getUsername();
		assertEquals(user.getEmailId(), actualUsername);
	}

	@Test
	void testIsAccountNonExpired() {
		User user = new User();
		CustomUserDetailService userDetail = new CustomUserDetailService(user);
		boolean isAccountNonExpired = userDetail.isAccountNonExpired();
		assertTrue(isAccountNonExpired);
	}

	@Test
	void testIsAccountNonLocked() {
		User user = new User();
		CustomUserDetailService userDetail = new CustomUserDetailService(user);
		boolean isAccountNonLocked = userDetail.isAccountNonLocked();
		assertTrue(isAccountNonLocked);
	}

	@Test
	void testIsCredentialsNonExpired() {
		User user = new User();
		CustomUserDetailService userDetail = new CustomUserDetailService(user);
		boolean isCredentialsNonExpired = userDetail.isCredentialsNonExpired();
		assertTrue(isCredentialsNonExpired);
	}

	@Test
	void testIsEnabled() {
		User user = new User();
		CustomUserDetailService userDetail = new CustomUserDetailService(user);
		boolean isEnabled = userDetail.isEnabled();
		assertTrue(isEnabled);
	}
}