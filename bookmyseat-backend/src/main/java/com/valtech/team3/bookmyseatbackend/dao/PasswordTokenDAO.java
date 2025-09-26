package com.valtech.team3.bookmyseatbackend.dao;

import java.time.LocalDateTime;

import com.valtech.team3.bookmyseatbackend.entities.PasswordToken;

/**
 * This is an interface for managing password tokens. It provides methods for
 * creating, retrieving, checking existence, and updating password tokens.
 */
public interface PasswordTokenDAO {
	/**
	 * Creates a new password token.
	 *
	 * @param passwordToken the password token to be created
	 */
	void createPasswordToken(PasswordToken passwordToken);

	/**
	 * Retrieves the password token associated with the given token.
	 *
	 * @param token the token to search for
	 * @return the password token associated with the given token, or null if not
	 *         found
	 */
	PasswordToken getPasswordTokenByToken(String token);

	/**
	 * Checks if a password token exists for the specified user ID.
	 *
	 * @param userId the ID of the user
	 * @return true if a password token exists for the user, false otherwise
	 */
	Boolean isPasswordTokenExists(int userId);

	/**
	 * Updates the password token for the specified user.
	 *
	 * @param userId         the ID of the user
	 * @param token          the new token to be updated
	 * @param expirationDate the expirationDate to be updated
	 */
	void updatePasswordToken(int userId, String token, LocalDateTime expirationDate);
}