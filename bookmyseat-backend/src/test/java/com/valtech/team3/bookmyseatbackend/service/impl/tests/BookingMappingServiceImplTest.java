package com.valtech.team3.bookmyseatbackend.service.impl.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.valtech.team3.bookmyseatbackend.dao.ShiftDetailsDAO;
import com.valtech.team3.bookmyseatbackend.entities.Booking;
import com.valtech.team3.bookmyseatbackend.entities.BookingMapping;
import com.valtech.team3.bookmyseatbackend.entities.Holiday;
import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;
import com.valtech.team3.bookmyseatbackend.entities.VehicleType;
import com.valtech.team3.bookmyseatbackend.exceptions.BookingNotFoundException;
import com.valtech.team3.bookmyseatbackend.exceptions.NoAttendanceMarkedBookingsFoundException;
import com.valtech.team3.bookmyseatbackend.models.AdminDashboardModel;
import com.valtech.team3.bookmyseatbackend.models.BookingMappingModel;
import com.valtech.team3.bookmyseatbackend.models.BookingModel;
import com.valtech.team3.bookmyseatbackend.service.impl.BookingMappingServiceImpl;

@ExtendWith(MockitoExtension.class)
class BookingMappingServiceImplTest {

	@Mock
	private BookingMappingDAO bookingMappingDAO;
	@Mock
	private HolidayDAO holidayDAO;
	@Mock
	private ShiftDetailsDAO shiftDAO;
	@Mock
	private BookingDAO bookingDAO;
	@InjectMocks
	private BookingMappingServiceImpl bookingMappingService;

	@Test
	void testCreateBookingMapping() {
		BookingModel bookingModel = new BookingModel();
		bookingModel.setStartDate(LocalDate.of(2024, 2, 1));
		bookingModel.setEndDate(LocalDate.of(2024, 2, 5));
		bookingModel.setShiftId(1);

		ShiftDetails shiftDetails = new ShiftDetails();
		shiftDetails.setId(1);

		List<Holiday> holidays = new ArrayList<>();
		holidays.add(new Holiday(LocalDate.of(2024, 2, 3)));

		when(shiftDAO.getShiftDetailsById(anyInt())).thenReturn(shiftDetails);
		when(holidayDAO.getHolidayByDate()).thenReturn(holidays);

		bookingMappingService.createBookingMapping(bookingModel, 1);

		verify(bookingMappingDAO, times(4)).createBookingMapping(any(BookingModel.class), eq(1),
				any(ShiftDetails.class));
	}

	@Test
	void testIsHoliday_ReturnsTrue() {
		LocalDate date = LocalDate.of(2024, 2, 17);
		List<Holiday> holidays = new ArrayList<>();
		Holiday holiday = new Holiday();
		holiday.setHolidayDate(date);
		holidays.add(holiday);
		when(holidayDAO.getHolidayByDate()).thenReturn(holidays);

		boolean result = bookingMappingService.isHoliday(date);

		assertTrue(result, "Expected date to be a holiday");
	}

	@Test
	void testIsHoliday_ReturnsFalse() {
		LocalDate date = LocalDate.of(2024, 2, 17);
		List<Holiday> holidays = new ArrayList<>();
		when(holidayDAO.getHolidayByDate()).thenReturn(holidays);

		boolean result = bookingMappingService.isHoliday(date);

		assertFalse(result, "Expected date not to be a holiday");
	}

	@Test
	void testEditBookingAndSubBookings_SuccessfulUpdate() {
		BookingMappingModel updatedSubBooking = new BookingMappingModel();
		updatedSubBooking.setShiftId(1);
		updatedSubBooking.setLunch(true);
		updatedSubBooking.setBeverage(true);
		updatedSubBooking.setAdditionalDesktop(false);
		updatedSubBooking.setParking(true);
		updatedSubBooking.setVehicleType("WHEELER_2");

		BookingMapping bookingMapping = new BookingMapping();
		bookingMapping.setShiftDetails(new ShiftDetails());
		bookingMapping.setLunch(false);
		bookingMapping.setBeverage(false);
		bookingMapping.setAdditionalDesktop(false);
		bookingMapping.setParking(false);

		Booking booking = new Booking();
		booking.setStartDate(LocalDate.now().plusDays(1));
		booking.setEndDate(LocalDate.now().plusDays(3));

		int bookingMappingId = 1;
		when(bookingMappingDAO.getBookingMappingById(bookingMappingId)).thenReturn(bookingMapping);
		when(bookingDAO.getBookingByMappingId(bookingMappingId)).thenReturn(booking);
		when(shiftDAO.getShiftDetailsById(updatedSubBooking.getShiftId())).thenReturn(new ShiftDetails());
		when(bookingMappingDAO.getVehicleTypeCount(eq("WHEELER_2"), any(LocalDate.class), any(LocalDate.class)))
				.thenReturn(30);
		when(bookingMappingDAO.getVehicleTypeCount(eq("WHEELER_4"), any(LocalDate.class), any(LocalDate.class)))
				.thenReturn(5);

		bookingMappingService.editBookingAndSubBookings(updatedSubBooking, bookingMappingId);

		assertEquals(true, bookingMapping.isLunch());
		assertEquals(true, bookingMapping.isBeverage());
		assertEquals(false, bookingMapping.isAdditionalDesktop());
		assertEquals(true, bookingMapping.isParking());
		assertEquals(true, bookingMapping.getModifiedDate().isAfter(LocalDateTime.now().minusSeconds(1)));
	}

