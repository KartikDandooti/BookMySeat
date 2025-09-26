package com.valtech.team3.bookmyseatbackend.security;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.valtech.team3.bookmyseatbackend.dao.UserDAO;
import com.valtech.team3.bookmyseatbackend.entities.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private UserDAO userDAO;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LOGGER.info("Loading User Details");
		LOGGER.debug("Loading User Details of:{} ", username);

		User user = userDAO.getUserByEmail(username);
		if (Objects.isNull(user)) {
			LOGGER.error("User not found for Username:{} ", username);
			throw new UsernameNotFoundException("User not found");
		}

		return new CustomUserDetails(user);
	}
}