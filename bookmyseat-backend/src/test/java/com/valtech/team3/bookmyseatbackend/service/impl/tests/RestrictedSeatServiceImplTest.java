package com.valtech.team3.bookmyseatbackend.service.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.valtech.team3.bookmyseatbackend.dao.RestrictedSeatDAO;
import com.valtech.team3.bookmyseatbackend.dao.SeatDAO;
import com.valtech.team3.bookmyseatbackend.entities.RestrictedSeat;
import com.valtech.team3.bookmyseatbackend.entities.Seat;
import com.valtech.team3.bookmyseatbackend.exceptions.DataBaseAccessException;
import com.valtech.team3.bookmyseatbackend.models.RestrictedSeatModel;
import com.valtech.team3.bookmyseatbackend.service.impl.RestrictedSeatServiceImpl;

@ExtendWith(MockitoExtension.class)
class RestrictedSeatServiceImplTest {

	@Mock
	private SeatDAO seatDAO;
	@Mock
	private RestrictedSeatDAO restrictedSeatDAO;
	@InjectMocks
	private RestrictedSeatServiceImpl restrictedSeatService;

	@Test
	void testCreateRestrictionSuccess() {
		int userId = 1;
		int seatId = 2;
		RestrictedSeatModel restrictedSeatModel = new RestrictedSeatModel();
		restrictedSeatModel.setSeatId(seatId);
		restrictedSeatModel.setUserId(userId);

		when(restrictedSeatDAO.isExistsRestrictionForUser(userId)).thenReturn(false);
		when(restrictedSeatDAO.createUserRestriction(userId)).thenReturn(3);
		doNothing().when(seatDAO).updateSeatRestriction(seatId, 3);

		String result = restrictedSeatService.createUserRestriction(restrictedSeatModel);

		assertEquals("Seat Restriction created successfully !", result);
		verify(restrictedSeatDAO, times(1)).isExistsRestrictionForUser(userId);
		verify(restrictedSeatDAO, times(1)).createUserRestriction(userId);
		verify(seatDAO, times(1)).updateSeatRestriction(seatId, 3);
		verifyNoMoreInteractions(restrictedSeatDAO, seatDAO);
	}

	@Test
	void testCreateRestrictionExistingRestriction() {
		int userId = 1;
		int seatId = 2;
		RestrictedSeatModel restrictedSeatModel = new RestrictedSeatModel();
		restrictedSeatModel.setSeatId(seatId);
		restrictedSeatModel.setUserId(userId);

		when(restrictedSeatDAO.isExistsRestrictionForUser(userId)).thenReturn(true);

		assertThrows(DataBaseAccessException.class, () -> restrictedSeatService.createUserRestriction(restrictedSeatModel), "Seat is already reserved for user !");
	}

	@Test
	void testCreateRestrictionDbException() {
		int userId = 1;
		int seatId = 2;
		RestrictedSeatModel restrictedSeatModel = new RestrictedSeatModel();
		restrictedSeatModel.setSeatId(seatId);
		restrictedSeatModel.setUserId(userId);

		when(restrictedSeatDAO.isExistsRestrictionForUser(userId)).thenReturn(false);
		doThrow(new RuntimeException()).when(restrictedSeatDAO).createUserRestriction(userId);

		assertThrows(DataBaseAccessException.class, () -> restrictedSeatService.createUserRestriction(restrictedSeatModel), "Failed to create Restriction. Please try again !");
	}

	@Test
	void testRemoveRestrictionSuccess() {
		int seatId = 1;
		RestrictedSeat restrictedSeat = new RestrictedSeat();
		restrictedSeat.setId(2);
		Seat seat = new Seat();
		seat.setId(seatId);
		seat.setRestrictedSeat(restrictedSeat);
		when(seatDAO.getSeatById(seatId)).thenReturn(seat);

		String result = restrictedSeatService.removeRestriction(seatId);

		assertEquals("Seat Restriction removed successfully !", result);
		verify(seatDAO, times(1)).getSeatById(seatId);
		verify(seatDAO, times(1)).updateSeatRestriction(seatId, null);
		verify(restrictedSeatDAO, times(1)).removeRestriction(restrictedSeat.getId());
		verifyNoMoreInteractions(restrictedSeatDAO, seatDAO);
	}

	@Test
	void test_remove_restriction_invalid_seatId() {
		int seatId = -1;

		when(seatDAO.getSeatById(seatId)).thenReturn(null);

		assertThrows(DataBaseAccessException.class, () -> {
			restrictedSeatService.removeRestriction(seatId);
		});
		verify(seatDAO, never()).updateSeatRestriction(anyInt(), any());
		verify(restrictedSeatDAO, never()).removeRestriction(anyInt());
	}

