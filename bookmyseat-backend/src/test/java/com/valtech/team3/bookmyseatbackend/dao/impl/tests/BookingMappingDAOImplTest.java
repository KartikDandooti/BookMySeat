package com.valtech.team3.bookmyseatbackend.dao.impl.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.valtech.team3.bookmyseatbackend.dao.impl.BookingMappingDAOImpl;
import com.valtech.team3.bookmyseatbackend.dao.impl.BookingMappingDAOImpl.AdminDashboardModelRowMapper;
import com.valtech.team3.bookmyseatbackend.dao.impl.BookingMappingDAOImpl.BookingMappingRowMapper;
import com.valtech.team3.bookmyseatbackend.dao.impl.BookingMappingDAOImpl.NewBookingMappingRowMapper;
import com.valtech.team3.bookmyseatbackend.entities.Booking;
import com.valtech.team3.bookmyseatbackend.entities.BookingMapping;
import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;
import com.valtech.team3.bookmyseatbackend.entities.VehicleType;
import com.valtech.team3.bookmyseatbackend.models.AdminDashboardModel;
import com.valtech.team3.bookmyseatbackend.models.BookingModel;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class BookingMappingDAOImplTest {

	@Mock
	private ResultSet resultSet;
	@Mock
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	private BookingMappingDAOImpl bookingMappingDAO;

	@Test
	void testCreateBookingMapping() {
		BookingModel bookingModel = new BookingModel();
		int bookingId = 1;
		ShiftDetails shiftDetails = new ShiftDetails();

		assertDoesNotThrow(() -> bookingMappingDAO.createBookingMapping(bookingModel, bookingId, shiftDetails));

		verify(jdbcTemplate).update(anyString(), eq(null), eq(false), eq(1), eq(false), eq(false), eq(false), eq(false), eq(0), eq((Object) null));
	}

	@Test
	void testGetAllBookingforCurrentDate() {
		List<BookingMapping> expectedBookingMappings = new ArrayList<>();
		expectedBookingMappings.add(new BookingMapping());
		when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedBookingMappings);

		List<BookingMapping> bookingMappings = bookingMappingDAO.getAllBookingsForCurrentDate();

		assertNotNull(bookingMappings);
		assertEquals(expectedBookingMappings, bookingMappings);

		verify(jdbcTemplate).query(anyString(), any(RowMapper.class));
	}

	@Test
	void testUpdateBooking() {
		BookingMapping bookingMapping = new BookingMapping();
		bookingMapping.setVehicleType(VehicleType.WHEELER_4);
		bookingMapping.setShiftDetails(new ShiftDetails());
		int bookingId = 1;

		assertDoesNotThrow(() -> bookingMappingDAO.updateBooking(bookingMapping, bookingId));

		verify(jdbcTemplate).update(anyString(), any(), any(), any(), any(), any(), any(), any(), any());
	}

	@Test
	void testIsBookingAvailableForCurrentDate() {
		int employeeId = 123;
		assertFalse(bookingMappingDAO.isBookingAvailableForCurrentDate(employeeId));

		verify(jdbcTemplate).queryForObject(anyString(), eq(Integer.class), any());
	}

	@Test
	void testIsAttendanceMarked() {
		int employeeId = 123;
		assertFalse(bookingMappingDAO.isAttendanceMarkedForCurrentDate(employeeId));

		verify(jdbcTemplate).queryForObject(anyString(), eq(Integer.class), any());
	}

	@Test
	void testGetBookingMappingById() {
		int bookingMappingId = 1;
		LocalDate bookedDate = LocalDate.now();
		boolean attendance = false;

		BookingMapping expectedMapping = new BookingMapping(bookingMappingId, null, bookedDate, attendance);

		when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq(bookingMappingId))).thenReturn(expectedMapping);

		BookingMapping actualMapping = bookingMappingDAO.getBookingMappingById(bookingMappingId);

		assertEquals(expectedMapping, actualMapping);
		verify(jdbcTemplate).queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq(bookingMappingId));
	}

	@Test
	void testUpdateAttendence() {
		int employeeId = 123;

		when(jdbcTemplate.update(anyString(), eq(employeeId))).thenReturn(1);

		bookingMappingDAO.updateAttendence(employeeId);

		verify(jdbcTemplate).update(anyString(), eq(employeeId));
	}

	@Test
	void testGetAllBookingsAttendanceMarkedForCurrentDate() {
		List<BookingMapping> expectedList = new ArrayList<>();

		when(jdbcTemplate.query(anyString(), eq(new BookingMappingRowMapper()))).thenReturn(expectedList);

		List<BookingMapping> actualList = bookingMappingDAO.getAllBookingsAttendanceMarkedForCurrentDate();

		assertEquals(expectedList, actualList);
		verify(jdbcTemplate).query(anyString(), eq(new BookingMappingRowMapper()));
	}

	@Test
	void testGetAdminDashboardModel() {
		int totalAttendees = 10;
		int totalSeatsBooked = 20;
		int totalParkingSpaceBooked = 5;
		int fourWheelerParkingSpaceBooked = 2;
		int twoWheelerParkingSpaceBooked = 3;
		int totalEmployeesOptedForLunch = 15;
		int totalEmployeesOptedForDesktop = 8;

		when(jdbcTemplate.queryForObject(anyString(), any(AdminDashboardModelRowMapper.class), any(LocalDate.class))).thenReturn(new AdminDashboardModel(totalAttendees, totalSeatsBooked,
		      totalParkingSpaceBooked, fourWheelerParkingSpaceBooked, twoWheelerParkingSpaceBooked, totalEmployeesOptedForLunch, totalEmployeesOptedForDesktop));

		AdminDashboardModel result = bookingMappingDAO.getAdminDashboardModel();

		verify(jdbcTemplate).queryForObject(anyString(), any(AdminDashboardModelRowMapper.class), eq(LocalDate.now()));

		assertNotNull(result);
		assertEquals(totalAttendees, result.getTotalAttendees());
		assertEquals(totalSeatsBooked, result.getTotalSeatsBooked());
		assertEquals(totalParkingSpaceBooked, result.getTotalParkingSpaceBooked());
		assertEquals(fourWheelerParkingSpaceBooked, result.getFourWheelerParkingSpaceBooked());
		assertEquals(twoWheelerParkingSpaceBooked, result.getTwoWheelerParkingSpaceBooked());
		assertEquals(totalEmployeesOptedForLunch, result.getTotalEmployeesOptedForLunch());
		assertEquals(totalEmployeesOptedForDesktop, result.getTotalEmployeesOptedForDesktop());
	}

	@Test
	void testIsAttendanceMarkedForBooking() {
		int bookingId = 1;
		boolean expectedResult = true;

		when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(bookingId))).thenReturn(1);

		boolean actualResult = bookingMappingDAO.isAttendanceMarkedForBooking(bookingId);

		assertEquals(expectedResult, actualResult);
		verify(jdbcTemplate).queryForObject(anyString(), eq(Integer.class), eq(bookingId));
	}

	@Test
	 void testIsAttendanceMarkedForBookingMapping_AttendanceMarked()  {
	   when(jdbcTemplate.queryForObject(anyString(), eq(Boolean.class), anyInt())).thenReturn(true);

	    boolean isAttendanceMarked = bookingMappingDAO.isAttendanceMarkedForBookingMapping(1);

	    assertTrue(isAttendanceMarked);
	    verify(jdbcTemplate).queryForObject(anyString(), eq(Boolean.class), eq(1));
	}

	@Test
	 void testGetVehicleTypeCount() {
	    when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyString(), any(LocalDate.class), any(LocalDate.class)))
	            .thenReturn(3); // Adjust the expected count

	    int count = bookingMappingDAO.getVehicleTypeCount("CAR", LocalDate.of(2024, 2, 20), LocalDate.of(2024, 2, 21));

	    assertEquals(3, count);
	    verify(jdbcTemplate).queryForObject(anyString(), eq(Integer.class), eq("CAR"), eq(LocalDate.of(2024, 2, 20)), eq(LocalDate.of(2024, 2, 21)));
	}

	@Test
	void testGetBookingMappingsByBookingId() {
		int bookingId = 1;
		List<BookingMapping> expectedList = Arrays.asList(new BookingMapping(1, LocalDate.now(), true, false, false, false, null, false, null, new Booking(), new ShiftDetails()));

		when(jdbcTemplate.query(anyString(), any(NewBookingMappingRowMapper.class), eq(bookingId))).thenReturn(expectedList);

		List<BookingMapping> actualList = bookingMappingDAO.getBookingMappingsByBookingId(bookingId);

		assertNotNull(actualList);
		assertEquals(expectedList.size(), actualList.size());
		assertEquals(expectedList, actualList);

		verify(jdbcTemplate).query(anyString(), any(NewBookingMappingRowMapper.class), eq(bookingId));
	}

	@Test
	void testBookingMappingRowMapper() throws SQLException {
		int employeeId = 123;
		String firstName = "John";
		String lastName = "Doe";
		int seatId = 456;
		int seatNumber = 10;
		String floorName = "First Floor";
		int verificationCode = 7890;

		when(resultSet.getInt("EMPLOYEE_ID")).thenReturn(employeeId);
		when(resultSet.getString("FIRST_NAME")).thenReturn(firstName);
		when(resultSet.getString("LAST_NAME")).thenReturn(lastName);
		when(resultSet.getInt("SEAT_ID")).thenReturn(seatId);
		when(resultSet.getInt("SEAT_NUMBER")).thenReturn(seatNumber);
		when(resultSet.getString("FLOOR_NAME")).thenReturn(floorName);
		when(resultSet.getInt("VERIFICATION_CODE")).thenReturn(verificationCode);

		BookingMappingRowMapper rowMapper = new BookingMappingRowMapper();
		BookingMapping bookingMapping = rowMapper.mapRow(resultSet, 1);

		assertNotNull(bookingMapping);
		assertEquals(employeeId, bookingMapping.getBooking().getUser().getEmployeeId());
		assertEquals(firstName, bookingMapping.getBooking().getUser().getFirstName());
		assertEquals(lastName, bookingMapping.getBooking().getUser().getLastName());
		assertEquals(seatId, bookingMapping.getBooking().getSeat().getId());
		assertEquals(seatNumber, bookingMapping.getBooking().getSeat().getSeatNumber());
		assertEquals(floorName, bookingMapping.getBooking().getSeat().getFloor().getFloorName());
		assertEquals(verificationCode, bookingMapping.getBooking().getVerificationCode());
	}

	@Test
	void testNewBookingMappingRowMapper() throws SQLException {
		int bookingMappingId = 1;
		LocalDate bookedDate = LocalDate.now();
		boolean additionalDesktop = true;
		boolean attendance = false;
		boolean beverage = false;
		boolean lunch = true;
		boolean parking = true;
		VehicleType vehicleType = VehicleType.WHEELER_2;
		int bookingId = 2;
		LocalDate startDate = LocalDate.of(2024, 2, 20);
		LocalDate endDate = LocalDate.of(2024, 2, 21);
		int shiftId = 3;
		String shiftName = "Morning";
		LocalTime startTime = LocalTime.parse("09:00:00");
		LocalTime endTime = LocalTime.parse("13:00:00");

		when(resultSet.getInt("id")).thenReturn(bookingMappingId);
		when(resultSet.getDate("booked_date")).thenReturn(Date.valueOf(bookedDate));
		when(resultSet.getBoolean("additional_desktop")).thenReturn(additionalDesktop);
		when(resultSet.getBoolean("attendance")).thenReturn(attendance);
		when(resultSet.getBoolean("beverage")).thenReturn(beverage);
		when(resultSet.getBoolean("lunch")).thenReturn(lunch);
		when(resultSet.getBoolean("parking")).thenReturn(parking);
		when(resultSet.getString("vehicle_type")).thenReturn(vehicleType.name());
		when(resultSet.getInt("booking_id")).thenReturn(bookingId);
		when(resultSet.getDate("start_date")).thenReturn(Date.valueOf(startDate));
		when(resultSet.getDate("end_date")).thenReturn(Date.valueOf(endDate));
		when(resultSet.getInt("shift_id")).thenReturn(shiftId);
		when(resultSet.getString("shift_name")).thenReturn(shiftName);
		when(resultSet.getTime("start_time")).thenReturn(Time.valueOf(startTime));
		when(resultSet.getTime("end_time")).thenReturn(Time.valueOf(endTime));

		NewBookingMappingRowMapper rowMapper = bookingMappingDAO.new NewBookingMappingRowMapper();
		BookingMapping bookingMapping = rowMapper.mapRow(resultSet, 1);

		assertNotNull(bookingMapping);
		assertEquals(bookingMappingId, bookingMapping.getId());
		assertEquals(bookedDate, bookingMapping.getBookedDate());
		assertEquals(additionalDesktop, bookingMapping.isAdditionalDesktop());
		assertEquals(attendance, bookingMapping.isAttendance());
		assertEquals(beverage, bookingMapping.isBeverage());
		assertEquals(lunch, bookingMapping.isLunch());
		assertEquals(parking, bookingMapping.isParking());
		assertEquals(vehicleType, bookingMapping.getVehicleType());
		assertEquals(bookingId, bookingMapping.getBooking().getId());
		assertEquals(startDate, bookingMapping.getBooking().getStartDate());
		assertEquals(endDate, bookingMapping.getBooking().getEndDate());
		assertEquals(shiftId, bookingMapping.getShiftDetails().getId());
		assertEquals(shiftName, bookingMapping.getShiftDetails().getShiftName());
		assertEquals(startTime, bookingMapping.getShiftDetails().getStartTime());
		assertEquals(endTime, bookingMapping.getShiftDetails().getEndTime());
	}

	@Test
	void testAdminDashboardModelRowMapper() throws SQLException {
		int totalAttendees = 10;
		int totalSeatsBooked = 5;
		int totalParkingSpaceBooked = 3;
		int fourWheelerParkingSpaceBooked = 2;
		int twoWheelerParkingSpaceBooked = 1;
		int totalEmployeesOptedForLunch = 6;
		int totalEmployeesOptedForDesktop = 4;

		when(resultSet.getInt("totalAttendees")).thenReturn(totalAttendees);
		when(resultSet.getInt("totalSeatsBooked")).thenReturn(totalSeatsBooked);
		when(resultSet.getInt("totalParkingSpaceBooked")).thenReturn(totalParkingSpaceBooked);
		when(resultSet.getInt("fourWheelerParkingSpaceBooked")).thenReturn(fourWheelerParkingSpaceBooked);
		when(resultSet.getInt("twoWheelerParkingSpaceBooked")).thenReturn(twoWheelerParkingSpaceBooked);
		when(resultSet.getInt("totalEmployeesOptedForLunch")).thenReturn(totalEmployeesOptedForLunch);
		when(resultSet.getInt("totalEmployeesOptedForDesktop")).thenReturn(totalEmployeesOptedForDesktop);

		AdminDashboardModelRowMapper rowMapper = new AdminDashboardModelRowMapper();
		AdminDashboardModel counts = rowMapper.mapRow(resultSet, 1);

		assertNotNull(counts);
		assertEquals(totalAttendees, counts.getTotalAttendees());
		assertEquals(totalSeatsBooked, counts.getTotalSeatsBooked());
		assertEquals(totalParkingSpaceBooked, counts.getTotalParkingSpaceBooked());
		assertEquals(fourWheelerParkingSpaceBooked, counts.getFourWheelerParkingSpaceBooked());
		assertEquals(twoWheelerParkingSpaceBooked, counts.getTwoWheelerParkingSpaceBooked());
		assertEquals(totalEmployeesOptedForLunch, counts.getTotalEmployeesOptedForLunch());
		assertEquals(totalEmployeesOptedForDesktop, counts.getTotalEmployeesOptedForDesktop());
	}
}