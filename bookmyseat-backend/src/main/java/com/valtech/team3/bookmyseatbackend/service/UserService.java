package com.valtech.team3.bookmyseatbackend.service;

import java.util.List;

import com.valtech.team3.bookmyseatbackend.entities.PasswordToken;
import com.valtech.team3.bookmyseatbackend.entities.Role;
import com.valtech.team3.bookmyseatbackend.entities.User;
import com.valtech.team3.bookmyseatbackend.models.UserModel;

public interface UserService {

	/**
	 * Saves the registered user details into the database.
	 *
	 * @param user The user model containing the details of the registered user to
	 *             be saved.
	 */
	void saveRegisteredUser(UserModel user);

	/**
	 * Allows user to change password.
	 *
	 * @param employeeId  Employee Id of user.
	 * @param oldPassword Existing password of user.
	 * @param newPassword Password that user want to update.
	 * @return True Based on change password successful.
	 */
	Boolean changeUserPassword(int employeeId, String oldPassword, String newPassword);

	/**
	 * Allows user to change password.
	 *
	 * @param employeeId  Employee Id of user.
	 * @param oldPassword Existing password of user.
	 * @param newPassword Password that user want to update.
	 * @return True Based on change password successful.
	 */
	String changeUserPhoneNumber(int id, long phoneNumber);

	/**
	 * Getting User object by email.
	 * 
	 * @param email of user.
	 * @return The User object.
	 */
	User getUserByEmail(String email);

	/**
	 * Approves the registration of the user.
	 *
	 * @param user The user whose registration will be approved.
	 */
	void approveUserRegistration(User user);

	/**
	 * Rejects the registration of the user.
	 *
	 * @param user The user whose registration will be rejected.
	 */
	void rejectUserRegistration(User user);

	/**
	 * Sends an email notification to the admin after a user registration.
	 *
	 * @param user The user model representing the user whose registration is
	 *             completed.
	 */
	void sendEmailToAdminAfterRegistration(User user);

	/**
	 * Retrieves the password token from the database based on the given token.
	 *
	 * @param token The token used to retrieve the password token.
	 * @return The PasswordToken object associated with the given token.
	 */
	PasswordToken getPasswordTokenByToken(String token);

	/**
	 * Updates the password for a user with the specified ID.
	 *
	 * @param id       The ID of the user whose password will be updated.
	 * @param password The new password for the user.
	 * @return A string indicating the result of the password update operation.
	 */
	String updateUserPassword(int id, String password);

	/**
	 * Generates a reset token for the given user.
	 * 
	 * @param user the user for whom the reset token is generated
	 * @return the generated reset token as a string
	 */
	String generateResetToken(User user);

	/**
	 * Retrieves a list of users who have been approved.
	 *
	 * @return A list of approved users.
	 */
	List<User> getApprovedUsers();

	/**
	 * Retrieves a list of users who are pending approval.
	 *
	 * @return A list of pending users.
	 */
	List<User> getPendingUsers();

	/**
	 * Retrieves a list of users who have been rejected.
	 *
	 * @return A list of rejected users.
	 */
	List<User> getRejectedUsers();

	/**
	 * Retrieves a list of all users from the database.
	 *
	 * @return A list containing all user objects stored in the database.
	 */
	List<User> getAllUsers();

	/**
	 * Updates the project assigned to a user.
	 *
	 * @param userModel The UserModel object containing the user's ID and the new
	 *                  project ID.
	 */
	void updateUserProject(UserModel userModel);

	/**
	 * Creates a new user based on the provided user model.
	 *
	 * @param userModel The UserModel object containing the details of the new user.
	 */
	void createNewUser(UserModel userModel);

	/**
	 * Retrieves the role of the user with the specified user ID.
	 * 
	 * @param userId the ID of the user whose role is to be retrieved
	 * @return the role of the user
	 */
	Role getUserRole(int userId);

	/**
	 * Retrieves the user associated with the specified booking ID.
	 *
	 * @param bookingId The ID of the booking to retrieve the user for.
	 * @return The user associated with the specified booking ID.
	 */
	User getUserByBookingId(int bookingId);
}