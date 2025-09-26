package com.valtech.team3.bookmyseatbackend.dao;

import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.RegisteredStatus;
import com.valtech.team3.bookmyseatbackend.entities.User;

/**
 * This is an interface for the UserDAO class.
 */
public interface UserDAO {

	/**
	 * Retrieves a user by their unique user ID.
	 *
	 * @param userId The unique ID of the user to retrieve.
	 * @return The user corresponding to the provided user ID, or null if not found.
	 */

	User getUserById(int userId);

	/**
	 * Retrieves a user by their email address.
	 *
	 * @param email The email address of the user to retrieve.
	 * @return The user corresponding to the provided email address, or null if not
	 *         found.
	 */
	User getUserByEmail(String email);

	/**
	 * Retrieves a user by their employee ID.
	 *
	 * @param employeeId The employee ID of the user to retrieve.
	 * @return The user corresponding to the provided employee ID, or null if not
	 *         found.
	 */
	User getUserByEmployeeId(int employeeId);

	/**
	 * Checks if an email address exists in the user database.
	 *
	 * @param email The email address to check for existence.
	 * @return True if the email address exists in the database, false otherwise.
	 */
	Boolean isEmailExists(String email);

	/**
	 * Checks if an employee ID exists in the user database.
	 *
	 * @param employeeId The employee ID to check for existence.
	 * @return True if the employee ID exists in the database, false otherwise.
	 */
	Boolean isEmployeeIdExists(int employeeId);

	/**
	 * Updates the registration status of a user.
	 *
	 * @param employeeId       The ID of the employee whose registration status will
	 *                         be updated.
	 * @param registeredStatus The new registration status to be set.
	 */
	void updateUserRegistrationStatus(int id, RegisteredStatus registeredStatus);

	/**
	 * Retrieves a list of admin users.
	 *
	 * @return A list of UserModel objects representing admin users.
	 */
	List<User> getAdminUsers();

	/**
	 * Creates a new user with the provided user model and role.
	 *
	 * @param user The user model containing the user's information.
	 * @param role The role of the user to be assigned.
	 */
	void createUser(User user);

	/**
	 * Updates user password.
	 *
	 * @param employee id of user
	 * @param password (new password) which he want to update.
	 */
	void updateUserPassword(int employeeId, String password);

	/**
	 * Updates user phone number.
	 *
	 * @param employee id of user
	 * @param updated  phone number of user.
	 */
	void updateUserPhoneNumber(int id, long phoneNumber);

	/**
	 * Retrieves a list of users based on their registration status.
	 *
	 * @param status The registration status of users to retrieve.
	 * @return A list of users with the specified registration status.
	 */
	List<User> getUsersByStatus(RegisteredStatus status);

	/**
	 * Retrieves a list of all users from the database.
	 *
	 * @return A list of User objects representing all users in the database.
	 */
	List<User> getAllUsers();

	/**
	 * Updates the project assigned to a user.
	 *
	 * @param userId    The unique identifier of the user whose project is to be
	 *                  updated.
	 * @param projectId The unique identifier of the project to assign to the user.
	 */
	void updateUserProject(int userId, int projectId);

	/**
	 * Retrieves the user associated with the specified booking ID.
	 *
	 * @param bookingId The ID of the booking to retrieve the user for.
	 * @return The user associated with the specified booking ID.
	 */
	User getUserByBookingId(int bookingId);

	Integer getLastInsertedUserId();
}