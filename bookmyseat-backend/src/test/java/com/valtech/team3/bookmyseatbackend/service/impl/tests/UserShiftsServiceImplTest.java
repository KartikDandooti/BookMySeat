package com.valtech.team3.bookmyseatbackend.service.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.valtech.team3.bookmyseatbackend.dao.UserShiftsDAO;
import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;
import com.valtech.team3.bookmyseatbackend.exceptions.FailedToAddShiftException;
import com.valtech.team3.bookmyseatbackend.exceptions.FailedToDeleteShiftsException;
import com.valtech.team3.bookmyseatbackend.exceptions.ShiftsNotFoundException;
import com.valtech.team3.bookmyseatbackend.service.impl.UserShiftsServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserShiftsServiceImplTest {

	@Mock
	private UserShiftsDAO userShiftsDAO;
	@InjectMocks
	private UserShiftsServiceImpl userShiftsService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddShiftsToUser_Success() {
		int userId = 1;
		int shiftId = 1;

		doNothing().when(userShiftsDAO).addUserShift(userId, shiftId);
		userShiftsService.addShiftsToUser(userId, shiftId);

		verify(userShiftsDAO).addUserShift(userId, shiftId);
	}

	@Test
	void testAddShiftsToUser_FailedToAddShiftException() {
		int userId = 1;
		int shiftId = 1;

		doThrow(new RuntimeException()).when(userShiftsDAO).addUserShift(userId, shiftId);

		assertThrows(FailedToAddShiftException.class, () -> userShiftsService.addShiftsToUser(userId, shiftId));
	}

	@Test
	void testGetAllShiftDetailsByUserId_ShiftsNotFoundException() {
		int userId = 1;
		when(userShiftsDAO.getAllShiftDetailsByUserId(userId)).thenReturn(new ArrayList<>());

		assertThrows(ShiftsNotFoundException.class, () -> userShiftsService.getAllShiftDetailsByUserId(userId));
	}

	@Test
	void testGetAllShiftDetailsByUserId_Success() {
		int userId = 1;
		List<ShiftDetails> shiftDetailsList = new ArrayList<>();
		shiftDetailsList.add(new ShiftDetails());
		shiftDetailsList.add(new ShiftDetails(2, "General Shift", LocalTime.of(9, 0), LocalTime.of(18, 0)));
		shiftDetailsList.add(new ShiftDetails(3, "General Shift", LocalTime.of(10, 0), LocalTime.of(19, 0)));
		when(userShiftsDAO.getAllShiftDetailsByUserId(userId)).thenReturn(shiftDetailsList);

		List<ShiftDetails> result = userShiftsService.getAllShiftDetailsByUserId(userId);
		assertEquals(shiftDetailsList, result);
	}

	@Test
	void testDeleteShiftsForUser_Success() {
		int userId = 1;

		doNothing().when(userShiftsDAO).deleteUserShifts(userId);
		userShiftsService.deleteShiftsForUser(userId);
		verify(userShiftsDAO).deleteUserShifts(userId);
	}

	@Test
	void testDeleteShiftsForUser_FailedToDeleteShiftsException() {
		int userId = 1;
		doThrow(new RuntimeException()).when(userShiftsDAO).deleteUserShifts(userId);

		assertThrows(FailedToDeleteShiftsException.class, () -> userShiftsService.deleteShiftsForUser(userId));
	}
}