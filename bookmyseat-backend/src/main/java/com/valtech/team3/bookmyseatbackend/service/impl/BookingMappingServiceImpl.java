package com.valtech.team3.bookmyseatbackend.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.team3.bookmyseatbackend.dao.BookingDAO;
import com.valtech.team3.bookmyseatbackend.dao.BookingMappingDAO;
import com.valtech.team3.bookmyseatbackend.dao.HolidayDAO;
import com.valtech.team3.bookmyseatbackend.dao.ShiftDetailsDAO;
import com.valtech.team3.bookmyseatbackend.entities.Booking;
import com.valtech.team3.bookmyseatbackend.entities.BookingMapping;
import com.valtech.team3.bookmyseatbackend.entities.Holiday;
import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;
import com.valtech.team3.bookmyseatbackend.entities.VehicleType;
import com.valtech.team3.bookmyseatbackend.exceptions.AttendanceMarkedException;
import com.valtech.team3.bookmyseatbackend.exceptions.BookingNotFoundException;
import com.valtech.team3.bookmyseatbackend.exceptions.NoAttendanceMarkedBookingsFoundException;
import com.valtech.team3.bookmyseatbackend.exceptions.NoParkingAvailableException;
import com.valtech.team3.bookmyseatbackend.models.AdminDashboardModel;
import com.valtech.team3.bookmyseatbackend.models.BookingMappingModel;
import com.valtech.team3.bookmyseatbackend.models.BookingModel;
import com.valtech.team3.bookmyseatbackend.service.BookingMappingService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BookingMappingServiceImpl implements BookingMappingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BookingMappingServiceImpl.class);

	@Autowired
	private BookingMappingDAO bookingMappingDAO;
	@Autowired
	private HolidayDAO holidayDAO;
	@Autowired
	private ShiftDetailsDAO shiftDAO;
	@Autowired
	private BookingDAO bookingDAO;

	@Override
	public void createBookingMapping(BookingModel booking, int bookingId) {
		LOGGER.info("Mapping Bookings based on booking date range");
		ShiftDetails shift = shiftDAO.getShiftDetailsById(booking.getShiftId());
		long numOfDays = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
		for (int i = 0; i <= numOfDays; i++) {
			LocalDate currentDate = booking.getStartDate().plusDays(i);
			boolean isHoliday = isHoliday(currentDate);
			if (!isHoliday) {
				booking.setBookedDate(currentDate);
				bookingMappingDAO.createBookingMapping(booking, bookingId, shift);
				LOGGER.debug("Mapping bookings for a user for the date: {} for days: {}", currentDate, numOfDays);
			}
		}
		LOGGER.info("Bookings mapped");
	}

	public Boolean isHoliday(LocalDate date) {
		List<Holiday> holidays = holidayDAO.getHolidayByDate();
		LOGGER.info("Checking holiday for the date");

		return holidays.stream().anyMatch(holiday -> holiday.getHolidayDate().equals(date));
	}

	@Override
	public List<BookingMapping> getSubBookingsByBookingId(int bookingId) {
		return bookingMappingDAO.getBookingMappingsByBookingId(bookingId);
	}

	@Override
	public void editBookingAndSubBookings(BookingMappingModel updatedSubBooking, int bookingMappingId) {
		LOGGER.info("Attempting to update booking mapping with ID {}", bookingMappingId);
		BookingMapping bookingMapping = bookingMappingDAO.getBookingMappingById(bookingMappingId);
		Booking booking = bookingDAO.getBookingByMappingId(bookingMappingId);
		boolean attendanceMarked = bookingMappingDAO.isAttendanceMarkedForBookingMapping(bookingMappingId);
		if (attendanceMarked) {
			throw new AttendanceMarkedException("Attendance is already marked for the specified booking ,you can't edit it.");
		} else {
			bookingMapping.setShiftDetails(shiftDAO.getShiftDetailsById(updatedSubBooking.getShiftId()));
			bookingMapping.setLunch(updatedSubBooking.isLunch());
			bookingMapping.setBeverage(updatedSubBooking.isBeverage());
			bookingMapping.setAdditionalDesktop(updatedSubBooking.isAdditionalDesktop());
			bookingMapping.setParking(updatedSubBooking.isParking());
			bookingMapping.setModifiedDate(LocalDateTime.now());
			if (updatedSubBooking.isParking()) {
				validateParkingAvailability(updatedSubBooking, booking);
				bookingMapping.setVehicleType(VehicleType.valueOf(updatedSubBooking.getVehicleType()));
			} else {
				bookingMapping.setVehicleType(null);
			}

			bookingMappingDAO.updateBooking(bookingMapping, bookingMappingId);
			LOGGER.info("Booking mapping with ID {} updated successfully", bookingMappingId);
		}
	}

	public void validateParkingAvailability(BookingMappingModel updatedSubBooking, Booking booking) {
		LocalDate startDate = booking.getStartDate();
		LocalDate endDate = booking.getEndDate();
		int twoWheelerCount = bookingMappingDAO.getVehicleTypeCount("WHEELER_2", startDate, endDate);
		int fourWheelerCount = bookingMappingDAO.getVehicleTypeCount("WHEELER_4", startDate, endDate);
		if ("WHEELER_2".equals(updatedSubBooking.getVehicleType()) && twoWheelerCount > 40) {
			throw new NoParkingAvailableException("Two-wheeler parking is full. Please use public transport.");
		} else if ("WHEELER_4".equals(updatedSubBooking.getVehicleType()) && fourWheelerCount > 10) {
			throw new NoParkingAvailableException("Four-wheeler parking is full. Please use public transport.");
		}
	}

	public void updateUserAttendence(int employeeId) {
		LOGGER.info("Updating Attendence of Employee");
		LOGGER.debug("Updating Attendence of Employee with Employee Id: {}", employeeId);
		bookingMappingDAO.updateAttendence(employeeId);
	}

	@Override
	public Boolean isBookingAvailableForCurrentDate(int employeeId) {
		LOGGER.info("Checking Booking of a Employee whether booked for current date");

		return bookingMappingDAO.isBookingAvailableForCurrentDate(employeeId);
	}

	@Override
	public Boolean isAttendanceMarkedForCurrentDate(int employeeId) {
		LOGGER.info("Checking Attendence marked for Employee");

		return bookingMappingDAO.isAttendanceMarkedForCurrentDate(employeeId);
	}

	@Override
	public List<BookingMapping> getAllBookingsForCurrentDate() {
		try {
			LOGGER.info("Getting All Bookings For Currentdate");

			return bookingMappingDAO.getAllBookingsForCurrentDate();
		} catch (RuntimeException e) {
			LOGGER.error("No bookings Found for Current date!");
			throw new BookingNotFoundException("No bookings found for current date!");
		}
	}

	@Override
	public List<BookingMapping> getAllBookingsAttendanceMarkedForCurrentDate() {
		try {
			LOGGER.info("Fetching Attendence marked bookings");

			return bookingMappingDAO.getAllBookingsAttendanceMarkedForCurrentDate();
		} catch (RuntimeException e) {
			throw new NoAttendanceMarkedBookingsFoundException("No Attendance Marked Bookings Found For Current Date!");
		}
	}

	@Override
	public AdminDashboardModel getAdminDashboardModel() {
		return bookingMappingDAO.getAdminDashboardModel();
	}
}