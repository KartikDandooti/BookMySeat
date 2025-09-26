package com.valtech.team3.bookmyseatbackend.dao.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.valtech.team3.bookmyseatbackend.dao.impl.ShiftDetailsDAOImpl;
import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class ShiftDetailsDAOImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;
	@InjectMocks
	private ShiftDetailsDAOImpl shiftDetailsDAO;

	@Test
	void testGetShiftDetailsById() {
		int shiftId = 1;
		ShiftDetails expectedShiftDetails = createSampleShiftDetails();

		when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(shiftId))).thenReturn(expectedShiftDetails);

		ShiftDetails actualShiftDetails = shiftDetailsDAO.getShiftDetailsById(shiftId);

		assertEquals(expectedShiftDetails, actualShiftDetails);
	}

	@Test
	void testGetAllShiftDetails() {
		List<ShiftDetails> expectedShiftDetailsList = Collections.singletonList(createSampleShiftDetails());
		when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedShiftDetailsList);

		List<ShiftDetails> actualShiftDetailsList = shiftDetailsDAO.getAllShiftDetails();

		assertEquals(expectedShiftDetailsList, actualShiftDetailsList);
	}

	@Test
	void testShiftDetailsRowMapper() throws SQLException {
		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.getInt("id")).thenReturn(1);
		when(resultSet.getString("shift_name")).thenReturn("Morning Shift");
		when(resultSet.getTime("start_time")).thenReturn(Time.valueOf(LocalTime.of(9, 0)));
		when(resultSet.getTime("end_time")).thenReturn(Time.valueOf(LocalTime.of(17, 0)));

		ShiftDetailsDAOImpl.ShiftDetailsRowMapper rowMapper = new ShiftDetailsDAOImpl().new ShiftDetailsRowMapper();
		ShiftDetails shiftDetails = rowMapper.mapRow(resultSet, 1);

		assertEquals(1, shiftDetails.getId());
		assertEquals("Morning Shift", shiftDetails.getShiftName());
		assertEquals(LocalTime.of(9, 0), shiftDetails.getStartTime());
		assertEquals(LocalTime.of(17, 0), shiftDetails.getEndTime());
	}

	private ShiftDetails createSampleShiftDetails() {
		ShiftDetails shiftDetails = new ShiftDetails();
		shiftDetails.setId(1);
		shiftDetails.setShiftName("Morning Shift");
		shiftDetails.setStartTime(LocalTime.of(9, 0));
		shiftDetails.setEndTime(LocalTime.of(17, 0));

		return shiftDetails;
	}
}