package com.valtech.team3.bookmyseatbackend.dao.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.valtech.team3.bookmyseatbackend.dao.UserDAO;
import com.valtech.team3.bookmyseatbackend.dao.impl.PasswordTokenDAOImpl;
import com.valtech.team3.bookmyseatbackend.dao.impl.PasswordTokenDAOImpl.PasswordTokenRowMapper;
import com.valtech.team3.bookmyseatbackend.entities.PasswordToken;
import com.valtech.team3.bookmyseatbackend.entities.User;

@ExtendWith(MockitoExtension.class)
class PasswordTokenDAOImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;
	@Mock
	private UserDAO userDAO;
	@Mock
	private ResultSet resultSet;
	@InjectMocks
	private PasswordTokenDAOImpl passwordTokenDAO;

	@Test
	void testCreatePasswordToken() {
		User user = new User();
		user.setId(1);

		PasswordToken passwordToken = new PasswordToken();
		passwordToken.setToken("testToken");
		passwordToken.setExpirationdate(LocalDateTime.now());
		passwordToken.setUser(user);

		when(jdbcTemplate.update(any(String.class), any(String.class), any(LocalDateTime.class), anyInt())).thenReturn(1);

		passwordTokenDAO.createPasswordToken(passwordToken);

		verify(jdbcTemplate).update(any(String.class), eq("testToken"), any(LocalDateTime.class), anyInt());
	}

	@SuppressWarnings("unchecked")
	@Test
	void testGetPasswordTokenByToken() {
		when(jdbcTemplate.queryForObject(any(String.class), any(RowMapper.class), any(String.class)))
				.thenReturn(new PasswordToken());

		PasswordToken result = passwordTokenDAO.getPasswordTokenByToken("testToken");

		assertNotNull(result);
	}

	@SuppressWarnings("unchecked")
	@Test
	void testIsPasswordTokenExists() {
		when(jdbcTemplate.queryForObject(any(String.class), any(Class.class), anyInt())).thenReturn(1);

		boolean result = passwordTokenDAO.isPasswordTokenExists(1);

		assertTrue(result);
	}

	@Test
	void testIsPasswordTokenExists_NullCheck() {
		Mockito.when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(1))).thenReturn(null); // Simulate
		// null
		// count

		assertThrows(NullPointerException.class, () -> passwordTokenDAO.isPasswordTokenExists(1));
	}

	@Test
	void testUpdatePasswordToken() {
		passwordTokenDAO.updatePasswordToken(1, "newToken", LocalDateTime.now());

		verify(jdbcTemplate).update(any(String.class), eq("newToken"), any(LocalDateTime.class), anyInt());
	}

	@Test
	void testMapRow() throws SQLException {
		int id = 1;
		String token = "some_token";
		LocalDateTime expirationDate = LocalDateTime.now();
		int userId = 2;

		when(resultSet.getInt("id")).thenReturn(id);
		when(resultSet.getString("token")).thenReturn(token);
		when(resultSet.getTimestamp("expiration_date")).thenReturn(Timestamp.valueOf(expirationDate));
		when(resultSet.getInt("user_id")).thenReturn(userId);

		User mockUser = new User();
		mockUser.setId(userId);
		when(userDAO.getUserById(userId)).thenReturn(mockUser);

		PasswordTokenRowMapper mapper = passwordTokenDAO.new PasswordTokenRowMapper();
		PasswordToken passwordToken = mapper.mapRow(resultSet, 1);

		assertEquals(id, passwordToken.getId());
		assertEquals(token, passwordToken.getToken());
		assertEquals(expirationDate, passwordToken.getExpirationdate());
		assertEquals(mockUser, passwordToken.getUser());
	}
}