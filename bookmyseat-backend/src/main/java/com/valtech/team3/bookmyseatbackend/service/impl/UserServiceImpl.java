package com.valtech.team3.bookmyseatbackend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.team3.bookmyseatbackend.dao.PasswordTokenDAO;
import com.valtech.team3.bookmyseatbackend.dao.ProjectDAO;
import com.valtech.team3.bookmyseatbackend.dao.RoleDAO;
import com.valtech.team3.bookmyseatbackend.dao.UserDAO;
import com.valtech.team3.bookmyseatbackend.dao.UserShiftsDAO;
import com.valtech.team3.bookmyseatbackend.entities.PasswordToken;
import com.valtech.team3.bookmyseatbackend.entities.Project;
import com.valtech.team3.bookmyseatbackend.entities.RegisteredStatus;
import com.valtech.team3.bookmyseatbackend.entities.Role;
import com.valtech.team3.bookmyseatbackend.entities.User;
import com.valtech.team3.bookmyseatbackend.exceptions.DataBaseAccessException;
import com.valtech.team3.bookmyseatbackend.exceptions.DuplicateEmployeeException;
import com.valtech.team3.bookmyseatbackend.exceptions.EmailSendingException;
import com.valtech.team3.bookmyseatbackend.exceptions.UserNotFoundException;
import com.valtech.team3.bookmyseatbackend.models.UserModel;
import com.valtech.team3.bookmyseatbackend.service.EmailService;
import com.valtech.team3.bookmyseatbackend.service.UserService;

