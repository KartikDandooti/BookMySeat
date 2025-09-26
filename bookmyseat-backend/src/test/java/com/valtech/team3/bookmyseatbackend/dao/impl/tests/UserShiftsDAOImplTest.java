package com.valtech.team3.bookmyseatbackend.dao.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import com.valtech.team3.bookmyseatbackend.dao.impl.UserShiftsDAOImpl;
import com.valtech.team3.bookmyseatbackend.dao.impl.UserShiftsDAOImpl.UserShiftsRowMapper;
import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;

@ExtendWith(MockitoExtension.class)
class UserShiftsDAOImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;
	@Mock
	private ResultSet resultSet;
	@InjectMocks
	private UserShiftsDAOImpl userShiftsDAO;

	@Test
	void testDoesUserShiftExist_Exists() {
		int userId = 1;
		int shiftId = 2;

		when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(userId), eq(shiftId))).thenReturn(1);

		boolean exists = userShiftsDAO.doesUserShiftExist(userId, shiftId);

		assertTrue(exists);
		verify(jdbcTemplate).queryForObject(eq("SELECT COUNT(*) FROM USER_SHIFTS WHERE USER_ID = ? AND SHIFT_ID = ?"), eq(Integer.class), eq(userId), eq(shiftId));
	}

	@Test
	void testDoesUserShiftExist_DoesNotExist() {
		int userId = -1;
		int shiftId = -2;

		when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(userId), eq(shiftId))).thenReturn(0);

		boolean exists = userShiftsDAO.doesUserShiftExist(userId, shiftId);

		assertFalse(exists);
		verify(jdbcTemplate).queryForObject(eq("SELECT COUNT(*) FROM USER_SHIFTS WHERE USER_ID = ? AND SHIFT_ID = ?"), eq(Integer.class), eq(userId), eq(shiftId));
	}

	@Test
	void testAddUserShift() {
		int userId = 1;
		int shiftId = 2;

		userShiftsDAO.addUserShift(userId, shiftId);

		verify(jdbcTemplate).update(eq("INSERT INTO USER_SHIFTS (USER_ID, SHIFT_ID, CREATED_DATE) VALUES (?, ?, NOW())"), eq(userId), eq(shiftId));
	}

	@Test
	void testDeleteUserShifts() {
		int userId = 1;

		userShiftsDAO.deleteUserShifts(userId);

		verify(jdbcTemplate).update(eq("DELETE FROM USER_SHIFTS WHERE USER_ID = ?"), eq(userId));
	}

	@Test
	void testGetAllShiftDetailsByUserId() {
		int userId = 1;

		List<ShiftDetails> expectedShifts = Arrays.asList(new ShiftDetails(1, "Morning Shift", LocalTime.of(8, 0), LocalTime.of(12, 0)),
		      new ShiftDetails(2, "Evening Shift", LocalTime.of(16, 0), LocalTime.of(20, 0)));

		when(jdbcTemplate.query(anyString(), any(UserShiftsRowMapper.class), eq(userId))).thenReturn(expectedShifts);

		List<ShiftDetails> actualShifts = userShiftsDAO.getAllShiftDetailsByUserId(userId);

		assertEquals(expectedShifts, actualShifts);
		verify(jdbcTemplate).query(eq("SELECT SD.ID AS SHIFT_ID, SD.SHIFT_NAME, SD.START_TIME, SD.END_TIME FROM USER_SHIFTS US JOIN SHIFT_DETAILS SD ON US.SHIFT_ID = SD.ID WHERE US.USER_ID = ?"),
		      any(UserShiftsRowMapper.class), eq(userId));
	}

	@Test
	void testMapRowCompleteShiftDetails() throws SQLException {
		when(resultSet.getInt("SHIFT_ID")).thenReturn(1);
		when(resultSet.getString("SHIFT_NAME")).thenReturn("Morning Shift");
		when(resultSet.getTime("START_TIME")).thenReturn(Time.valueOf("08:00:00"));
		when(resultSet.getTime("END_TIME")).thenReturn(Time.valueOf("12:00:00"));

		UserShiftsRowMapper rowMapper = userShiftsDAO.new UserShiftsRowMapper();
		ShiftDetails shiftDetails = rowMapper.mapRow(resultSet, 0);

		assertEquals(1, shiftDetails.getId());
		assertEquals("Morning Shift", shiftDetails.getShiftName());
		assertEquals(LocalTime.of(8, 0), shiftDetails.getStartTime());
		assertEquals(LocalTime.of(12, 0), shiftDetails.getEndTime());
	}
}