	@Test
	void testGetSubBookingsByBookingId() {
		int bookingId = 1;
		List<BookingMapping> expectedBookingMappings = new ArrayList<>();
		BookingMapping bookingMapping1 = new BookingMapping();
		bookingMapping1.setId(1);
		BookingMapping bookingMapping2 = new BookingMapping();
		bookingMapping2.setId(2);
		expectedBookingMappings.add(bookingMapping1);
		expectedBookingMappings.add(bookingMapping2);

		when(bookingMappingDAO.getBookingMappingsByBookingId(bookingId)).thenReturn(expectedBookingMappings);

		List<BookingMapping> actualBookingMappings = bookingMappingService.getSubBookingsByBookingId(bookingId);

		assertEquals(expectedBookingMappings, actualBookingMappings);
	}

	@Test
	void testGetAllBookingforCurrentDate_Success() {
		List<BookingMapping> bookingIds = new ArrayList<>();
		bookingIds.add(new BookingMapping(1, new Booking(), LocalDate.now(), false));
		bookingIds.add(new BookingMapping(2, new Booking(), LocalDate.now(), false));

		when(bookingMappingDAO.getAllBookingsForCurrentDate()).thenReturn(bookingIds);

		assertEquals(bookingIds, bookingMappingService.getAllBookingsForCurrentDate());
	}

	@Test
	void testUpdateUserAttendence_Success() {
		doNothing().when(bookingMappingDAO).updateAttendence(anyInt());

		assertDoesNotThrow(() -> bookingMappingService.updateUserAttendence(123));
	}

	@Test
	void testIsBookingAvailableForCurrentDate_True() {
		when(bookingMappingDAO.isBookingAvailableForCurrentDate(anyInt())).thenReturn(true);

		assertTrue(bookingMappingService.isBookingAvailableForCurrentDate(123));
	}

	@Test
	void testIsBookingAvailableForCurrentDate_False() {
		when(bookingMappingDAO.isBookingAvailableForCurrentDate(anyInt())).thenReturn(false);

		assertFalse(bookingMappingService.isBookingAvailableForCurrentDate(123));
	}

	@Test
	void testIsAttendanceMarkedForCurrentDate_True() {
		when(bookingMappingDAO.isAttendanceMarkedForCurrentDate(anyInt())).thenReturn(true);

		assertTrue(bookingMappingService.isAttendanceMarkedForCurrentDate(123));
	}

	@Test
	void testIsAttendanceMarkedForCurrentDate_False() {
		when(bookingMappingDAO.isAttendanceMarkedForCurrentDate(anyInt())).thenReturn(false);

		assertFalse(bookingMappingService.isAttendanceMarkedForCurrentDate(123));
	}

	@Test
	void testGetAllBookingforCurrentDate() {
		List<BookingMapping> expectedBookings = new ArrayList<>();
		expectedBookings.add(new BookingMapping());
		expectedBookings.add(new BookingMapping());

		LocalDate currentDate = LocalDate.now();
		BookingMapping newBookingMapping1 = new BookingMapping();
		newBookingMapping1.setAttendance(false);
		newBookingMapping1.setBookedDate(currentDate);
		newBookingMapping1.setBooking(new Booking());
		newBookingMapping1.setAdditionalDesktop(true);
		newBookingMapping1.setBeverage(true);
		newBookingMapping1.setLunch(true);
		newBookingMapping1.setParking(true);
		newBookingMapping1.setShiftDetails(new ShiftDetails());
		newBookingMapping1.setVehicleType(null);
		newBookingMapping1.setModifiedDate(null);
		expectedBookings.add(newBookingMapping1);

		BookingMapping newBookingMapping2 = new BookingMapping();
		newBookingMapping2.setAttendance(false);
		newBookingMapping2.setBookedDate(currentDate);
		newBookingMapping2.setBooking(new Booking());
		newBookingMapping2.setAdditionalDesktop(true);
		newBookingMapping2.setBeverage(true);
		newBookingMapping2.setLunch(true);
		newBookingMapping2.setParking(true);
		newBookingMapping2.setShiftDetails(new ShiftDetails());
		newBookingMapping2.setVehicleType(null);
		newBookingMapping2.setModifiedDate(null);
		expectedBookings.add(newBookingMapping2);

		when(bookingMappingDAO.getAllBookingsForCurrentDate()).thenReturn(expectedBookings);

		List<BookingMapping> actualBookings = bookingMappingService.getAllBookingsForCurrentDate();

		assertEquals(expectedBookings, actualBookings);
	}

