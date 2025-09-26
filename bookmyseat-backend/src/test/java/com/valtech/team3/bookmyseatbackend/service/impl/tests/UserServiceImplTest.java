package com.valtech.team3.bookmyseatbackend.service.impl.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
import com.valtech.team3.bookmyseatbackend.service.impl.UserServiceImpl;

import jakarta.mail.MessagingException;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@Mock
	private UserDAO userDAO;
	@Mock
	private RoleDAO roleDAO;
	@Mock
	private UserShiftsDAO userShiftsDAO;
	@Mock
	private ProjectDAO projectDAO;
	@Mock
	private PasswordTokenDAO passwordTokenDAO;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private EmailService emailService;
	@InjectMocks
	private UserServiceImpl userService;
	
	@Test
	void testSaveRegisteredUser_DuplicateEmployeeException() {
		UserModel userModel = new UserModel();
		userModel.setEmail("kruthik.b@valtech.com");
		userModel.setEmployeeId(5718);
		userModel.setPassword("password");
		userModel.setProjectName("Project A");

		when(userDAO.isEmailExists(userModel.getEmail())).thenReturn(true);
		when(userDAO.isEmployeeIdExists(userModel.getEmployeeId())).thenReturn(true);

		assertThrows(DuplicateEmployeeException.class, () -> userService.saveRegisteredUser(userModel));
	}

	@Test
	void testSaveRegisteredUserDuplicateEmail() {
		UserModel user = new UserModel();
		user.setEmail("kruthik.b@valtech.com");
		user.setEmployeeId(5718);

		when(userDAO.isEmailExists(user.getEmail())).thenReturn(true);

		assertThrows(DuplicateEmployeeException.class, () -> userService.saveRegisteredUser(user));
	}

	@Test
	void sendEmailToAdminAfterRegistration_Success() throws MessagingException {
		User user = new User();
		user.setEmail("kruthik.b@valtech.com");
		user.setEmployeeId(5718);
		user.setPassword("password");
		user.setProject(new Project(1, "LV", LocalDate.now()));
		userService.sendEmailToAdminAfterRegistration(user);

		verify(emailService, times(1)).sendEmailToAdmins(user);
	}

	@Test
	void sendEmailToAdminAfterRegistration_Failure() throws MessagingException {
		User user = new User();
		user.setEmail("kruthik.b@valtech.com");
		user.setEmployeeId(5718);
		user.setPassword("password");
		user.setProject(new Project(1, "LV", LocalDate.now()));
		doThrow(new MessagingException("Failed to send email")).when(emailService).sendEmailToAdmins(user);

		assertThrows(EmailSendingException.class, () -> userService.sendEmailToAdminAfterRegistration(user));
	}

	@Test
	void testSaveRegisteredUser_DuplicateEmployeeId() {
		UserModel user = new UserModel();
		user.setEmail("kruthik.b@valtech.com");
		user.setEmployeeId(5718);

		when(userDAO.isEmployeeIdExists(user.getEmployeeId())).thenReturn(true);

		assertThrows(DuplicateEmployeeException.class, () -> userService.saveRegisteredUser(user));
	}

	@Test
	void testGetUserByEmail() {
		String email = "murali.kr@valtech.com";
		User expectedUser = new User();
		expectedUser.setEmail("murali.kr@valtech.com");
		expectedUser.setEmployeeId(5724);

		when(userDAO.getUserByEmail((email))).thenReturn(expectedUser);

		User actualUser = userService.getUserByEmail(email);

		assertEquals(expectedUser, actualUser);
	}

	@Test
	void testFindUserByEmailId() {
		String email = "murali.kr@valtech.com";
		User expectedUser = new User();
		expectedUser.setEmail("murali.kr@valtech.com");
		expectedUser.setEmployeeId(5724);

		when(userDAO.getUserByEmail(email)).thenReturn(expectedUser);

		User result = userService.getUserByEmail(email);

		assertEquals(expectedUser, result);
		verify(userDAO).getUserByEmail(email);
	}

	@Test
	void testChangeUserPassword_Successful() {
		User user = new User(1, 5719, "Laxman", "Kuddemmi", "laxman.kuddemmi@valtech.com", 9945711296L, "Laxman@123",
				RegisteredStatus.PENDING, LocalDateTime.now(), new Project(3, "RMG", null, LocalDateTime.now()),
				new Role(1, "ASD"));

		int id = user.getId();
		String currentPassword = user.getPassword();
		String newPassword = "Lax@123";

		when(userDAO.getUserById(id)).thenReturn(user);
		when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);

		boolean result = userService.changeUserPassword(id, currentPassword, newPassword);

		assertTrue(result);
		verify(userDAO).getUserById(id);
		verify(passwordEncoder).matches(currentPassword, user.getPassword());
		verify(userDAO).updateUserPassword(id, passwordEncoder.encode(newPassword));
	}

	@Test
	void testChangeUserPassword_Unsuccessful() {
		User user = new User(1, 5719, "Laxman", "Kuddemmi", "laxman.kuddemmi@valtech.com", 9945711296L, "Laxman@123",
				RegisteredStatus.PENDING, LocalDateTime.now(), new Project(3, "RMG", null, LocalDateTime.now()),
				new Role(1, "ASD"));

		int id = user.getId();
		String currentPassword = "WrongPassword";
		String newPassword = "Lax@123";

		when(userDAO.getUserById(id)).thenReturn(user);
		when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(false);

		boolean result = userService.changeUserPassword(id, currentPassword, newPassword);

		assertFalse(result);
		verify(userDAO).getUserById(id);
		verify(passwordEncoder).matches(currentPassword, user.getPassword());
		verify(userDAO, never()).updateUserPassword(Mockito.anyInt(), Mockito.anyString());
	}

	@Test
	void testChangeUserPassword_UserNotFound() {
		User user = new User(1, 5719, "Laxman", "Kuddemmi", "laxman.kuddemmi@valtech.com", 9945711296L, "Laxman@123",
				RegisteredStatus.PENDING, LocalDateTime.now(), new Project(3, "RMG", null, LocalDateTime.now()),
				new Role(1, "ASD"));
		int id = user.getId();
		String currentPassword = user.getPassword();
		String newPassword = "NewPassword";

		when(userDAO.getUserById(id)).thenReturn(null);

		assertThrowsExactly(UserNotFoundException.class,
				() -> userService.changeUserPassword(id, currentPassword, newPassword));
	}

	@Test
	void testUpdatePhoneNumber() {
		int id = 1;
		long phoneNumber = 1234567890;

		String result = userService.changeUserPhoneNumber(id, phoneNumber);

		verify(userDAO).updateUserPhoneNumber(id, phoneNumber);
		assertEquals("Profile updated successfully!", result);
	}

	@Test
	void testUpdatePhoneNumberReturnsSuccessMessage() {
		int id = 1;
		long phoneNumber = 1234567890;

		String result = userService.changeUserPhoneNumber(id, phoneNumber);

		assertEquals("Profile updated successfully!", result);
	}

	@Test
	void approveUserRegistration_Success() throws MessagingException {
		User user = new User();
		user.setId(1);
		doNothing().when(userDAO).updateUserRegistrationStatus(eq(1), eq(RegisteredStatus.APPROVED));
		userService.approveUserRegistration(user);
		assertEquals(RegisteredStatus.APPROVED, user.getRegisteredStatus());
		verify(userDAO, times(1)).updateUserRegistrationStatus(eq(1), eq(RegisteredStatus.APPROVED));
	}

	@Test
	void approveUserRegistration_EmailSendingFailure() throws MessagingException {
		User user = new User();
		user.setId(1);
		doThrow(new MessagingException("Failed to send email")).when(emailService).sendApprovalEmail(user);
		EmailSendingException exception = assertThrows(EmailSendingException.class,
				() -> userService.approveUserRegistration(user));
		assertEquals("Error sending email. Please try again later !", exception.getMessage());
	}

	@Test
	void rejectUserRegistration_Success() throws MessagingException {
		User user = new User();
		user.setId(1);
		doNothing().when(userDAO).updateUserRegistrationStatus(eq(1), eq(RegisteredStatus.REJECTED));

		userService.rejectUserRegistration(user);

		assertEquals(RegisteredStatus.REJECTED, user.getRegisteredStatus());
		verify(userDAO, times(1)).updateUserRegistrationStatus(eq(1), eq(RegisteredStatus.REJECTED));
	}

	@Test
	void rejectUserRegistration_EmailSendingFailure() throws MessagingException {
		User user = new User();
		user.setId(1);
		doThrow(new MessagingException("Failed to send email")).when(emailService).sendRejectionEmail(user);

		EmailSendingException exception = assertThrows(EmailSendingException.class,
				() -> userService.rejectUserRegistration(user));
		assertEquals("Error sending email. Please try again later !", exception.getMessage());
	}

	@Test
	void testProcessStatusAndSendEmail_Approved() throws MessagingException {
		User user = new User();
		user.setRegisteredStatus(RegisteredStatus.APPROVED);
		userService.processStatusAndSendEmail(user);
		verify(emailService, times(1)).sendApprovalEmail(user);
	}

	@Test
	void testProcessStatusAndSendEmail_Rejected() throws MessagingException {
		User user = new User();
		user.setRegisteredStatus(RegisteredStatus.REJECTED);
		userService.processStatusAndSendEmail(user);
		verify(emailService, times(1)).sendRejectionEmail(user);
	}

	@Test
	void testUpdateUserPasswordSuccess() {
		int id = 1;
		String password = "newPassword";

		String result = userService.updateUserPassword(id, password);

		assertEquals("Password changed successfully!", result);
		verify(userDAO).updateUserPassword(id, passwordEncoder.encode(password));
	}

	@Test
	void test_catch_data_access_exception_and_throw_database_access_exception() {
		int id = 0;
		String password = "";

		doThrow(RuntimeException.class).when(userDAO).updateUserPassword(eq(id), anyString());

		assertThrows(DataBaseAccessException.class, () -> userService.updateUserPassword(id, password));
	}

	@Test
	void test_get_password_token_by_token_expired_token() {

		String token = "expiredToken";
		PasswordToken passwordToken = new PasswordToken();
		passwordToken.setExpirationdate(LocalDateTime.now().minusMinutes(2));

		Mockito.when(passwordTokenDAO.getPasswordTokenByToken(token)).thenReturn(passwordToken);

		assertThrows(DataBaseAccessException.class, () -> userService.getPasswordTokenByToken(token));
	}

	@Test
	void test_check_password_token_exists() {
		User user = new User();
		user.setId(1);

		when(passwordTokenDAO.isPasswordTokenExists(user.getId())).thenReturn(true);
		String result = userService.generateResetToken(user);

		assertNotNull(result);
	}

	@Test
	void test_get_password_token_by_token_expired() {
		String token = "token";
		PasswordToken passwordToken = new PasswordToken();
		passwordToken.setExpirationdate(LocalDateTime.now().minusMinutes(2));

		when(passwordTokenDAO.getPasswordTokenByToken(token)).thenReturn(passwordToken);

		assertThrows(DataBaseAccessException.class, () -> userService.getPasswordTokenByToken(token));
	}

	@Test
	void test_get_password_token_by_token_success() {
		String token = "token123";
		PasswordToken passwordToken = new PasswordToken();
		passwordToken.setExpirationdate(LocalDateTime.now().plusMinutes(2));

		when(passwordTokenDAO.getPasswordTokenByToken(token)).thenReturn(passwordToken);

		PasswordToken result = userService.getPasswordTokenByToken(token);

		assertNotNull(result);
	}

	@Test
	void test_changeUserPhoneNumber_throw_exception() {
		int id = 0;
		long phoneNumber = 1234567890L;

		doThrow(new RuntimeException()).when(userDAO).updateUserPhoneNumber(id, phoneNumber);

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class,
				() -> userService.changeUserPhoneNumber(id, phoneNumber));

		assertEquals("Failed to Update Phone number. Please try again later !", exception.getMessage());
	}

	@Test
	void test_generateResetToken_token_exists() {
		User user = new User();
		user.setId(1);

		when(passwordTokenDAO.isPasswordTokenExists(user.getId())).thenReturn(true);

		String result = userService.generateResetToken(user);

		assertNotNull(result);
		verify(passwordTokenDAO).updatePasswordToken(eq(user.getId()), anyString(), any(LocalDateTime.class));
	}

	@Test
	void test_generateResetToken_token_does_not_exist() {
		User user = new User();
		user.setId(1);
		when(passwordTokenDAO.isPasswordTokenExists(user.getId())).thenReturn(false);

		String result = userService.generateResetToken(user);

		assertNotNull(result);
		verify(passwordTokenDAO).createPasswordToken(any(PasswordToken.class));
	}

	@Test
	void test_getUserByEmail_exception() {
		String email = "";

		when(userDAO.getUserByEmail(email)).thenThrow(new RuntimeException("Simulated exception"));

		assertThrows(DataBaseAccessException.class, () -> userService.getUserByEmail(email));
	}

	@Test
	void testGetApprovedUsers() {
		List<User> approvedUsers = new ArrayList<>();

		when(userDAO.getUsersByStatus(RegisteredStatus.APPROVED)).thenReturn(approvedUsers);

		List<User> actualApprovedUsers = userService.getApprovedUsers();

		assertEquals(approvedUsers, actualApprovedUsers);
	}

	@Test
	void testGetPendingUsers() {
		List<User> pendingUsers = new ArrayList<>();

		when(userDAO.getUsersByStatus(RegisteredStatus.PENDING)).thenReturn(pendingUsers);

		List<User> actualPendingUsers = userService.getPendingUsers();

		assertEquals(pendingUsers, actualPendingUsers);
	}

	@Test
	void testGetRejectedUsers() {
		List<User> rejectedUsers = new ArrayList<>();

		when(userDAO.getUsersByStatus(RegisteredStatus.REJECTED)).thenReturn(rejectedUsers);

		List<User> actualRejectedUsers = userService.getRejectedUsers();

		assertEquals(rejectedUsers, actualRejectedUsers);
	}

	@Test
	void testGetUserByBookingId() {
		int bookingId = 123;

		User expectedUser = new User();

		when(userDAO.getUserByBookingId(bookingId)).thenReturn(expectedUser);

		User actualUser = userService.getUserByBookingId(bookingId);

		assertEquals(expectedUser, actualUser);
	}

	@Test
	void testUpdateUserProject_Success() {
		UserModel userModel = new UserModel();
		userModel.setUserId(1);
		userModel.setProjectId(100);

		assertDoesNotThrow(() -> userService.updateUserProject(userModel));

		verify(userDAO).updateUserProject(userModel.getUserId(), userModel.getProjectId());
	}

	@Test
	void testUpdateUserProject_Exception() {
		UserModel userModel = new UserModel();
		userModel.setUserId(1);
		userModel.setProjectId(100);

		doThrow(new RuntimeException("Database error")).when(userDAO).updateUserProject(anyInt(), anyInt());

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class,
				() -> userService.updateUserProject(userModel));

		assertEquals("Failed to update User's project !", exception.getMessage());
	}

	@Test
	void testGetUserRole_Success() {
		int userId = 1;
		Role role = new Role();
		role.setId(1);
		role.setRoleName("Admin");

		when(roleDAO.getUsersRole(userId)).thenReturn(role);

		Role result = userService.getUserRole(userId);

		assertEquals(role, result);
	}

	@Test
	void testGetUserRole_Exception() {
		int userId = 1;

		when(roleDAO.getUsersRole(anyInt())).thenThrow(new RuntimeException("Database error"));

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class,
				() -> userService.getUserRole(userId));

		assertEquals("Failed to get User's role !", exception.getMessage());
	}

	@Test
	void testGetUserByBookingId_Success() {
		int bookingId = 1;
		User user = new User();
		user.setId(1);
		user.setFirstName("Kruthik");
		user.setLastName("Bhupal");

		when(userDAO.getUserByBookingId(bookingId)).thenReturn(user);

		User result = userService.getUserByBookingId(bookingId);

		assertEquals(user, result);
	}

	@Test
	void testGetUserByBookingId_Exception() {
		int bookingId = 1;

		when(userDAO.getUserByBookingId(anyInt())).thenThrow(new RuntimeException("Database error"));

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class,
				() -> userService.getUserByBookingId(bookingId));

		assertEquals("Failed to get User's !", exception.getMessage());
	}

	@Test
	void testGetAllUsers_Success() {
		List<User> users = new ArrayList<>();
		User user1 = new User();
		user1.setId(1);
		user1.setFirstName("Kruthik");
		user1.setLastName("Bhupal");
		users.add(user1);

		when(userDAO.getAllUsers()).thenReturn(users);

		List<User> result = userService.getAllUsers();

		assertEquals(users, result);
	}

	@Test
	void testGetAllUsers_Exception() {
		when(userDAO.getAllUsers()).thenThrow(new RuntimeException("Database error"));

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class,
				() -> userService.getAllUsers());

		assertEquals("Error fetching Users from the Database !", exception.getMessage());
	}
}