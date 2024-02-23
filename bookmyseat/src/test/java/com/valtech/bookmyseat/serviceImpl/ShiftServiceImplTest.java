package com.valtech.bookmyseat.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import com.valtech.bookmyseat.dao.ShiftDAO;
import com.valtech.bookmyseat.entity.Shift;
import com.valtech.bookmyseat.serviceimpl.ShiftServiceImpl;

@ExtendWith(MockitoExtension.class)
class ShiftServiceImplTest {

	@Mock
	private ShiftDAO shiftDetailsDao;

	@InjectMocks
	private ShiftServiceImpl shiftServiceImpl;

	@Test
	void testGetAllShiftDetails() throws DataAccessException {
		Shift shift1 = new Shift();
		Shift shift2 = new Shift();
		List<Shift> expectedShifts = Arrays.asList(shift1, shift2);
		when(shiftDetailsDao.getAllShiftDetails()).thenReturn(expectedShifts);
		List<Shift> actualShifts = shiftServiceImpl.getAllShiftDetails();
		verify(shiftDetailsDao, times(1)).getAllShiftDetails();
		assertEquals(expectedShifts, actualShifts);
	}

	@Test
	void testAddShiftTime_Success() throws DataAccessException, SQLException {
		Shift shift = new Shift();
		doNothing().when(shiftDetailsDao).addShiftTime(shift);
		String result = shiftServiceImpl.addShiftTime(shift);
		assertEquals("Shift Time added successfully", result);
	}

	@Test
	void testUpdateShiftTime_Success() throws DataAccessException, SQLException {
		int id = 1;
		Shift shift = new Shift();
		doNothing().when(shiftDetailsDao).updateShiftTime(id, shift);
		String result = shiftServiceImpl.updateShiftTime(id, shift);
		assertEquals("Shift time updated successfully", result);
		verify(shiftDetailsDao, times(1)).updateShiftTime(id, shift);
	}

	@Test
	void testUpdateShiftTime_Failure() throws DataAccessException, SQLException {
		int id = 1;
		Shift shift = new Shift();
		String errorMessage = "Database connection failed";
		doThrow(new DataAccessException(errorMessage) {
			private static final long serialVersionUID = 1L;
		}).when(shiftDetailsDao).updateShiftTime(id, shift);
		String result = shiftServiceImpl.updateShiftTime(id, shift);
		assertEquals("Failed to update the shift time " + errorMessage, result);
		verify(shiftDetailsDao, times(1)).updateShiftTime(id, shift);
	}

	@Test
	void testDeleteShift_Success() throws DataAccessException {
		int shiftId = 1;
		when(shiftDetailsDao.deleteShiftDetailsById(shiftId)).thenReturn(true);
		String result = shiftServiceImpl.deleteShift(shiftId);
		assertEquals("Shift Details with Id 1 has been deleted successfully", result);
		verify(shiftDetailsDao).deleteShiftDetailsById(shiftId);
	}

	@Test
	void testDeleteShift_Failure() throws DataAccessException {
		int shiftId = 2;
		when(shiftDetailsDao.deleteShiftDetailsById(shiftId)).thenReturn(false);
		String result = shiftServiceImpl.deleteShift(shiftId);
		assertEquals("Failed to delete the shift details with id 2", result);
		verify(shiftDetailsDao).deleteShiftDetailsById(shiftId);
	}
}
