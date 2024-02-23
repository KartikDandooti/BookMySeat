package com.valtech.bookmyseat.serviceimpl;

import java.util.Objects;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.valtech.bookmyseat.dao.UserDAO;
import com.valtech.bookmyseat.entity.ApprovalStatus;
import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.service.CustomUserDetailService;

/**
 * CustomUserDetailsService is an implementation of the Spring Security
 * UserDetailsService interface. It provides functionality to load user details
 * by username, specifically designed for user authentication.
 */
@Service
public class CustomUserDetailServiceImpl implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailServiceImpl.class);

	@Autowired
	private UserDAO userDAO;

	/**
	 * Attempts to load user details by the provided username.
	 *
	 * @param username The username (email) of the user to be loaded.
	 * @return An implementation of UserDetails representing the loaded user.
	 * @throws UsernameNotFoundException If the user with the given username is not
	 *                                   found.
	 * @throws ServiceException          If an unexpected service error occurs
	 *                                   during user loading.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Attempting to load user by email:{}", username);
		User user = userDAO.getUserByEmail(username);
		if (Objects.nonNull(user) && user.getApprovalStatus() == ApprovalStatus.APPROVED) {
			logger.info("Successfully loaded user by email:{}", username);

			return new CustomUserDetailService(user);
		}
		logger.warn("User with email '{}' not found.", username);
		throw new UsernameNotFoundException("User not found");
	}
}