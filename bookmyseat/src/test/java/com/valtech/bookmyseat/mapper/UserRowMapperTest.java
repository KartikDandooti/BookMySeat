package com.valtech.bookmyseat.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.valtech.bookmyseat.entity.ApprovalStatus;
import com.valtech.bookmyseat.entity.Role;
import com.valtech.bookmyseat.entity.User;

@ExtendWith(MockitoExtension.class)
class UserRowMapperTest {

	@Test
	void testMapRow() throws SQLException {
		UserRowMapper rowMapper = new UserRowMapper();
		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.getInt("user_id")).thenReturn(1);
		when(resultSet.getString("email_id")).thenReturn("test@example.com");
		when(resultSet.getString("first_name")).thenReturn("John");
		when(resultSet.getString("last_name")).thenReturn("Doe");
		when(resultSet.getString("password")).thenReturn("password");
		when(resultSet.getString("approval_status")).thenReturn(ApprovalStatus.APPROVED.name());
		when(resultSet.getLong("phone_number")).thenReturn(1234567890L);
		when(resultSet.getInt("role_id")).thenReturn(2);
		when(resultSet.getString("role_name")).thenReturn("USER");
		when(resultSet.getDate("registered_date")).thenReturn(java.sql.Date.valueOf(LocalDate.now()));
		when(resultSet.getDate("modified_date")).thenReturn(java.sql.Date.valueOf(LocalDate.now()));
		User user = rowMapper.mapRow(resultSet, 1);
		assertEquals(1, user.getUserId());
		assertEquals("test@example.com", user.getEmailId());
		assertEquals("John", user.getFirstName());
		assertEquals("Doe", user.getLastName());
		assertEquals("password", user.getPassword());
		assertEquals(ApprovalStatus.APPROVED, user.getApprovalStatus());
		assertEquals(1234567890L, user.getPhoneNumber());
		assertEquals(LocalDate.now(), user.getRegisteredDate());
		assertEquals(LocalDate.now(), user.getModifiedDate());
		Role role = user.getRole();
		assertEquals(2, role.getRoleId());
		assertEquals("USER", role.getRoleName());
	}
}
