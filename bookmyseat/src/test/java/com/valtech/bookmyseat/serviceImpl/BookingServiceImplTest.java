package com.valtech.bookmyseat.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import com.valtech.bookmyseat.dao.BookingDAO;
import com.valtech.bookmyseat.dao.SeatDAO;
import com.valtech.bookmyseat.dao.ShiftDAO;
import com.valtech.bookmyseat.entity.Booking;
import com.valtech.bookmyseat.entity.BookingMapping;
import com.valtech.bookmyseat.exception.CustomDataAccessException;
import com.valtech.bookmyseat.exception.DataBaseAccessException;
import com.valtech.bookmyseat.model.BookingDTO;
import com.valtech.bookmyseat.serviceimpl.BookingServiceImpl;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

	@Mock
	private BookingDAO bookingDAO;

	@Mock
	private SeatDAO seatDAO;

	@Mock
	private ShiftDAO shiftDAO;

	@InjectMocks
	private BookingServiceImpl bookingService;

	@Test
	void testGetUserPreferredSeats_Success() {
		int floorId = 1;
		int projectId = 1;
		List<Booking> expectedSeats = new ArrayList<>();
		expectedSeats.add(new Booking());
		when(bookingDAO.userPreferredSeats(floorId, projectId)).thenReturn(expectedSeats);
		List<Booking> actualSeats = bookingService.getUserPreferredSeats(floorId, projectId);
		assertEquals(expectedSeats.size(), actualSeats.size());
	}

	@Test
	void testCustomDataAccessException() {
		int floorId = 1;
		int projectId = 2;
		when(bookingDAO.userPreferredSeats(floorId, projectId)).thenThrow(new DataAccessException("Test exception") {
			private static final long serialVersionUID = 1L;
		});
		assertThrows(CustomDataAccessException.class, () -> bookingService.getUserPreferredSeats(floorId, projectId));
	}

	@Test
	void testGetAllBookings_Success() {
		List<BookingDTO> mockBookings = new ArrayList<>();
		mockBookings.add(new BookingDTO());
		when(bookingDAO.getAllBookings()).thenReturn(mockBookings);
		List<BookingDTO> result = bookingService.getAllBookings();
		assertEquals(mockBookings, result);
	}

	@Test
	void testGetAllBookings_EmptyList() {
		when(bookingDAO.getAllBookings()).thenReturn(new ArrayList<>());
		assertThrows(DataBaseAccessException.class, () -> bookingService.getAllBookings());
	}

	@Test
	void testGetAllBookingDetails() {
		List<BookingMapping> expectedBookings = new ArrayList<>();
		doReturn(expectedBookings).when(bookingDAO).getAllBookingDetails();
		List<BookingMapping> result = bookingService.getAllBookingDetails();
		assertEquals(expectedBookings, result);
		verify(bookingDAO).getAllBookingDetails();
	}

	@Test
	void testApproveUserAttendance() {
		int userId = 123;
		bookingService.approveUserAttendance(userId);
		verify(bookingDAO).approvalAttendance(userId);
	}

	@Test
	void testApproveUserAttendance_DataAccessException() {
		int userId = 123;
		doThrow(new DataAccessException("Test Exception") {
			static final long serialVersionUID = 1L;
		}).when(bookingDAO).approvalAttendance(userId);
		assertThrows(CustomDataAccessException.class, () -> bookingService.approveUserAttendance(userId));
	}
}
