package com.valtech.bookmyseat.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.valtech.bookmyseat.dao.SeatDAO;
import com.valtech.bookmyseat.entity.Seat;
import com.valtech.bookmyseat.exception.DataBaseAccessException;
import com.valtech.bookmyseat.model.BookingModel;
import com.valtech.bookmyseat.serviceimpl.SeatServiceImpl;

@ExtendWith(MockitoExtension.class)
class SeatServiceImplTest {

	@Mock
	private SeatDAO seatDAO;

	@InjectMocks
	private SeatServiceImpl seatServiceImpl;
//
//	@Test
//	void testGetAvailableSeatsByFloorOnDate() {
//		BookingModel bookingModel = new BookingModel();
//		bookingModel.setFloorId(1);
//		bookingModel.setStartDate(new Date());
//		bookingModel.setEndDate(new Date());
//		List<Seat> availableSeats = new ArrayList<>();
//		availableSeats.add(new Seat());
//		when(seatDAO.findAvailableSeatsByFloorOnDate(bookingModel.getFloorId(), bookingModel.getStartDate(),
//				bookingModel.getEndDate())).thenReturn(availableSeats);
//		List<Seat> result = seatServiceImpl.getAvailableSeatsByFloorOnDate(bookingModel);
//		verify(seatDAO).findAvailableSeatsByFloorOnDate(bookingModel.getFloorId(), bookingModel.getStartDate(),
//				bookingModel.getEndDate());
//		assertEquals(availableSeats, result);
//	}

//	@Test
//	void testGetAvailableSeatsByFloorOnDate_DataBaseAccessException() {
//		BookingModel bookingModel = new BookingModel();
//		when(seatDAO.findAvailableSeatsByFloorOnDate(bookingModel.getFloorId(), bookingModel.getStartDate(),
//				bookingModel.getEndDate())).thenThrow(new DataBaseAccessException("Test exception"));
//		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class,
//				() -> seatServiceImpl.getAvailableSeatsByFloorOnDate(bookingModel));
//		assertEquals("Failed to get all seats by floor !", exception.getMessage());
//	}
}
