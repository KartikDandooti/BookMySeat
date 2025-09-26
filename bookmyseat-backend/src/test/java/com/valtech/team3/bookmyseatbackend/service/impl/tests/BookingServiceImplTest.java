package com.valtech.team3.bookmyseatbackend.service.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import com.valtech.team3.bookmyseatbackend.dao.BookingDAO;
import com.valtech.team3.bookmyseatbackend.dao.BookingMappingDAO;
import com.valtech.team3.bookmyseatbackend.dao.HolidayDAO;
import com.valtech.team3.bookmyseatbackend.dao.SeatDAO;
import com.valtech.team3.bookmyseatbackend.dao.ShiftDetailsDAO;
import com.valtech.team3.bookmyseatbackend.dao.UserDAO;
import com.valtech.team3.bookmyseatbackend.entities.Booking;
import com.valtech.team3.bookmyseatbackend.entities.BookingMapping;
import com.valtech.team3.bookmyseatbackend.entities.Seat;
import com.valtech.team3.bookmyseatbackend.entities.User;
import com.valtech.team3.bookmyseatbackend.exceptions.AlreadyBookedForDateException;
import com.valtech.team3.bookmyseatbackend.exceptions.AttendanceMarkedException;
import com.valtech.team3.bookmyseatbackend.exceptions.DataBaseAccessException;
import com.valtech.team3.bookmyseatbackend.exceptions.NoParkingAvailableException;
import com.valtech.team3.bookmyseatbackend.models.BookingModel;
import com.valtech.team3.bookmyseatbackend.models.UserDashboardModel;
import com.valtech.team3.bookmyseatbackend.service.EmailService;
import com.valtech.team3.bookmyseatbackend.service.impl.BookingServiceImpl;

