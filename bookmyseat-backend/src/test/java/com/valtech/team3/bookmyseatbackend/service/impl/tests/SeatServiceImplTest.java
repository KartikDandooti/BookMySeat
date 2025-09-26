package com.valtech.team3.bookmyseatbackend.service.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.valtech.team3.bookmyseatbackend.dao.SeatDAO;
import com.valtech.team3.bookmyseatbackend.entities.Building;
import com.valtech.team3.bookmyseatbackend.entities.Floor;
import com.valtech.team3.bookmyseatbackend.entities.Seat;
import com.valtech.team3.bookmyseatbackend.exceptions.DataBaseAccessException;
import com.valtech.team3.bookmyseatbackend.models.BookingModel;
import com.valtech.team3.bookmyseatbackend.service.impl.SeatServiceImpl;

@ExtendWith(MockitoExtension.class)
class SeatServiceImplTest {

	@Mock
	private SeatDAO seatDAO;
	@InjectMocks
	private SeatServiceImpl seatService;
	private BookingModel bookingModel;

	@BeforeEach
	void setUp() {
		bookingModel = new BookingModel();
		bookingModel.setFloorId(1);
		bookingModel.setStartDate(LocalDate.of(2024, 2, 10));
		bookingModel.setEndDate(LocalDate.of(2024, 2, 15));
	}

	@Test
	void testGetAvailableSeatsByFloorOnDate_Success() {
		List<Seat> seats = new ArrayList<>();
		Floor floor = new Floor(1, "First Floor", new Building("Mass Unique", "Bangalore"));
		seats.add(new Seat(1, 1, floor, false));
		seats.add(new Seat(2, 3, floor, false));

		when(seatDAO.findAvailableSeatsByFloorOnDate(anyInt(), any(LocalDate.class), any(LocalDate.class)))
				.thenReturn(seats);

		List<Seat> result = seatService.getAvailableSeatsByFloorOnDate(bookingModel);

		verify(seatDAO).findAvailableSeatsByFloorOnDate(bookingModel.getFloorId(), bookingModel.getStartDate(),
				bookingModel.getEndDate());

		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	void testGetAvailableSeatsByFloorOnDate_DataBaseAccessException() {
		when(seatDAO.findAvailableSeatsByFloorOnDate(anyInt(), any(LocalDate.class), any(LocalDate.class)))
				.thenThrow(new RuntimeException("Test Exception"));

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class,
				() -> seatService.getAvailableSeatsByFloorOnDate(bookingModel));

		assertEquals("Failed to get all seats by floor !", exception.getMessage());
	}

	@Test
	void test_getAllSeatsByFloor_shouldReturnListOfAllSeats() {
		int floorId = 1;

		List<Seat> expectedSeats = new ArrayList<>();
		Floor floor = new Floor(1, "First Floor", new Building("Mass Unique", "Bangalore"));
		expectedSeats.add(new Seat(1, 1, floor, false));

		when(seatDAO.getSeatsByFloor(floorId)).thenReturn(expectedSeats);

		List<Seat> actualSeats = seatService.getAllSeatsByFloor(floorId);

		assertEquals(expectedSeats, actualSeats);
	}

	@Test
	void testGetAllSeatsByFloor_ExceptionHandling() {
		int floorId = 0;
		when(seatDAO.getSeatsByFloor(floorId)).thenThrow(new RuntimeException("Test exception"));

		Throwable exception = assertThrows(DataBaseAccessException.class,
				() -> seatService.getAllSeatsByFloor(floorId));

		assertEquals("Failed to get all seats by floor !", exception.getMessage());
	}

	@Test
	void testGetAllReservedSeats_Success() {
		List<Seat> reservedSeats = new ArrayList<>();
		Floor floor = new Floor(1, "First Floor", new Building("Mass Unique", "Bangalore"));
		reservedSeats.add(new Seat(1, 1, floor, false));
		reservedSeats.add(new Seat(2, 5, floor, false));
		when(seatDAO.getAllReservedSeats()).thenReturn(reservedSeats);

		List<Seat> result = seatService.getAllReservedSeats();

		assertEquals(reservedSeats, result);
	}

	@Test
    void testGetAllReservedSeats_Failure() {
        when(seatDAO.getAllReservedSeats()).thenThrow(new RuntimeException("Database error"));

        assertThrows(DataBaseAccessException.class, () -> seatService.getAllReservedSeats());
    }

	@Test
	void testGetAllUserReservedSeats_Success() {
		Floor floor = new Floor(1, "First Floor", new Building("Mass Unique", "Bangalore"));
		List<Seat> userReservedSeats = Arrays.asList(new Seat(1, 1, floor, false), new Seat(2, 5, floor, false));
		when(seatDAO.getAllUserReservedSeats()).thenReturn(userReservedSeats);

		List<Seat> result = seatService.getAllUserReservedSeats();

		assertEquals(userReservedSeats, result);
	}

	@Test
    void testGetAllUserReservedSeats_Failure() {
        when(seatDAO.getAllUserReservedSeats()).thenThrow(new RuntimeException("Database error"));

        assertThrows(DataBaseAccessException.class, () -> seatService.getAllUserReservedSeats());
    }

	@Test
	void testGetAllProjectReservedSeats_Success() {
		Floor floor = new Floor(1, "First Floor", new Building("Mass Unique", "Bangalore"));
		List<Seat> projectReservedSeats = Arrays.asList(new Seat(1, 1, floor, false), new Seat(2, 5, floor, false));
		when(seatDAO.getAllProjectReservedSeats()).thenReturn(projectReservedSeats);

		List<Seat> result = seatService.getAllProjectReservedSeats();

		assertEquals(projectReservedSeats, result);
	}

	@Test
    void testGetAllProjectReservedSeats_Failure() {
        when(seatDAO.getAllProjectReservedSeats()).thenThrow(new RuntimeException("Database error"));

        assertThrows(DataBaseAccessException.class, () -> seatService.getAllProjectReservedSeats());
    }
}