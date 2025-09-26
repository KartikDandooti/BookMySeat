package com.valtech.team3.bookmyseatbackend.dao.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.valtech.team3.bookmyseatbackend.dao.FloorDAO;
import com.valtech.team3.bookmyseatbackend.dao.SeatDAO;
import com.valtech.team3.bookmyseatbackend.dao.ShiftDetailsDAO;
import com.valtech.team3.bookmyseatbackend.dao.UserDAO;
import com.valtech.team3.bookmyseatbackend.dao.impl.BookingDAOImpl;
import com.valtech.team3.bookmyseatbackend.dao.impl.BookingDAOImpl.BookingsRowMapper;
import com.valtech.team3.bookmyseatbackend.dao.impl.BookingDAOImpl.UserPreferredSeatsRowMapper;
import com.valtech.team3.bookmyseatbackend.entities.Booking;
import com.valtech.team3.bookmyseatbackend.entities.BookingRange;
import com.valtech.team3.bookmyseatbackend.entities.BookingStatus;
import com.valtech.team3.bookmyseatbackend.entities.Floor;
import com.valtech.team3.bookmyseatbackend.entities.RestrictedSeat;
import com.valtech.team3.bookmyseatbackend.entities.Seat;
import com.valtech.team3.bookmyseatbackend.entities.User;
import com.valtech.team3.bookmyseatbackend.models.BookingModel;
import com.valtech.team3.bookmyseatbackend.models.UserDashboardModel;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class BookingDAOImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;
	@Mock
	private ResultSet resultSet;
	@Mock
	private UserDAO userDAO;
	@Mock
	private SeatDAO seatDAO;
	@Mock
	private FloorDAO floorDAO;
	@Mock
	private ShiftDetailsDAO shiftDetailsDAO;
	@InjectMocks
	private BookingDAOImpl bookingDAO;

	@Test
	void testCreateBooking() {
		BookingModel bookingModel = new BookingModel(BookingRange.DAILY, LocalDate.now(), LocalDate.now().plusDays(1),
				BookingStatus.ACTIVE, "verificationCode");
		User user = new User();
		user.setId(1);
		Seat seat = new Seat(2, 10, true, new Floor(), new RestrictedSeat());

		when(jdbcTemplate.update(anyString(), eq(bookingModel.getBookingRange()), eq(bookingModel.getStartDate()), eq(bookingModel.getEndDate()), eq(bookingModel.getCreatedDate()), eq(user.getId()),
		      eq(seat.getId()), eq(bookingModel.isBookingStatus()), eq(bookingModel.getVerificationCode()))).thenReturn(1);
		when(jdbcTemplate.queryForObject(anyString(), any(Class.class))).thenReturn(10);

		int bookingId = bookingDAO.createBooking(bookingModel, user, seat);

		assertEquals(10, bookingId);
		verify(jdbcTemplate).update(anyString(), eq(bookingModel.getBookingRange()), eq(bookingModel.getStartDate()), eq(bookingModel.getEndDate()), eq(bookingModel.getCreatedDate()), eq(user.getId()),
		      eq(seat.getId()), eq(bookingModel.isBookingStatus()), eq(bookingModel.getVerificationCode()));
		verify(jdbcTemplate).queryForObject(anyString(), any(Class.class));
	}

	@Test
	void testHasAlreadyBookedForDate() {
		int userId = 1;
		LocalDate startDate = LocalDate.now().minusDays(2);
		LocalDate endDate = LocalDate.now().plusDays(2);
		int count = 1;

		when(jdbcTemplate.queryForObject(any(String.class), eq(Integer.class), any(Integer.class), any(LocalDate.class), any(LocalDate.class), any(LocalDate.class), any(LocalDate.class)))
		      .thenReturn(count);

		boolean hasBooked = bookingDAO.hasAlreadyBookedForDate(userId, startDate, endDate);

		assertTrue(hasBooked);
	}

	@Test
	void testGetAllBookings() {
		int userId = 1;
		Booking booking = new Booking();

		when(jdbcTemplate.query(anyString(), any(RowMapper.class), any())).thenReturn(List.of(booking));

		List<Booking> allBookings = bookingDAO.getAllBookings(userId);

		assertFalse(allBookings.isEmpty());
		assertEquals(booking, allBookings.get(0));
	}

	@Test
	void testGetBookingById() {
		int bookingId = 1;
		Booking booking = new Booking();

		when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), any())).thenReturn(booking);

		Booking retrievedBooking = bookingDAO.getBookingById(bookingId);

		assertNotNull(retrievedBooking);
		assertEquals(booking, retrievedBooking);
	}

	@Test
	void testCancelBooking() {
		int bookingId = 1;

		bookingDAO.cancelBooking(bookingId);

		verify(jdbcTemplate).update(anyString(), eq(bookingId));
	}

	@Test
	void testUserPreferredSeats() {
		int floorId = 1;
		int projectId = 1;
		LocalDate startDate = LocalDate.now();
		Booking booking = new Booking();

		when(jdbcTemplate.query(anyString(), any(RowMapper.class), any(), any(), any())).thenReturn(List.of(booking));

		List<Booking> preferredSeats = bookingDAO.userPreferredSeats(floorId, projectId, startDate);

		assertFalse(preferredSeats.isEmpty());
		assertEquals(booking, preferredSeats.get(0));
	}

	@Test
	void testUpdateBookingByAdmin() {
		int bookingId = 1;
		int seatId = 2;
		LocalDateTime modifiedDate = LocalDateTime.now();
		BookingModel bookingModel = new BookingModel();
		bookingModel.setModifiedDate(modifiedDate);

		bookingDAO.updateBookingByAdmin(bookingModel, bookingId, seatId);

		verify(jdbcTemplate).update(anyString(), eq(seatId), eq(modifiedDate), eq(bookingId));
	}

	@Test
	void testFindBookingsByFloorAndDates() {
		int floorId = 3;
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = LocalDate.now().plusDays(10);
		List<Booking> expectedBookings = Arrays.asList(new Booking(), new Booking());

		when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(floorId), eq(endDate), eq(startDate), eq(startDate), eq(endDate), eq(startDate), eq(endDate))).thenReturn(expectedBookings);

		List<Booking> bookings = bookingDAO.findBookingsByFloorAndDates(floorId, startDate, endDate);

		assertEquals(expectedBookings, bookings);
		verify(jdbcTemplate).query(anyString(), any(RowMapper.class), eq(floorId), eq(endDate), eq(startDate), eq(startDate), eq(endDate), eq(startDate), eq(endDate));
	}

	@Test
	void testGetAttendanceCountForCurrentMonth() {
		int userId = 1;
		int expectedCount = 3;

		when(jdbcTemplate.queryForObject(anyString(), any(Class.class), eq(userId))).thenReturn(expectedCount);

		int attendanceCount = bookingDAO.getAttendanceCountForCurrentMonth(userId);

		assertEquals(expectedCount, attendanceCount);
		verify(jdbcTemplate).queryForObject(anyString(), any(Class.class), eq(userId));
	}

	@Test
	void testGetUserDashboard() {
		int userId = 1;
		List<UserDashboardModel> expectedModels = Arrays.asList(new UserDashboardModel(), new UserDashboardModel());

		when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(userId))).thenReturn(expectedModels);

		List<UserDashboardModel> dashboard = bookingDAO.getUserDashboard(userId);

		assertEquals(expectedModels, dashboard);
		verify(jdbcTemplate).query(anyString(), any(RowMapper.class), eq(userId));
	}

	@Test
	void testGetBookingByMappingId() {
		int bookingMappingId = 1;
		Booking expectedBooking = new Booking();

		when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(bookingMappingId))).thenReturn(expectedBooking);

		Booking booking = bookingDAO.getBookingByMappingId(bookingMappingId);

		assertEquals(expectedBooking, booking);
		verify(jdbcTemplate).queryForObject(anyString(), any(RowMapper.class), eq(bookingMappingId));
	}

	@Test
	void testGetAllUserBookings() {
		int userId = 1;
		List<Booking> expectedBookings = Arrays.asList(new Booking(), new Booking());

		when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(userId))).thenReturn(expectedBookings);

		List<Booking> bookings = bookingDAO.getAllUserBookings(userId);

		assertEquals(expectedBookings, bookings);
		verify(jdbcTemplate).query(anyString(), any(RowMapper.class), eq(userId));
	}

	@Test
	 void testUserPreferredSeatsRowMapper() throws SQLException {
	    when(resultSet.getInt("BOOKING_ID")).thenReturn(1);
	    when(resultSet.getDate("START_DATE")).thenReturn(Date.valueOf(LocalDate.now()));
	    when(resultSet.getDate("END_DATE")).thenReturn(Date.valueOf(LocalDate.now().plusDays(1)));
	    when(resultSet.getString("BOOKING_RANGE")).thenReturn("DAILY");
	    when(resultSet.getInt("USER_ID")).thenReturn(2);
	    when(resultSet.getString("FIRST_NAME")).thenReturn("John");
	    when(resultSet.getInt("SEAT_ID")).thenReturn(3);
	    when(resultSet.getInt("SEAT_NUMBER")).thenReturn(10);
	    when(resultSet.getInt("FLOOR_ID")).thenReturn(5);

	    UserPreferredSeatsRowMapper rowMapper = bookingDAO.new UserPreferredSeatsRowMapper();
	    Booking booking = rowMapper.mapRow(resultSet, 1);

	    assertEquals(1, booking.getId());
	    assertEquals(LocalDate.now(), booking.getStartDate());
	    assertEquals(LocalDate.now().plusDays(1), booking.getEndDate());
	    assertEquals(BookingRange.DAILY, booking.getBookingRange());
	    assertEquals(2, booking.getUser().getId());
	    assertEquals("John", booking.getUser().getFirstName());
	    assertEquals(3, booking.getSeat().getId());
	    assertEquals(10, booking.getSeat().getSeatNumber());
	    assertEquals(5, booking.getSeat().getFloor().getId());
	}

	@Test
	void testBookingsRowMapper() throws SQLException {
		int bookingId = 1;
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = startDate.plusDays(1);
		BookingRange bookingRange = BookingRange.DAILY;
		int verificationCode = 1234;
		int userId = 2;
		int seatId = 3;
		int seatNumber = 10;

		when(resultSet.getInt("id")).thenReturn(bookingId);
		when(resultSet.getDate("start_date")).thenReturn(Date.valueOf(LocalDate.now()));
		when(resultSet.getDate("end_date")).thenReturn(Date.valueOf(LocalDate.now().plusDays(1)));
		when(resultSet.getString("booking_range")).thenReturn(bookingRange.name());
		when(resultSet.getBoolean("booking_status")).thenReturn(true);
		when(resultSet.getInt("verification_code")).thenReturn(verificationCode);
		when(resultSet.getInt("user_id")).thenReturn(userId);
		when(resultSet.getInt("seat_id")).thenReturn(seatId);

		User expectedUser = new User();
		expectedUser.setId(userId);
		when(userDAO.getUserById(userId)).thenReturn(expectedUser);

		Seat expectedSeat = new Seat(seatId, seatNumber, true);
		when(seatDAO.getSeatById(seatId)).thenReturn(expectedSeat);

		BookingsRowMapper rowMapper = bookingDAO.new BookingsRowMapper();
		Booking actualBooking = rowMapper.mapRow(resultSet, 1);

		assertEquals(startDate, actualBooking.getStartDate());
		assertEquals(endDate, actualBooking.getEndDate());
		assertEquals(bookingRange, actualBooking.getBookingRange());
		assertEquals(true, actualBooking.isBookingStatus());
		assertEquals(verificationCode, actualBooking.getVerificationCode());
		assertEquals(expectedUser, actualBooking.getUser());
		assertEquals(expectedSeat, actualBooking.getSeat());

		verify(resultSet).getInt("id");
		verify(resultSet).getDate("start_date");
		verify(resultSet).getDate("end_date");
		verify(resultSet).getString("booking_range");
		verify(resultSet).getBoolean("booking_status");
		verify(resultSet).getInt("verification_code");
		verify(resultSet).getInt("user_id");
		verify(resultSet).getInt("seat_id");
		verify(userDAO).getUserById(userId);
		verify(seatDAO).getSeatById(seatId);
	}
}