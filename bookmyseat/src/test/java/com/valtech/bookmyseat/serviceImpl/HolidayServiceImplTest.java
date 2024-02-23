package com.valtech.bookmyseat.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.valtech.bookmyseat.dao.HolidayDAO;
import com.valtech.bookmyseat.entity.Holiday;
import com.valtech.bookmyseat.serviceimpl.HolidayServiceImpl;

@ExtendWith(MockitoExtension.class)
class HolidayServiceImplTest {

	@Mock
	private HolidayDAO holidayDAO;

	@InjectMocks
	private HolidayServiceImpl holidayService;

	@Test
	void testGetAllHolidays() {
		Holiday holiday = new Holiday();
		holiday.setHolidayName("New Year");
		holiday.setHolidayDate(LocalDate.of(2024, 01, 01));
		Holiday holiday1 = new Holiday();
		holiday1.setHolidayName("Christmas");
		holiday1.setHolidayDate(LocalDate.of(2024, 12, 25));
		List<Holiday> mockHolidays = new ArrayList<>();
		mockHolidays.add(holiday);
		mockHolidays.add(holiday1);
		when(holidayDAO.getAllHolidays()).thenReturn(mockHolidays);
		List<Holiday> result = holidayService.getAllHolidays();
		verify(holidayDAO).getAllHolidays();
		assertEquals(mockHolidays, result);
	}

	@Test
	void testAddHolidayByAdmin() {
		Holiday holiday = new Holiday();
		String result = holidayService.addHolidayByAdmin(holiday);
		verify(holidayDAO).addHoliday(holiday);
		assertEquals("Holiday Added succesfully", result);
	}

	@Test
	void testDeleteHoliday() {
		int holidayId = 123;
		when(holidayDAO.deleteHoliday(holidayId)).thenReturn(true);
		String result = holidayService.deleteHoliday(holidayId);
		verify(holidayDAO).deleteHoliday(holidayId);
		assertEquals("Holiday Deatils with Id " + holidayId + " has been deleted successfully", result);
	}

	@Test
	void testDeleteHoliday_Failure() {
		int holidayId = 123;
		when(holidayDAO.deleteHoliday(holidayId)).thenReturn(false);
		String result = holidayService.deleteHoliday(holidayId);
		assertEquals("Failed to delete the Holiday details with id " + holidayId, result);
	}

	@Test
	void testGetHoildayBydate() {
		List<Holiday> mockHolidays = new ArrayList<>();
		when(holidayDAO.getHolidayByDate()).thenReturn(mockHolidays);
		List<Holiday> result = holidayService.getHoildayBydate();
		verify(holidayDAO).getHolidayByDate();
		assertEquals(mockHolidays, result);
	}
}