import jakarta.mail.MessagingException;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

	@Mock
	private UserDAO userDAO;
	@Mock
	private BookingDAO bookingDAO;
	@Mock
	private SeatDAO seatDAO;
	@Mock
	private HolidayDAO holidayDAO;
	@Mock
	private ShiftDetailsDAO shiftDetailsDAO;
	@Mock
	private BookingMappingDAO bookingMappingDAO;
	@Mock
	private EmailService emailService;
	@InjectMocks
	private BookingServiceImpl bookingService;

	@Test
	void testCreateBooking_Success() {
		BookingModel bookingModel = new BookingModel();
		bookingModel.setSeatId(1);
		bookingModel.setStartDate(LocalDate.now());
		bookingModel.setEndDate(LocalDate.now().plusDays(7));
		bookingModel.setParking(false);
		bookingModel.setVehicleType("WHEELER_2");

		User user = new User();
		user.setId(1);
		user.setFirstName("Kruthik");
		user.setLastName("Bhupal");

		Seat seat = new Seat();
		seat.setId(1);

		when(seatDAO.getSeatById(bookingModel.getSeatId())).thenReturn(seat);
		when(bookingService.hasAlreadyBookedForDate(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(false);
		when(bookingMappingDAO.getVehicleTypeCount(eq("WHEELER_2"), any(LocalDate.class), any(LocalDate.class))).thenReturn(20);
		when(bookingMappingDAO.getVehicleTypeCount(eq("WHEELER_4"), any(LocalDate.class), any(LocalDate.class))).thenReturn(5);
		when(bookingDAO.createBooking(eq(bookingModel), eq(user), eq(seat))).thenReturn(1);

		int result = bookingService.createBooking(bookingModel, user);

		assertEquals(1, result);
	}

	@Test
	void testCreateBooking_AlreadyBookedForDate() {
		BookingModel bookingModel = new BookingModel();
		bookingModel.setSeatId(1);
		bookingModel.setStartDate(LocalDate.now());
		bookingModel.setEndDate(LocalDate.now().plusDays(7));
		bookingModel.setParking(false);
		bookingModel.setVehicleType("WHEELER_2");

		User user = new User();
		user.setId(1);
		user.setFirstName("Kruthik");
		user.setLastName("Bhupal");

		when(bookingService.hasAlreadyBookedForDate(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(true);

		AlreadyBookedForDateException exception = assertThrows(AlreadyBookedForDateException.class, () -> bookingService.createBooking(bookingModel, user));

		assertEquals("You have already booked for this date range.", exception.getMessage());
	}

	@Test
	void testCreateBooking_NoParkingAvailable() {
		BookingModel bookingModel = new BookingModel();
		bookingModel.setSeatId(1);
		bookingModel.setStartDate(LocalDate.now());
		bookingModel.setEndDate(LocalDate.now().plusDays(7));
		bookingModel.setParking(true);
		bookingModel.setVehicleType("WHEELER_2");

		User user = new User();
		user.setId(1);
		user.setFirstName("Kruthik");
		user.setLastName("Bhupal");

		when(bookingService.hasAlreadyBookedForDate(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(false);
		when(bookingMappingDAO.getVehicleTypeCount(eq("WHEELER_2"), any(LocalDate.class), any(LocalDate.class))).thenReturn(50);

		NoParkingAvailableException exception = assertThrows(NoParkingAvailableException.class, () -> bookingService.createBooking(bookingModel, user));

		assertEquals("Two-wheeler parking is full. Please use public transport.", exception.getMessage());
	}

	@Test
	void testCreateBooking_NoParkingAvailableForWheeler_4() {
		BookingModel bookingModel = new BookingModel();
		bookingModel.setSeatId(7);
		bookingModel.setStartDate(LocalDate.now());
		bookingModel.setEndDate(LocalDate.now());
		bookingModel.setParking(true);
		bookingModel.setVehicleType("WHEELER_4");

		User user = new User();
		user.setId(2);
		user.setFirstName("Laxman");
		user.setLastName("Kuddemmi");

		when(bookingMappingDAO.getVehicleTypeCount(eq("WHEELER_2"), any(LocalDate.class), any(LocalDate.class))).thenReturn(50);
		when(bookingMappingDAO.getVehicleTypeCount(eq("WHEELER_4"), any(LocalDate.class), any(LocalDate.class))).thenReturn(11);

		NoParkingAvailableException exception = assertThrows(NoParkingAvailableException.class, () -> bookingService.createBooking(bookingModel, user));

		assertEquals("Four-wheeler parking is full. Please use public transport.", exception.getMessage());
	}

	@Test
	void testHasAlreadyBookedForDate_RuntimeException() {
		int userId = 1;
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = LocalDate.now().plusDays(2);

		when(bookingDAO.hasAlreadyBookedForDate(userId, startDate, endDate)).thenThrow(new RuntimeException("Error accessing data"));

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class, () -> bookingService.hasAlreadyBookedForDate(userId, startDate, endDate));

		assertEquals("Failed to check whether seat is already booked for particular date range", exception.getMessage());
	}

	@Test
	void testHasAlreadyBookedForDate_ExceptionThrown() {
		BookingModel bookingModel = new BookingModel();
		bookingModel.setStartDate(LocalDate.of(2024, 2, 1));
		bookingModel.setEndDate(LocalDate.of(2024, 2, 5));
		User user = mock(User.class);

		when(bookingDAO.hasAlreadyBookedForDate(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(true);

		AlreadyBookedForDateException exception = assertThrows(AlreadyBookedForDateException.class, () -> bookingService.createBooking(bookingModel, user));
		assertEquals("You have already booked for this date range.", exception.getMessage());
	}

	@Test
	void testGetAllBookings_Success() {
		int userId = 123;

		List<Booking> bookings = new ArrayList<>();
		bookings.add(new Booking());
		bookings.add(new Booking());

		when(bookingDAO.getAllBookings(userId)).thenReturn(bookings);

		List<Booking> result = bookingService.getAllBookings(userId);

		assertEquals(bookings, result);
	}

	@Test
	void testGetAllBookings_Exception() {
		int userId = 123;
		when(bookingDAO.getAllBookings(userId)).thenThrow(new RuntimeException("Test exception"));

		assertThrows(DataBaseAccessException.class, () -> bookingService.getAllBookings(userId));
	}

	@Test
	void cancelBooking_Success() {
		int bookingId = 123;

		doNothing().when(bookingDAO).cancelBooking(bookingId);

		bookingService.cancelBooking(bookingId);

		verify(bookingDAO).cancelBooking(bookingId);
	}

	@Test
	void cancelBooking_Exception() {
		int bookingId = 123;

		doThrow(new RuntimeException("Test Exception")).when(bookingDAO).cancelBooking(bookingId);

		assertThrows(DataBaseAccessException.class, () -> bookingService.cancelBooking(bookingId));
	}

	@Test
	void testIsAttendanceMarked() {
		List<BookingMapping> bookingMappings = new ArrayList<>();
		BookingMapping mappingWithAttendance = new BookingMapping();
		mappingWithAttendance.setAttendance(true);
		bookingMappings.add(mappingWithAttendance);
		BookingMapping mappingWithoutAttendance = new BookingMapping();
		mappingWithoutAttendance.setAttendance(false);
		bookingMappings.add(mappingWithoutAttendance);

		when(bookingMappingDAO.getBookingMappingsByBookingId(anyInt())).thenReturn(bookingMappings);

		Booking booking = new Booking();
		booking.setId(1);

		boolean attendanceMarked = bookingService.isAttendanceMarked(booking.getId());

		assertTrue(attendanceMarked, "Attendance should be marked");
	}

	@Test
	void getBookingById_ValidId_ReturnsBooking() {
		int bookingId = 1;
		Booking expectedBooking = new Booking();
		expectedBooking.setId(bookingId);
		when(bookingDAO.getBookingById(bookingId)).thenReturn(expectedBooking);

		Booking actualBooking = bookingService.getBookingById(bookingId);

		assertEquals(expectedBooking, actualBooking);
	}

	@Test
	void getBookingById_InvalidId_ReturnsNull() {
		int invalidBookingId = -1;
		when(bookingDAO.getBookingById(invalidBookingId)).thenReturn(null);

		Booking actualBooking = bookingService.getBookingById(invalidBookingId);

		assertNull(actualBooking);
	}

	@Test
	void testGetUserPreferredSeats() {
		int floorId = 1;
		int projectId = 1;
		LocalDate startDate = LocalDate.now();

		when(bookingDAO.userPreferredSeats(floorId, projectId, startDate)).thenReturn(new ArrayList<>());

		bookingService.getUserPreferredSeats(floorId, projectId, startDate);

		verify(bookingDAO).userPreferredSeats(floorId, projectId, startDate);
	}

	@Test
	void testGetUserPreferredSeats_DataAccessException() {
		int floorId = 1;
		int projectId = 1;
		LocalDate startDate = LocalDate.now();

		when(bookingDAO.userPreferredSeats(floorId, projectId, startDate)).thenThrow(new DataAccessException("Error accessing data") {
			private static final long serialVersionUID = 1L;
		});

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class, () -> bookingService.getUserPreferredSeats(floorId, projectId, startDate));

		assertEquals("Failed to get User preferred Seats !", exception.getMessage());
	}

	@Test
	void testUpdateBookingByAdmin() throws MessagingException {
		int bookingId = 1;
		int seatId = 2;
		BookingModel bookingModel = new BookingModel();
		bookingModel.setId(bookingId);
		bookingModel.setSeatId(seatId);
		when(bookingMappingDAO.isAttendanceMarkedForBooking(bookingId)).thenReturn(false);
		when(userDAO.getUserByBookingId(bookingId)).thenReturn(new User()); // Mocking user retrieval

		bookingService.updateBookingByAdmin(bookingModel);

		verify(bookingDAO).updateBookingByAdmin(bookingModel, bookingId, seatId);
		verify(emailService).sendSeatModificationMail(any(User.class));
	}

	@Test
	void testUpdateBookingByAdmin_AttendanceMarked() {
		int bookingId = 1;
		BookingModel bookingModel = new BookingModel();
		bookingModel.setId(bookingId);
		when(bookingMappingDAO.isAttendanceMarkedForBooking(bookingId)).thenReturn(true);

		AttendanceMarkedException exception = org.junit.jupiter.api.Assertions.assertThrows(AttendanceMarkedException.class, () -> bookingService.updateBookingByAdmin(bookingModel));
		org.junit.jupiter.api.Assertions.assertEquals("Attendance is already marked for the specified booking.", exception.getMessage());
		verifyNoInteractions(bookingDAO);
		verifyNoInteractions(userDAO);
		verifyNoInteractions(emailService);
	}

	@Test
	void testGetBookingsforFloorBetweenDates_Success() {
		BookingModel bookingModel = new BookingModel();
		bookingModel.setFloorId(1);
		bookingModel.setStartDate(LocalDate.now());
		bookingModel.setEndDate(LocalDate.now().plusDays(7));

		List<Booking> expectedBookings = new ArrayList<>();

		when(bookingDAO.findBookingsByFloorAndDates(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(expectedBookings);

		List<Booking> result = bookingService.getBookingsforFloorBetweenDates(bookingModel);

		assertEquals(expectedBookings, result);
	}

	@Test
	void testGetBookingsforFloorBetweenDates_Failure() {
		BookingModel bookingModel = new BookingModel();
		bookingModel.setFloorId(1);
		bookingModel.setStartDate(LocalDate.now());
		bookingModel.setEndDate(LocalDate.now().plusDays(7));

		when(bookingDAO.findBookingsByFloorAndDates(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenThrow(new RuntimeException());

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class, () -> bookingService.getBookingsforFloorBetweenDates(bookingModel));

		assertEquals("Failed to get Bookings between selected Booking range !", exception.getMessage());
	}

	@Test
	void testGetAllUserBookings_Success() {
		int userId = 1;

		List<Booking> expectedBookings = new ArrayList<>();

		when(bookingDAO.getAllUserBookings(userId)).thenReturn(expectedBookings);

		List<Booking> result = bookingService.getAllUserBookings(userId);

		assertEquals(expectedBookings, result);
	}

	@Test
	void testGetAllUserBookings_Failure() {
		int userId = 1;

		when(bookingDAO.getAllUserBookings(userId)).thenThrow(new RuntimeException());

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class, () -> bookingService.getAllUserBookings(userId));

		assertEquals("Error getting all the Bookings !", exception.getMessage());
	}

	@Test
	void testGetBookingById_Success() {
		int bookingId = 1;

		Booking expectedBooking = new Booking();
		when(bookingDAO.getBookingById(bookingId)).thenReturn(expectedBooking);

		Booking result = bookingService.getBookingById(bookingId);

		assertEquals(expectedBooking, result);
	}

	@Test
	void testGetBookingById_Failure() {
		int bookingId = 1;

		when(bookingDAO.getBookingById(bookingId)).thenThrow(new RuntimeException());

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class, () -> bookingService.getBookingById(bookingId));

		assertEquals("Failed to get User's !", exception.getMessage());
	}

	@Test
	void testGetAttendanceCountForCurrentMonth() {
		int userId = 123;
		int expectedCount = 5;
		when(bookingDAO.getAttendanceCountForCurrentMonth(userId)).thenReturn(expectedCount);
		int result = bookingService.getAttendanceCountForCurrentMonth(userId);
		assertEquals(expectedCount, result);
	}

	@Test
	void testGetUserDashboard() {
		int userId = 123;
		List<UserDashboardModel> expectedUserDashboard = new ArrayList<>();
		UserDashboardModel userDashboard1 = new UserDashboardModel();
		UserDashboardModel userDashboard2 = new UserDashboardModel();

		expectedUserDashboard.add(userDashboard1);
		expectedUserDashboard.add(userDashboard2);

		when(bookingDAO.getUserDashboard(userId)).thenReturn(expectedUserDashboard);

		List<UserDashboardModel> result = bookingService.getUserDashboard(userId);

		assertEquals(expectedUserDashboard.size(), result.size());
	}

	@Test
	void testGetBookingById_DatabaseFailure() {
		int bookingId = 123;
		when(bookingDAO.getBookingById(bookingId)).thenThrow(new RuntimeException("Failed to get User's Booking!"));

		assertThrows(DataBaseAccessException.class, () -> bookingService.getBookingById(bookingId));
	}

	@Test
	void testGetUserDashboard_DatabaseFailure() {
		int userId = 456;
		when(bookingDAO.getUserDashboard(userId)).thenThrow(new RuntimeException("Failed to get info for User dashboard !"));

		assertThrows(DataBaseAccessException.class, () -> bookingService.getUserDashboard(userId));
	}

	@Test
	void testGetAttendanceCountForCurrentMonth_DatabaseFailure() {
		int userId = 123;
		when(bookingDAO.getAttendanceCountForCurrentMonth(anyInt())).thenThrow(new RuntimeException("Failed to get Attendance Count !"));

		assertThrows(DataBaseAccessException.class, () -> bookingService.getAttendanceCountForCurrentMonth(userId));
	}
}