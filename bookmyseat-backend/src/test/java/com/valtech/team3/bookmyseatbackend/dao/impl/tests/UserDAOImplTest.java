package com.valtech.team3.bookmyseatbackend.dao.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.valtech.team3.bookmyseatbackend.dao.impl.ProjectDAOImpl;
import com.valtech.team3.bookmyseatbackend.dao.impl.RoleDAOImpl;
import com.valtech.team3.bookmyseatbackend.dao.impl.UserDAOImpl;
import com.valtech.team3.bookmyseatbackend.dao.impl.UserDAOImpl.UserRowMappper;
import com.valtech.team3.bookmyseatbackend.entities.Project;
import com.valtech.team3.bookmyseatbackend.entities.RegisteredStatus;
import com.valtech.team3.bookmyseatbackend.entities.Role;
import com.valtech.team3.bookmyseatbackend.entities.User;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class UserDAOImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;
	@Mock
	private RoleDAOImpl roleDAO;
	@Mock
	private ProjectDAOImpl projectDAO;
	@Mock
	private ResultSet resultSet;
	@InjectMocks
	private UserDAOImpl userDAO;

	@Test
	void testGetUserById() {
		User expectedUser = new User(7, 5701, "Aastha", "Singh", "aastha.singh@valtech.com", 6000263732L, "Aastha@123", RegisteredStatus.PENDING, LocalDateTime.now(), projectDAO.getProjectById(1),
		      roleDAO.getRoleById(1));

		when(jdbcTemplate.queryForObject(any(String.class), ArgumentMatchers.<BeanPropertyRowMapper<User>>any(), any(Integer.class))).thenReturn(expectedUser);

		User actualUser = userDAO.getUserById(7);

		assertEquals(expectedUser, actualUser);
	}

	@Test
	void testGetUserByEmail() {
		User expectedUser = new User(7, 5701, "Aastha", "Singh", "aastha.singh@valtech.com", 6000263732L, "Aastha@123", RegisteredStatus.PENDING, LocalDateTime.now(), projectDAO.getProjectById(1),
		      roleDAO.getRoleById(1));

		when(jdbcTemplate.queryForObject(any(String.class), ArgumentMatchers.<BeanPropertyRowMapper<User>>any(), any(String.class))).thenReturn(expectedUser);

		User actualUser = userDAO.getUserByEmail("aastha.singh@valtech.com");

		assertEquals(expectedUser, actualUser);
	}

	@Test
	void testGetUserByEmployeeId() {
		User expectedUser = new User(7, 5701, "Aastha", "Singh", "aastha.singh@valtech.com", 6000263732L, "Aastha@123", RegisteredStatus.PENDING, LocalDateTime.now(), projectDAO.getProjectById(1),
		      roleDAO.getRoleById(1));

		when(jdbcTemplate.queryForObject(any(String.class), ArgumentMatchers.<BeanPropertyRowMapper<User>>any(), any(Integer.class))).thenReturn(expectedUser);

		User actualUser = userDAO.getUserByEmployeeId(5701);

		assertEquals(expectedUser, actualUser);
	}

	@Test
	void testIsEmailExists_EmailExists() {
		String email = "kruthik.b@valtech.com";
		int count = 1;

		when(jdbcTemplate.queryForObject(any(String.class), eq(Integer.class), eq(email))).thenReturn(count);
		boolean exists = userDAO.isEmailExists(email);

		assertTrue(exists);
	}

	@Test
	void testIsEmailExists_EmailDoesNotExist() {
		String email = "kruthik.bhupal@valtech.com";
		int count = 0;

		when(jdbcTemplate.queryForObject(any(String.class), eq(Integer.class), eq(email))).thenReturn(count);
		boolean exists = userDAO.isEmailExists(email);

		assertFalse(exists);
	}

	@Test
	void testIsEmployeeIdExists_EmployeeIdExists() {
		int employeeId = 5318;
		int count = 1;
		when(jdbcTemplate.queryForObject(any(String.class), eq(Integer.class), eq(employeeId))).thenReturn(count);
		boolean exists = userDAO.isEmployeeIdExists(employeeId);

		assertTrue(exists);
	}

	@Test
	void testIsEmployeeIdExists_EmployeeIdDoesNotExist() {
		int employeeId = 53180;
		int count = 0;

		when(jdbcTemplate.queryForObject(any(String.class), eq(Integer.class), eq(employeeId))).thenReturn(count);
		boolean exists = userDAO.isEmployeeIdExists(employeeId);

		assertFalse(exists);
	}

	@Test
	void testGetAdminUsers() {
		List<User> expectedUsers = new ArrayList<>();
		List<User> actualUsers = userDAO.getAdminUsers();

		assertEquals(expectedUsers, actualUsers);
	}

	@Test
	void testUpdateUserRegistrationStatus() {
		int userId = 1;
		RegisteredStatus registrationStatus = RegisteredStatus.PENDING;

		userDAO.updateUserRegistrationStatus(userId, registrationStatus);

		verify(jdbcTemplate).update("UPDATE USER SET REGISTERED_STATUS = ? WHERE ID = ?", registrationStatus.toString(), userId);
	}

	@Test
	void testUpdateUserPassword() {
		int userId = 1;
		String newPassword = "newPassword";

		userDAO.updateUserPassword(userId, newPassword);

		verify(jdbcTemplate).update("UPDATE USER SET PASSWORD=?, MODIFIED_DATE=NOW() WHERE ID=?", newPassword, userId);
	}

	@Test
	void testUpdateUserPhoneNumber() {
		int id = 1;
		long phoneNumber = 1234567890L;

		userDAO.updateUserPhoneNumber(id, phoneNumber);

		verify(jdbcTemplate).update("UPDATE USER SET PHONE_NUMBER=?, MODIFIED_DATE=NOW() WHERE id=?", phoneNumber, id);
	}

	@Test
	void testUpdateUserProject() {
		int userId = 1;
		int projectId = 2;

		userDAO.updateUserProject(userId, projectId);

		verify(jdbcTemplate).update(eq("UPDATE USER SET PROJECT_ID = ? WHERE ID = ?"), eq(projectId), eq(userId));
	}

	@Test
	void testGetAllUsers() {
		List<User> expectedUsers = Arrays.asList(
				new User(1, 123456, "John", "Doe", "john@example.com", 1234567890L, "password", null),
				new User(2, 789012, "Jane", "Doe", "jane@example.com", 9876543210L, "password", null));

		when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedUsers);

		List<User> actualUsers = userDAO.getAllUsers();

		assertEquals(expectedUsers.size(), actualUsers.size());
		assertTrue(actualUsers.containsAll(expectedUsers));
	}

	@Test
	void testGetUsersByStatus() {
		RegisteredStatus status = RegisteredStatus.APPROVED;
		List<User> expectedUsers = Arrays.asList(
				new User(1, 123456, "John", "Doe", "john@example.com", 1234567890L, "password", null),
				new User(2, 789012, "Jane", "Doe", "jane@example.com", 9876543210L, "password", null));

		when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(status.name()))).thenReturn(expectedUsers);

		List<User> actualUsers = userDAO.getUsersByStatus(status);

		assertEquals(expectedUsers.size(), actualUsers.size());
		assertTrue(actualUsers.containsAll(expectedUsers));
	}

	@Test
	void testGetUserByBookingId() {
		int bookingId = 123;
		User expectedUser = new User(1, 123456, "John", "Doe", "john@example.com", 1234567890L, "password", null);

		when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(bookingId))).thenReturn(expectedUser);

		User actualUser = userDAO.getUserByBookingId(bookingId);

		assertEquals(expectedUser, actualUser);
	}

	@Test
	void testCreateUser() {
		LocalDateTime createdDateTime = LocalDateTime.now();
		User user = new User();
		user.setEmployeeId(12345);
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setEmail("johndoe@example.com");
		user.setPhoneNumber(1234567890L);
		user.setPassword("password");
		user.setCreatedDate(createdDateTime);
		user.setProject(new Project(10, "Project X", null));
		user.setRole(new Role(2, "Developer"));
		user.setRegisteredStatus(RegisteredStatus.APPROVED);

		ArgumentCaptor<Object[]> argsCaptor = ArgumentCaptor.forClass(Object[].class);

		doAnswer(invocation -> null).when(jdbcTemplate).update(anyString(), argsCaptor.capture());

		userDAO.createUser(user);

		assertEquals(1, argsCaptor.getAllValues().size());
		Object[] actualParams = argsCaptor.getValue();
		assertEquals(user.getEmployeeId(), actualParams[0]);
		assertEquals(user.getFirstName(), actualParams[1]);
		assertEquals(user.getLastName(), actualParams[2]);
		assertEquals(user.getEmail(), actualParams[3]);
		assertEquals(user.getPhoneNumber(), actualParams[4]);
		assertEquals(user.getPassword(), actualParams[5]);
		assertEquals(createdDateTime, actualParams[6]);
		assertEquals(user.getProject().getId(), actualParams[7]);
		assertEquals(user.getRole().getId(), actualParams[8]);
		assertEquals(user.getRegisteredStatus().toString(), actualParams[9]);
	}

	@Test
	void testMapRowCompleteUserData() throws SQLException {

		LocalDateTime dateTime = LocalDateTime.now();
		Mockito.when(resultSet.getInt("id")).thenReturn(1);
		Mockito.when(resultSet.getInt("employee_id")).thenReturn(12345);
		Mockito.when(resultSet.getString("email")).thenReturn("john.doe@example.com");
		Mockito.when(resultSet.getString("first_name")).thenReturn("John");
		Mockito.when(resultSet.getString("last_name")).thenReturn("Doe");
		Mockito.when(resultSet.getString("password")).thenReturn("password");
		Mockito.when(resultSet.getLong("phone_number")).thenReturn(1234567890L);
		Mockito.when(resultSet.getTimestamp("created_date")).thenReturn(Timestamp.valueOf(dateTime));
		Mockito.when(resultSet.getTimestamp("modified_date")).thenReturn(null);
		Mockito.when(resultSet.getString("registered_status")).thenReturn("APPROVED");
		Mockito.when(resultSet.getInt("role_id")).thenReturn(2);
		Mockito.when(resultSet.getInt("project_id")).thenReturn(10);

		Role mockRole = Mockito.mock(Role.class);
		Mockito.when(roleDAO.getRoleById(2)).thenReturn(mockRole);
		Project mockProject = Mockito.mock(Project.class);
		Mockito.when(projectDAO.getProjectById(10)).thenReturn(mockProject);

		UserRowMappper rowMapper = userDAO.new UserRowMappper();
		User user = rowMapper.mapRow(resultSet, 0);

		assertEquals(1, user.getId());
		assertEquals(12345, user.getEmployeeId());
		assertEquals("john.doe@example.com", user.getEmail());
		assertEquals("John", user.getFirstName());
		assertEquals("Doe", user.getLastName());
		assertEquals("password", user.getPassword());
		assertEquals(1234567890L, user.getPhoneNumber());
		assertEquals(dateTime, user.getCreatedDate());
		assertNull(user.getModifiedDate());
		assertEquals(RegisteredStatus.APPROVED, user.getRegisteredStatus());
		assertEquals(mockRole, user.getRole());
		assertEquals(mockProject, user.getProject());
	}
}