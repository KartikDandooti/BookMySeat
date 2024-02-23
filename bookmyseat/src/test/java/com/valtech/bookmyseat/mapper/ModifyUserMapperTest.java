package com.valtech.bookmyseat.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.valtech.bookmyseat.entity.User;

@ExtendWith(MockitoExtension.class)
class ModifyUserMapperTest {

	@Mock
	private ResultSet resultSet;

	@InjectMocks
	private ModifyUserMapper modifyUserMapper;

	@Test
	void testMapRow() throws SQLException {
		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.getInt("user_id")).thenReturn(123);
		when(resultSet.getString("email_id")).thenReturn("test@example.com");
		when(resultSet.getString("first_name")).thenReturn("John");
		when(resultSet.getString("last_name")).thenReturn("Doe");
		when(resultSet.getLong("phone_number")).thenReturn(1234567890L);
		User user = modifyUserMapper.mapRow(resultSet, 1);
		assertEquals(123, user.getUserId());
		assertEquals("test@example.com", user.getEmailId());
		assertEquals("John", user.getFirstName());
		assertEquals("Doe", user.getLastName());
		assertEquals(1234567890L, user.getPhoneNumber());
		verify(resultSet).getInt("user_id");
		verify(resultSet).getString("email_id");
		verify(resultSet).getString("first_name");
		verify(resultSet).getString("last_name");
		verify(resultSet).getLong("phone_number");
	}
}