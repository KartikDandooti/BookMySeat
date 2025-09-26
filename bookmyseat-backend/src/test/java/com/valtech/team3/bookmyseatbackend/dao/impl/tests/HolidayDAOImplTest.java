package com.valtech.team3.bookmyseatbackend.dao.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.valtech.team3.bookmyseatbackend.dao.impl.HolidayDAOImpl;
import com.valtech.team3.bookmyseatbackend.entities.Holiday;
import com.valtech.team3.bookmyseatbackend.models.HolidayModel;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class HolidayDAOImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;
	@InjectMocks
	private HolidayDAOImpl holidayDAO;

	@Test
	void testGetHolidayByDate() {
		Holiday holiday1 = new Holiday();
		holiday1.setHolidayDate(LocalDate.now());
		Holiday holiday2 = new Holiday();
		holiday2.setHolidayDate(LocalDate.now());

		List<Holiday> expectedHolidays = Arrays.asList(holiday1, holiday2);

		when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class))).thenReturn(expectedHolidays);

		List<Holiday> actualHolidays = holidayDAO.getHolidayByDate();

		assertEquals(expectedHolidays, actualHolidays);
	}

	@Test
	void testGetAllHolidays_NoHolidays() {
		when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class))).thenReturn(new ArrayList<>());

		List<Holiday> result = holidayDAO.getAllHolidays();

		assertTrue(result.isEmpty());
	}

	@Test
	void testGetAllHolidays_WithHolidays() {
		List<Holiday> mockHolidays = new ArrayList<>();
		mockHolidays.add(new Holiday(1, "New Year", LocalDate.now()));
		mockHolidays.add(new Holiday(2, "Sankranthi", LocalDate.now().plusDays(1)));

		when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class))).thenReturn(mockHolidays);

		List<Holiday> result = holidayDAO.getAllHolidays();

		assertEquals(mockHolidays, result);
	}

	@Test
	void testCreateHoliday() {
		HolidayModel holidayModel = new HolidayModel();
		holidayModel.setHolidayDate(LocalDate.now());
		holidayModel.setHolidayName("Test Holiday");

		holidayDAO.createHoliday(holidayModel);

		verify(jdbcTemplate).update(anyString(), eq(holidayModel.getHolidayDate()), eq(holidayModel.getHolidayName()));
	}

	@Test
	void testUpdateHoliday() {
		HolidayModel holidayModel = new HolidayModel();
		holidayModel.setId(1); // Set the ID of the holiday
		holidayModel.setHolidayDate(LocalDate.now());
		holidayModel.setHolidayName("Updated Holiday");

		holidayDAO.updateHoliday(holidayModel);

		verify(jdbcTemplate).update(anyString(), eq(holidayModel.getHolidayName()), eq(holidayModel.getHolidayDate()), eq(holidayModel.getId()));
	}

	@Test
	void testIsHolidayExists_HolidayExists() {
		when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(LocalDate.class), anyString()))
				.thenReturn(1);

		boolean result = holidayDAO.isHolidayExists(LocalDate.now(), "Test Holiday");

		assertTrue(result);
	}

	@Test
	void testIsHolidayExists_HolidayDoesNotExist() {
		when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(LocalDate.class), anyString()))
				.thenReturn(0);

		boolean result = holidayDAO.isHolidayExists(LocalDate.now(), "Test Holiday");

		assertFalse(result);
	}

	@Test
	void testGetAllFutureHolidays_FutureHolidaysExist() {
		List<Holiday> expectedHolidays = new ArrayList<>();
		expectedHolidays.add(new Holiday(3, "Ugadi", LocalDate.of(2024, 4, 9)));
		expectedHolidays.add(new Holiday(4, "Ramdan", LocalDate.of(2024, 4, 11)));
		expectedHolidays.add(new Holiday(5, "May Day", LocalDate.of(2024, 5, 1)));

		String selectQuery = "SELECT * FROM HOLIDAY WHERE HOLIDAY_DATE >= CURDATE()";
		when(jdbcTemplate.query(eq(selectQuery), any(RowMapper.class))).thenReturn(expectedHolidays);

		List<Holiday> actualHolidays = holidayDAO.getAllFutureHolidays();

		assertEquals(expectedHolidays, actualHolidays);
	}
}