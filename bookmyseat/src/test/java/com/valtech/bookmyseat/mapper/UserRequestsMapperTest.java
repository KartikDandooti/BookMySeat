package com.valtech.bookmyseat.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.valtech.bookmyseat.model.UserRequestsModel;

@ExtendWith(MockitoExtension.class)
class UserRequestsMapperTest {

	@Mock
	private ResultSet resultSet;

	@InjectMocks
	private UserRequestsMapper userRequestsMapper;

	@Test
	void testMapRow() throws SQLException {
		when(resultSet.getString("name")).thenReturn("John Doe");
		when(resultSet.getString("email_id")).thenReturn("john@example.com");
		when(resultSet.getInt("user_id")).thenReturn(123);
		when(resultSet.getDate("registered_date")).thenReturn(Date.valueOf(LocalDate.now()));
		when(resultSet.getString("approval_status")).thenReturn("Pending");
		UserRequestsModel result = userRequestsMapper.mapRow(resultSet, 1);
		assertEquals("John Doe", result.getName());
		assertEquals("john@example.com", result.getEmailID());
		assertEquals(123, result.getUserID());
		assertEquals(LocalDate.now(), result.getRegsiterdDate());
		assertEquals("Pending", result.getApprovalStatus());
		verify(resultSet).getString("name");
		verify(resultSet).getString("email_id");
		verify(resultSet).getInt("user_id");
		verify(resultSet).getDate("registered_date");
		verify(resultSet).getString("approval_status");
		verifyNoMoreInteractions(resultSet);
	}
}
