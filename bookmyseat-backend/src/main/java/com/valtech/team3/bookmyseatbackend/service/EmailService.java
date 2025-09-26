package com.valtech.team3.bookmyseatbackend.service;

import com.valtech.team3.bookmyseatbackend.entities.User;

import jakarta.mail.MessagingException;

/**
 * Interface for sending email notifications to users and administrators.
 */
public interface EmailService {

	/**
	 * Sends an email notification to administrators.
	 *
	 * @param user The user model associated with the email notification.
	 * @throws MessagingException If an error occurs while sending the email.
	 */
	void sendEmailToAdmins(User user) throws MessagingException;

	/**
	 * Sends an approval email notification to a user.
	 *
	 * @param user The user model associated with the email notification.
	 * @throws MessagingException If an error occurs while sending the email.
	 */
	void sendApprovalEmail(User user) throws MessagingException;

	/**
	 * Sends a rejection email notification to a user.
	 *
	 * @param user The user model associated with the email notification.
	 * @throws MessagingException If an error occurs while sending the email.
	 */
	void sendRejectionEmail(User user) throws MessagingException;

	/**
	 * Sends an email notification to the user for changing password.
	 *
	 * @param user The user object representing the user for whom the email
	 *             notification is sent.
	 * @return A string indicating the status of the email notification.
	 */

	String sendChangePasswordEmail(User user, String token);

	/**
	 * Sends a seat modification notification email to the specified user.
	 *
	 * @param user The user to whom the email will be sent.
	 * @throws MessagingException if an error occurs while sending the email.
	 */
	void sendSeatModificationMail(User user) throws MessagingException;

	/**
	 * Sends a seat cancellation notification email to the specified user.
	 *
	 * @param user The user to whom the email will be sent.
	 * @throws MessagingException if an error occurs while sending the email.
	 */
	void sendSeatCanceledMail(User user) throws MessagingException;

	/**
	 * Sends a welcome email to the specified user.
	 * 
	 * @param user The user to whom the welcome email will be sent.
	 * @throws MessagingException       If an error occurs while sending the email.
	 * @throws IllegalArgumentException If the user parameter is null.
	 */
	void sendWelcomeEmail(User user);
}