import jakarta.mail.MessagingException;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	private static final String EMAIL_ERROR = "Error sending email. Please try again later !";

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ProjectDAO projectDAO;
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private EmailService emailService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private PasswordTokenDAO passwordTokenDAO;
	@Autowired
	private UserShiftsDAO userShiftsDAO;

	@Override
	public void saveRegisteredUser(UserModel userModel) {
		LOGGER.info("Validating registered User !");
		validateUser(userModel);
		LOGGER.info("User Validated !");
		User user = userModel.getUserDetails();
		Role role = roleDAO.getRoleByRoleName("EMPLOYEE");
		Project project = projectDAO.getProjectByProjectName(userModel.getProjectName());
		String password = passwordEncoder.encode(user.getPassword());
		LocalDateTime registeredDate = LocalDateTime.now();
		user.setRole(role);
		user.setProject(project);
		user.setPassword(password);
		user.setCreatedDate(registeredDate);
		user.setRegisteredStatus(RegisteredStatus.PENDING);
		userDAO.createUser(user);
		
		Integer userId = userDAO.getLastInsertedUserId();
		user.setId(userId);
		userShiftsDAO.addUserShift(userId, 2);
		userShiftsDAO.addUserShift(userId, 3);
		
		sendEmailToAdminAfterRegistration(user);
		LOGGER.info("User created !");
	}

	public void validateUser(UserModel user) {
		Boolean emailExists = userDAO.isEmailExists(user.getEmail());
		Boolean employeeIdExists = userDAO.isEmployeeIdExists(user.getEmployeeId());
		if (emailExists || employeeIdExists) {
			LOGGER.warn("User with Email: {} or EmployeeId: {} already exists !", user.getEmail(),
					user.getEmployeeId());
			throw new DuplicateEmployeeException("Employee with Email or Employee Id already exists !");
		}
	}

	@Override
	public void createNewUser(UserModel userModel) {
		LOGGER.info("Validating newly Created User !");
		validateUser(userModel);
		LOGGER.info("User Validated !");
		User user = userModel.getUserDetails();
		Role role = roleDAO.getRoleByRoleName("EMPLOYEE");
		Project project = projectDAO.getProjectById(userModel.getProjectId());
		String phoneNumber = String.valueOf(user.getPhoneNumber());
		String passwordFirstPart = user.getFirstName().substring(0, Math.min(user.getFirstName().length(), 3));
		String passwordSecondPart = user.getLastName().substring(0, Math.min(user.getLastName().length(), 3));
		String phoneNumberLastDigits = phoneNumber.substring(Math.max(0, phoneNumber.length() - 4));
		StringBuilder passwordBuilder = new StringBuilder();
		passwordBuilder.append(passwordFirstPart);
		passwordBuilder.append(passwordSecondPart);
		passwordBuilder.append("@");
		passwordBuilder.append(phoneNumberLastDigits);
		String password = passwordBuilder.toString();
		String generatedPassword = passwordEncoder.encode(password);
		user.setRole(role);
		user.setProject(project);
		user.setPassword(generatedPassword);
		user.setCreatedDate(LocalDateTime.now());
		user.setRegisteredStatus(RegisteredStatus.APPROVED);
		userDAO.createUser(user);
		
		Integer userId = userDAO.getLastInsertedUserId();
		user.setId(userId);
		userShiftsDAO.addUserShift(userId, 2);
		userShiftsDAO.addUserShift(userId, 3);
		
		emailService.sendWelcomeEmail(user);
	}

	@Override
	public List<User> getAllUsers() {
		try {
			LOGGER.info("Retrieving all Users from Database !");

			return userDAO.getAllUsers();
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Error fetching Users from the Database !");
		}
	}

	@Override
	public Boolean changeUserPassword(int id, String currentPassword, String newPassword) {
		User user = userDAO.getUserById(id);
		LOGGER.debug("User with id:{} exist!", id);
		if (user == null) {
			LOGGER.error("User doesn't exist!");
			throw new UserNotFoundException("User not Found!");
		}
		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			return false;
		}
		userDAO.updateUserPassword(id, passwordEncoder.encode(newPassword));

		return true;
	}

	@Override
	public User getUserByEmail(String email) {
		try {
			LOGGER.info("Fetching User by email !");
			return userDAO.getUserByEmail(email);
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("User not found !");
		}
	}

	@Override
	public void sendEmailToAdminAfterRegistration(User user) {
		try {
			emailService.sendEmailToAdmins(user);
			LOGGER.info("Email sent to admin after user registration");
		} catch (MailException | MessagingException e) {
			LOGGER.error("Error sending mail to admin : {}", e.getMessage());
			throw new EmailSendingException(EMAIL_ERROR);
		}
	}

	@Async
	@Override
	public void approveUserRegistration(User user) {
		user.setRegisteredStatus(RegisteredStatus.APPROVED);
		userDAO.updateUserRegistrationStatus(user.getId(), user.getRegisteredStatus());
		LOGGER.info("Registration status changed to APPROVED !");
		try {
			processStatusAndSendEmail(user);
			LOGGER.info("Approval Mail sent !");
		} catch (MailException | MessagingException e) {
			LOGGER.error("Error sending approval mail : {}", e.getMessage());
			throw new EmailSendingException(EMAIL_ERROR);
		}
	}

	@Async
	@Override
	public void rejectUserRegistration(User user) {
		user.setRegisteredStatus(RegisteredStatus.REJECTED);
		userDAO.updateUserRegistrationStatus(user.getId(), user.getRegisteredStatus());
		LOGGER.info("Registration status changed to REJECTED");
		try {
			processStatusAndSendEmail(user);
			LOGGER.info("Rejection mail sent");
		} catch (MailException | MessagingException e) {
			LOGGER.error("Error sending rejection mail : {}", e.getMessage());
			throw new EmailSendingException(EMAIL_ERROR);
		}
	}

	public void processStatusAndSendEmail(User user) throws MessagingException {
		LOGGER.info("Processing registration status for user {} ", user.getEmail());
		if (RegisteredStatus.APPROVED.equals(user.getRegisteredStatus())) {
			emailService.sendApprovalEmail(user);
			LOGGER.info("Approval email sent successfully to user {}", user.getEmail());
		} else if (RegisteredStatus.REJECTED.equals(user.getRegisteredStatus())) {
			emailService.sendRejectionEmail(user);
			LOGGER.info("Rejection email sent successfully to user {}", user.getEmail());
		}
	}

	@Override
	public List<User> getApprovedUsers() {
		LOGGER.info("Fetching approved users...");

		return userDAO.getUsersByStatus(RegisteredStatus.APPROVED);
	}

	@Override
	public List<User> getPendingUsers() {
		LOGGER.info("Fetching pending users...");

		return userDAO.getUsersByStatus(RegisteredStatus.PENDING);
	}

	@Override
	public List<User> getRejectedUsers() {
		LOGGER.info("Fetching rejected users...");

		return userDAO.getUsersByStatus(RegisteredStatus.REJECTED);
	}

	@Override
	public String changeUserPhoneNumber(int id, long phoneNumber) {
		try {
			LOGGER.info("Updating user phone number !");
			userDAO.updateUserPhoneNumber(id, phoneNumber);

			return "Profile updated successfully!";
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to Update Phone number. Please try again later !");
		}
	}

	@Override
	public String updateUserPassword(int id, String password) {
		try {
			LOGGER.info("Updating user Password !");
			userDAO.updateUserPassword(id, passwordEncoder.encode(password));

			return "Password changed successfully!";
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to Update Phone number. Please try again later !");
		}
	}

	@Override
	public String generateResetToken(User user) {
		UUID uuid = UUID.randomUUID();
		if (Boolean.TRUE.equals(passwordTokenDAO.isPasswordTokenExists(user.getId()))) {
			LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(2);
			passwordTokenDAO.updatePasswordToken(user.getId(), uuid.toString(), expirationDate);
		} else {
			PasswordToken passwordToken = new PasswordToken();
			passwordToken.setToken(uuid.toString());
			passwordToken.setExpirationdate(LocalDateTime.now().plusMinutes(2));
			passwordToken.setUser(user);
			passwordTokenDAO.createPasswordToken(passwordToken);
		}

		return uuid.toString();
	}

	@Override
	public PasswordToken getPasswordTokenByToken(String token) {
		PasswordToken passwordToken = passwordTokenDAO.getPasswordTokenByToken(token);
		if (LocalDateTime.now().isAfter(passwordToken.getExpirationdate())) {
			throw new DataBaseAccessException("Token has expired. Please try again !");
		}

		return passwordToken;
	}

	@Override
	public void updateUserProject(UserModel userModel) {
		try {
			LOGGER.info("Updating project for the User: {}", userModel.getUserId());
			userDAO.updateUserProject(userModel.getUserId(), userModel.getProjectId());
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to update User's project !");
		}
	}

	@Override
	public Role getUserRole(int userId) {
		try {
			LOGGER.info("Getting Role of a User !");

			return roleDAO.getUsersRole(userId);
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to get User's role !");
		}
	}

	@Override
	public User getUserByBookingId(int bookingId) {
		try {
			LOGGER.info("Getting user of booking Id: {}", bookingId);

			return userDAO.getUserByBookingId(bookingId);
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to get User's !");
		}
	}
}