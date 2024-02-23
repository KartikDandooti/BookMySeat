package com.valtech.bookmyseat.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.valtech.bookmyseat.entity.Booking;
import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.exception.DataBaseAccessException;
import com.valtech.bookmyseat.exception.DuplicateEmailException;
import com.valtech.bookmyseat.exception.DuplicateUserIdException;
import com.valtech.bookmyseat.exception.EmailException;
import com.valtech.bookmyseat.exception.InvalidEmailIdException;
import com.valtech.bookmyseat.model.JwtAuthResponse;
import com.valtech.bookmyseat.model.LoginRequest;
import com.valtech.bookmyseat.model.UserBookingHistoryModel;
import com.valtech.bookmyseat.model.UserModel;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

/**
 * Service interface for handling user-related operations.
 */
public interface UserService {

	/**
	 * Authenticates a user using the provided login credentials.
	 * 
	 * @param loginRequest The login request containing user credentials.
	 * @return JwtAuthResponse containing authentication token and user details.
	 */
	JwtAuthResponse login(LoginRequest loginRequest) throws InvalidEmailIdException;

	/**
	 * Creates a new user with the provided details.
	 * 
	 * @param user The user object to be created.
	 * @return The created user object.
	 * @throws DuplicateEmailException  if the email is already associated with
	 *                                  another user.
	 * @throws DuplicateUserIdException if the user ID is already taken.
	 */
	User createUserDetail(User user) throws DuplicateEmailException, DuplicateUserIdException, EmailException;

	/**
	 * Finds a user by their email address.
	 * 
	 * @param emailId The email address of the user to find.
	 * @return The user object associated with the email, or null if not found.
	 */
	User findUserByEmail(String emailId);

	/**
	 * Creates a new user based on the provided user model.
	 * 
	 * @param userModel The UserModel object containing the details of the user to
	 *                  be created.
	 * @return A string indicating the success of the user creation process.
	 * @throws DuplicateEmailException If the email provided in the userModel
	 *                                 already exists in the system.
	 */
	String createUser(UserModel userModel) throws DuplicateEmailException, EmailException;

	/**
	 * Updates a user profile by an administrator.
	 * 
	 * @param userId    The ID of the user whose profile is to be updated.
	 * @param userModel The UserModel object containing the updated user profile
	 *                  information.
	 */
	void updateUserProfileByAdmin(UserModel userModel);

	/**
	 * Retrieves a user by their ID.
	 * 
	 * @param userId The ID of the user to retrieve.
	 * @return The User object representing the user with the specified ID.
	 */
	User getUserById(int userId);

	/**
	 * Retrieves information about user seat assignments from the database.
	 *
	 * @return A list of mappings containing seat information for each user. Each
	 *         mapping represents a user's seat information and contains key-value
	 *         pairs where the keys are strings representing attribute names and the
	 *         values are objects representing corresponding attribute values. The
	 *         list may be empty if no seat information is found.
	 * @throws DataBaseAccessException If there is an error accessing the database
	 *                                 while retrieving user seat information.
	 */
	List<Map<String, Object>> getUserSeatInfo() throws DataBaseAccessException;

	/**
	 * Retrieves a list of users based on the provided user ID.
	 *
	 * @param userId The ID of the user for which to retrieve the list of users.
	 * @return A list of User objects representing the users retrieved based on the
	 *         provided user ID. If no users are found matching the provided user
	 *         ID, an empty list is returned.
	 * @throws SomeException (Optional) Throws an exception if there is an error
	 *                       retrieving the list of users.
	 */
	List<User> getAllUser(int userId);

	/**
	 * Retrieves the booking details for a particular user to display on their
	 * dashboard.
	 * 
	 * @param userId The ID of the user whose booking details are being retrieved.
	 * @return A list of Booking objects containing details about the bookings made
	 *         by the user.
	 */
	List<Booking> getUserDashboardDetails(int userId);

	/**
	 * Updates the seat details associated with a booking.
	 * 
	 * @param seatNumber The new seat number to be assigned.
	 * @param floorId    The ID of the floor where the seat is located.
	 * @param bookingId  The ID of the booking to be updated.
	 */
	void updateUserSeat(int seatNumber, int floorId, int bookingId) throws EmailException;

	/**
	 * Cancels a user's booking and frees up the associated seat.
	 * 
	 * @param bookingId The ID of the booking to be canceled.
	 */
	void cancelUserSeat(int bookingId);

	/**
	 * Cancels the booking(s) associated with the specified user ID.
	 *
	 * @param userId the unique identifier of the user whose booking(s) are to be
	 *               canceled
	 */
	void cancelMyBooking(int userId);

	/**
	 * Retrieves a list of booking histories for a particular user based on their
	 * userId.
	 *
	 * @param userId the unique identifier of the user whose booking histories are
	 *               to be retrieved
	 * @return a list of UserBookingHistory objects representing the booking
	 *         histories of the user
	 * @throws DataBaseAccessException if there is an error accessing the database
	 */
	List<UserBookingHistoryModel> getBookingHistoryByUserId(int userId) throws DataBaseAccessException;

	/**
	 * Updates the password for the user with the specified user ID. This method
	 * sets the user's password to the provided password.
	 *
	 * @param userId   the ID of the user whose password is to be updated
	 * @param password the new password to set for the user
	 * @return the updated password for the user
	 */
	String updateUserPassword(int userId, String password);

	/**
	 * Changes the password for the user with the specified user ID. This method
	 * verifies the current password before updating it to the new password.
	 *
	 * @param userId          the ID of the user whose password is to be changed
	 * @param currentPassword the current password of the user
	 * @param newPassword     the new password to set for the user
	 * @return {@code true} if the password is successfully changed, {@code false}
	 *         otherwise
	 */
	boolean changeUserPassword(int userId, String currentPassword, String newPassword);

	/**
	 * Handles the forgot password functionality for the provided email ID. Sends an
	 * email containing a one-time password (OTP) to the user's email address to
	 * allow resetting the password.
	 *
	 * @param emailId The email ID of the user who forgot the password.
	 * @return A message indicating the result of the operation.
	 * @throws MessagingException If an error occurs while sending the email.
	 * @throws IOException        If an I/O error occurs while processing the email
	 *                            template.
	 * @throws TemplateException  If an error occurs while processing the email
	 *                            template.
	 */
	String handleForgotPassword(String emailId) throws MessagingException, IOException, TemplateException;

	/**
	 * Generates a one-time password (OTP) for the user associated with the provided
	 * email ID.
	 *
	 * @param emailId The email ID of the user for whom the OTP is to be generated.
	 * @return True if the OTP is generated successfully; otherwise, false.
	 */
	boolean generateUserOtp(String emailId);

	/**
	 * Generates a random one-time password (OTP).
	 *
	 * @return A randomly generated one-time password (OTP).
	 */
	String generateRandomOtp();

	/**
	 * Verifies the entered OTP against the stored OTP for the specified user.
	 *
	 * @param userId     The ID of the user for whom the OTP verification is
	 *                   performed.
	 * @param enteredOtp The OTP entered by the user for verification.
	 * @return True if the entered OTP matches the stored OTP for the user;
	 *         otherwise, false.
	 */
	boolean verifyOtp(int userId, String enteredOtp);

	/**
	 * Updates the password for the user with the specified user ID to the new
	 * password provided.
	 *
	 * @param userId      The ID of the user whose password needs to be updated.
	 * @param newPassword The new password to be set for the user.
	 * @return True if the password is updated successfully; otherwise, false.
	 */
	boolean updateUserForgotPassword(int userId, String newPassword);

}