package com.valtech.team3.bookmyseatbackend.service.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.valtech.team3.bookmyseatbackend.dao.HolidayDAO;
import com.valtech.team3.bookmyseatbackend.entities.Holiday;
import com.valtech.team3.bookmyseatbackend.exceptions.DataBaseAccessException;
import com.valtech.team3.bookmyseatbackend.models.HolidayModel;
import com.valtech.team3.bookmyseatbackend.service.impl.HolidayServiceImpl;

@ExtendWith(MockitoExtension.class)
class HolidayServiceImplTest {

	@Mock
	private HolidayDAO holidayDAO;

	@InjectMocks
	private HolidayServiceImpl holidayService;

	@Test
	void testGetAllHolidays_Success() {
		List<Holiday> mockHolidays = new ArrayList<>();
		mockHolidays.add(new Holiday(1, "Gandhi Jayanti", LocalDate.of(2024, 10, 02)));

		when(holidayDAO.getAllHolidays()).thenReturn(mockHolidays);

		List<Holiday> result = holidayService.getAllHolidays();

		assertEquals(mockHolidays, result);
	}

	@Test
	void testGetAllHolidays_Exception() {
		when(holidayDAO.getAllHolidays()).thenThrow(new RuntimeException("Database error"));

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class,
				() -> holidayService.getAllHolidays());

		assertEquals("Error retrieving holiday's from database !", exception.getMessage());
	}

	@Test
	void testCreateNewHoliday_Success() {
		HolidayModel holidayModel = new HolidayModel();

		when(holidayDAO.isHolidayExists(any(), any())).thenReturn(false);

		holidayService.createNewHoliday(holidayModel);

		ArgumentCaptor<HolidayModel> argumentCaptor = ArgumentCaptor.forClass(HolidayModel.class);
		verify(holidayDAO).createHoliday(argumentCaptor.capture());
		assertEquals(holidayModel, argumentCaptor.getValue());
	}

	@Test
	void testCreateNewHoliday_HolidayAlreadyExists() {
		HolidayModel holidayModel = new HolidayModel();

		when(holidayDAO.isHolidayExists(any(), any())).thenReturn(true);

		assertThrows(DataBaseAccessException.class, () -> holidayService.createNewHoliday(holidayModel));
	}

	@Test
	void testUpdateHoliday_Success() {
		HolidayModel holidayModel = new HolidayModel();
		holidayModel.setHolidayDate(LocalDate.of(2024, 11, 01));
		holidayModel.setHolidayName("Kannada Rajyotsava");
		doNothing().when(holidayDAO).updateHoliday(holidayModel);
		holidayService.updateHoliday(holidayModel);

		verify(holidayDAO).updateHoliday(holidayModel);
	}

	@Test
	void testUpdateHoliday_Exception() {
		doThrow(new RuntimeException("Database error")).when(holidayDAO).updateHoliday(any());

		HolidayModel holidayModel = new HolidayModel();
		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class, () -> holidayService.updateHoliday(holidayModel));

		assertEquals("Error Updating existing holiday !", exception.getMessage());
	}

	@Test
	void testGetAllFutureHolidays_Success() {
		List<Holiday> expectedHolidays = Arrays.asList(new Holiday(1, "Gandhi Jayanti", LocalDate.of(2024, 10, 02)), new Holiday(2, "Kannada Rajyotsava", LocalDate.of(2024, 11, 01)));
		when(holidayDAO.getAllFutureHolidays()).thenReturn(expectedHolidays);

		List<Holiday> actualHolidays = holidayService.getAllFutureHolidays();

		assertEquals(expectedHolidays, actualHolidays);
	}

	@Test
    void testGetAllFutureHolidays_Failure() {
        when(holidayDAO.getAllFutureHolidays()).thenThrow(new RuntimeException("Database error"));

        assertThrows(DataBaseAccessException.class, () -> holidayService.getAllFutureHolidays());
    }
}