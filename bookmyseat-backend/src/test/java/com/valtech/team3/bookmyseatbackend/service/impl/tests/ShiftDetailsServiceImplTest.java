package com.valtech.team3.bookmyseatbackend.service.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.valtech.team3.bookmyseatbackend.dao.ShiftDetailsDAO;
import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;
import com.valtech.team3.bookmyseatbackend.exceptions.DataBaseAccessException;
import com.valtech.team3.bookmyseatbackend.service.impl.ShiftDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
class ShiftDetailsServiceImplTest {

	@Mock
	private ShiftDetailsDAO shiftDetailsDAO;
	@InjectMocks
	private ShiftDetailsServiceImpl shiftDetailsService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetAllShiftDetails() {
		ShiftDetails shift1 = new ShiftDetails(1, "Morning Shift", LocalTime.parse("06:00:00"), LocalTime.parse("15:00:00"), LocalDateTime.parse("2024-02-05T12:00:00"));
		ShiftDetails shift2 = new ShiftDetails(2, "General Shift", LocalTime.parse("09:00:00"), LocalTime.parse("18:00:00"), LocalDateTime.parse("2024-02-05T12:00:00"));
		List<ShiftDetails> expectedShifts = Arrays.asList(shift1, shift2);
		when(shiftDetailsDAO.getAllShiftDetails()).thenReturn(expectedShifts);

		List<ShiftDetails> actualShifts = shiftDetailsService.getAllShiftDetails();

		assertEquals(expectedShifts, actualShifts);
		verify(shiftDetailsDAO).getAllShiftDetails();
	}

	@Test
	void testGetAllShiftDetails_RuntimeException() {
		when(shiftDetailsDAO.getAllShiftDetails()).thenThrow(new RuntimeException("Simulated DAO exception"));

		assertThrows(DataBaseAccessException.class, () -> shiftDetailsService.getAllShiftDetails());
	}
}