	@Test
	void testGetAllBookingForCurrentDate_DataAccessException() {
		when(bookingMappingDAO.getAllBookingsForCurrentDate())
				.thenThrow(new DataAccessException("Error accessing data") { 

					private static final long serialVersionUID = 1L;
				});

		BookingNotFoundException exception = assertThrows(BookingNotFoundException.class,
				() -> bookingMappingService.getAllBookingsForCurrentDate());

		assertEquals("No bookings found for current date!", exception.getMessage());
	}

	@Test
	void testGetAllBookingsAttendanceMarkedForCurrentDate_AttendanceMarked() {
		List<BookingMapping> expectedBookings = new ArrayList<>();
		expectedBookings.add(new BookingMapping());
		expectedBookings.add(new BookingMapping());

		LocalDate currentDate = LocalDate.now();
		BookingMapping newBookingMapping1 = new BookingMapping();
		newBookingMapping1.setAttendance(true);
		newBookingMapping1.setBookedDate(currentDate);
		newBookingMapping1.setBooking(new Booking());
		newBookingMapping1.setAdditionalDesktop(true);
		newBookingMapping1.setBeverage(true);
		newBookingMapping1.setLunch(true);
		newBookingMapping1.setParking(true);
		newBookingMapping1.setShiftDetails(new ShiftDetails());
		newBookingMapping1.setVehicleType(null);
		newBookingMapping1.setModifiedDate(null);
		expectedBookings.add(newBookingMapping1);

		BookingMapping newBookingMapping2 = new BookingMapping();
		newBookingMapping2.setAttendance(true);
		newBookingMapping2.setBookedDate(currentDate);
		newBookingMapping2.setBooking(new Booking());
		newBookingMapping2.setAdditionalDesktop(true);
		newBookingMapping2.setBeverage(true);
		newBookingMapping2.setLunch(true);
		newBookingMapping2.setParking(true);
		newBookingMapping2.setShiftDetails(new ShiftDetails());
		newBookingMapping2.setVehicleType(null);
		newBookingMapping2.setModifiedDate(null);
		expectedBookings.add(newBookingMapping2);

		when(bookingMappingDAO.getAllBookingsAttendanceMarkedForCurrentDate()).thenReturn(expectedBookings);

		List<BookingMapping> actualBookings = bookingMappingService.getAllBookingsAttendanceMarkedForCurrentDate();

		assertEquals(expectedBookings, actualBookings);
	}

	@Test
   void testGetAllBookingsAttendanceMarkedForCurrentDate_NoAttendanceMarked() {
       when(bookingMappingDAO.getAllBookingsAttendanceMarkedForCurrentDate()).thenThrow(NoAttendanceMarkedBookingsFoundException.class);

       assertThrows(
               NoAttendanceMarkedBookingsFoundException.class,
               () -> bookingMappingService.getAllBookingsAttendanceMarkedForCurrentDate()
       );
   }

	@Test

	void editBookingAndSubBookings_VehicleTypeNull() {
		int bookingMappingId = 1;
		BookingMappingModel updatedSubBooking = new BookingMappingModel();
		updatedSubBooking.setShiftId(1);
		updatedSubBooking.setLunch(true);
		updatedSubBooking.setBeverage(true);
		updatedSubBooking.setAdditionalDesktop(true);
		updatedSubBooking.setParking(true);
		updatedSubBooking.setVehicleType("WHEELER_2");

		BookingMapping bookingMapping = new BookingMapping();
		Booking booking = new Booking();
		when(bookingMappingDAO.isAttendanceMarkedForBookingMapping(bookingMappingId)).thenReturn(false);
		when(bookingMappingDAO.getBookingMappingById(bookingMappingId)).thenReturn(bookingMapping);
		when(bookingDAO.getBookingByMappingId(bookingMappingId)).thenReturn(booking);
		when(shiftDAO.getShiftDetailsById(1)).thenReturn(new ShiftDetails());

		bookingMappingService.editBookingAndSubBookings(updatedSubBooking, bookingMappingId);

		verify(bookingMappingDAO).updateBooking(bookingMapping, bookingMappingId);
		assertNotNull(bookingMapping.getModifiedDate());
		assertEquals(updatedSubBooking.isParking(), bookingMapping.isParking());
		assertEquals(VehicleType.WHEELER_2, bookingMapping.getVehicleType());
	}

	@Test
	void testGetAdminDashboardModel() {
		AdminDashboardModel expectedDashboardModel = new AdminDashboardModel(/* specify data */);

		when(bookingMappingDAO.getAdminDashboardModel()).thenReturn(expectedDashboardModel);

		AdminDashboardModel actualDashboardModel = bookingMappingService.getAdminDashboardModel();

		assertEquals(expectedDashboardModel, actualDashboardModel);
		verify(bookingMappingDAO).getAdminDashboardModel();
	}
}