	@Test
	void testCreateProjectRestriction_Success() {
		RestrictedSeatModel restrictedSeatModel = new RestrictedSeatModel();
		restrictedSeatModel.setProjectId(1);
		restrictedSeatModel.setSeats(Arrays.asList(1, 2, 3));

		when(restrictedSeatDAO.isExistsRestrictionForProject(restrictedSeatModel.getProjectId())).thenReturn(false);
		when(restrictedSeatDAO.createProjectRestriction(restrictedSeatModel.getProjectId())).thenReturn(1);

		String result = restrictedSeatService.createProjectRestriction(restrictedSeatModel);

		assertEquals("Seat Restriction for Project created successfully !", result);
		verify(restrictedSeatDAO).createProjectRestriction(restrictedSeatModel.getProjectId());
		for (int seatId : restrictedSeatModel.getSeats()) {
			verify(seatDAO).updateSeatRestriction(seatId, 1);
		}
	}

	@Test
	void testCreateProjectRestriction_RestrictionExists_Success() {
		RestrictedSeatModel restrictedSeatModel = new RestrictedSeatModel();
		restrictedSeatModel.setProjectId(1);
		restrictedSeatModel.setSeats(Arrays.asList(1, 2, 3));

		when(restrictedSeatDAO.isExistsRestrictionForProject(restrictedSeatModel.getProjectId())).thenReturn(true);
		RestrictedSeat restrictedSeat = new RestrictedSeat();
		restrictedSeat.setId(1);

		when(restrictedSeatDAO.getRestrictedSeatByProjectId(restrictedSeatModel.getProjectId())).thenReturn(restrictedSeat);

		String result = restrictedSeatService.createProjectRestriction(restrictedSeatModel);

		assertEquals("Seat Restriction for Project created successfully !", result);
		for (int seatId : restrictedSeatModel.getSeats()) {
			verify(seatDAO).updateSeatRestriction(seatId, 1);
		}
	}

	@Test
	void testCreateProjectRestriction_Failure() {
		RestrictedSeatModel restrictedSeatModel = new RestrictedSeatModel();
		restrictedSeatModel.setProjectId(1);

		when(restrictedSeatDAO.isExistsRestrictionForProject(1)).thenReturn(true);

		assertThrows(DataBaseAccessException.class, () -> restrictedSeatService.createProjectRestriction(restrictedSeatModel));

		verify(restrictedSeatDAO, never()).createProjectRestriction(anyInt());
		verify(seatDAO, never()).updateSeatRestriction(anyInt(), anyInt());
	}

	@Test
	void testRemoveProjectRestriction_Success() {
		RestrictedSeat restrictedSeat = new RestrictedSeat();
		restrictedSeat.setId(1);
		when(restrictedSeatDAO.getRestrictedSeatByProjectId(1)).thenReturn(restrictedSeat);

		String result = restrictedSeatService.removeProjectRestriction(1);

		assertEquals("Seat Restriction for Project removed successfully !", result);
		verify(seatDAO).updateProjectReserveation(1);
		verify(restrictedSeatDAO).removeRestriction(1);
	}

	@Test
	void testRemoveProjectRestrictionForSeat_Success() {
		Seat seat = new Seat();
		seat.setId(1);
		when(seatDAO.getSeatById(1)).thenReturn(seat);

		String result = restrictedSeatService.removeProjectRestrictionForSeat(1);

		assertEquals("Seat Restriction for Project removed successfully !", result);
		verify(seatDAO).updateSeatRestriction(1, null);
	}

	@Test
	void testRemoveProjectRestriction_Failure() {
		int projectId = 1;

		when(restrictedSeatDAO.getRestrictedSeatByProjectId(projectId)).thenThrow(new RuntimeException("Database error"));

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class, () -> restrictedSeatService.removeProjectRestriction(projectId));

		assertEquals("Failed to remove Restriction. Please try again !", exception.getMessage());
		verify(seatDAO, never()).updateProjectReserveation(anyInt());
		verify(restrictedSeatDAO, never()).removeRestriction(anyInt());
	}

	@Test
	void testRemoveProjectRestrictionForSeat_Failure() {
		int seatId = 1;

		when(seatDAO.getSeatById(seatId)).thenThrow(new RuntimeException("Database error"));

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class, () -> restrictedSeatService.removeProjectRestrictionForSeat(seatId));

		assertEquals("Failed to remove Restriction. Please try again !", exception.getMessage());
		verify(seatDAO, never()).updateSeatRestriction(anyInt(), any());
	}
}