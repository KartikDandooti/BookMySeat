package com.valtech.bookmyseat.daoimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.valtech.bookmyseat.entity.Holiday;

@ExtendWith(MockitoExtension.class)
class HolidayDAOImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	private HolidayDAOImpl holidayDAO;

	@Test
	void testAddHoliday() {
		Holiday holiday = new Holiday();
		when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);
		holidayDAO.addHoliday(holiday);
		verify(jdbcTemplate).update(anyString(), any(Object[].class));
	}

	@Test
	void testDeleteHoliday() {
		int holidayId = 123;
		int rowsAffected = 1;
		when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(rowsAffected);
		boolean result = holidayDAO.deleteHoliday(holidayId);
		verify(jdbcTemplate).update(anyString(), any(Object[].class));
		assertTrue(result);
	}

	@Test
	void testDeleteHoliday_NotFound() {
		int holidayId = 123;
		int rowsAffected = 0;
		when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(rowsAffected);
		boolean result = holidayDAO.deleteHoliday(holidayId);
		verify(jdbcTemplate).update(anyString(), any(Object[].class));
		assertFalse(result);
	}

	@SuppressWarnings("unchecked")
	@Test
	void testGetHolidayByDate() {
		List<Holiday> expectedHolidays = new ArrayList<>();
		when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class))).thenReturn(expectedHolidays);
		List<Holiday> result = holidayDAO.getHolidayByDate();
		verify(jdbcTemplate).query(anyString(), any(BeanPropertyRowMapper.class));
		assertEquals(expectedHolidays, result);
	}
}