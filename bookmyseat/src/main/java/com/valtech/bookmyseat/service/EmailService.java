package com.valtech.bookmyseat.service;

import java.io.IOException;

import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.exception.EmailException;
import com.valtech.bookmyseat.model.UserModel;
import com.valtech.bookmyseat.model.UserModifyBooking;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

/**
 * The EmailService interface provides methods for generating and sending emails
 * to users and administrators related to seat booking approval and rejection.
 */
public interface EmailService {

	/**
	 * Generates the email content to be sent to the administrator.
	 *
	 * @param user The user for whom the email content is generated.
	 * @return The email content intended for the administrator.
	 * @throws Exception If there's an error generating the email content.
	 */
	String getEmailContentAdmin(User user) throws EmailException;

	/**
	 * Sends an approval email to the administrator.
	 *
	 * @param user The user whose approval email needs to be sent.
	 * @return True if the email is sent successfully; otherwise, false.
	 * @throws Exception If there's an error sending the approval email.
	 */
	void sendApprovalEmailToAdmin(User user) throws EmailException;

	/**
	 * Generates the email content for the user.
	 *
	 * @param userModel The model containing user information.
	 * @return The email content intended for the user.
	 * @throws EmailException If there's an error generating the email content.
	 */
	String getUserContent(UserModel userModel) throws EmailException;

	/**
	 * Sends an email to the user.
	 *
	 * @param userModel The model containing user information.
	 * @return True if the email is sent successfully; otherwise, false.
	 * @throws EmailException If there's an error sending the email to the user.
	 */
	boolean sendEmailToUser(UserModel userModel) throws EmailException;

	/**
	 * Generates the email content for approval.
	 *
	 * @param user The user for whom the approval email content is generated.
	 * @return The email content intended for user approval.
	 * @throws Exception If there's an error generating the approval email content.
	 */
	String getApprovalEmailContent(User user) throws EmailException;

	/**
	 * Sends an approval email to the user.
	 *
	 * @param user The user who needs to receive the approval email.
	 * @return True if the approval email is sent successfully; otherwise, false.
	 * @throws Exception If there's an error sending the approval email to the user.
	 */
	void sendApprovalEmailToUser(User user) throws EmailException;

	/**
	 * Generates the email content for rejection.
	 *
	 * @param user The user for whom the rejection email content is generated.
	 * @return The email content intended for user rejection.
	 * @throws Exception If there's an error generating the rejection email content.
	 */
	String getRejectionEmailContent(User user) throws EmailException;

	/**
	 * Sends a rejection email to the user.
	 *
	 * @param user The user who needs to receive the rejection email.
	 * @return True if the rejection email is sent successfully; otherwise, false.
	 * @throws Exception If there's an error sending the rejection email to the
	 *                   user.
	 */
	void sendRejectionEmailToUser(User user) throws EmailException;

	/**
	 * Sends an update seat email to the user based on the provided
	 * UserModifyBooking object.
	 * 
	 * @param userModifyBooking The UserModifyBooking object containing details
	 *                          about the booking modification.
	 * @throws EmailException If there is an issue sending the email.
	 */
	void sendUpdateSeatEmailToUser(UserModifyBooking userModifyBooking) throws EmailException;

	/**
	 * Generates the email content for updating a seat based on the provided
	 * UserModifyBooking object.
	 * 
	 * @param userModifyBooking The UserModifyBooking object containing details
	 *                          about the booking modification.
	 * @return A String representing the email content.
	 * @throws EmailException If there is an issue generating the email content.
	 */
	String getEmailContentForUpdateSeat(UserModifyBooking userModifyBooking) throws EmailException;

	/**
	 * Sends an OTP (One-Time Password) email to the user along with the OTP value.
	 * 
	 * @param user     The user object to whom the OTP email is being sent.
	 * @param otpValue The OTP value to be included in the email.
	 * @throws MessagingException If there is an issue with the messaging system.
	 * @throws IOException        If there is an issue with input/output operations.
	 * @throws TemplateException  If there is an issue with the email template.
	 */
	void sendOtpMailToUser(User user, String otpValue) throws MessagingException, IOException, TemplateException;

	/**
	 * Retrieves user OTP details including the OTP value.
	 * 
	 * @param user     The user object for whom the OTP details are retrieved.
	 * @param otpValue The OTP value associated with the user.
	 * @return A String containing the user OTP details.
	 * @throws IOException       If there is an issue with input/output operations.
	 * @throws TemplateException If there is an issue with the email template.
	 */
	String getUserOtpDetails(User user, String otpValue) throws IOException, TemplateException;
}