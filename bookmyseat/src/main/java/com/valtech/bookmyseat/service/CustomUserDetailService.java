package com.valtech.bookmyseat.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.valtech.bookmyseat.entity.User;

/**
 * CustomUserDetails is an implementation of the UserDetails interface for
 * representing user details in the context of Spring Security.
 */
public class CustomUserDetailService implements UserDetails {
	private static final long serialVersionUID = 1L;
	private transient User user;

	/**
	 * Constructs a CustomUserDetails object with the specified User.
	 *
	 * @param user The user for whom the details are being represented.
	 * @return
	 */
	public CustomUserDetailService(User user) {
		this.user = user;
	}

	/**
	 * Returns the authorities granted to the user.
	 *
	 * @return A list of GrantedAuthority objects representing the user's roles.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(() -> user.getRole().getRoleName());
	}

	/**
	 * Returns the user's password.
	 *
	 * @return The user's password.
	 */
	@Override
	public String getPassword() {

		return user.getPassword();
	}

	/**
	 * Returns the user's email address, used as the username.
	 *
	 * @return The user's email address.
	 */
	@Override
	public String getUsername() {
		return user.getEmailId();
	}

	/**
	 * Indicates whether the user's account has not expired.
	 *
	 * @return Always returns true as there is no account expiration check.
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * Indicates whether the user's account is not locked.
	 *
	 * @return Always returns true as there is no account locking check.
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * Indicates whether the user's credentials (password) are not expired.
	 *
	 * @return Always returns true as there is no credential expiration check.
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * Indicates whether the user is enabled.
	 *
	 * @return Always returns true as there is no account disabling check.
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
}