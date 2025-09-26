package com.valtech.team3.bookmyseatbackend.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.team3.bookmyseatbackend.dao.BookingDAO;
import com.valtech.team3.bookmyseatbackend.dao.BookingMappingDAO;
import com.valtech.team3.bookmyseatbackend.dao.SeatDAO;
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
import com.valtech.team3.bookmyseatbackend.service.BookingService;
import com.valtech.team3.bookmyseatbackend.service.EmailService;

import jakarta.mail.MessagingException;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BookingServiceImpl implements BookingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BookingServiceImpl.class);

	@Autowired
	private BookingDAO bookingDAO;
	@Autowired
	private SeatDAO seatDAO;
	@Autowired
	private BookingMappingDAO bookingMappingDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private EmailService emailService;

	Random random = new Random();

	@Override
	public Integer createBooking(BookingModel booking, User user) {
		booking.setUser(user);
		booking.setBookingStatus(true);
		booking.setAttendance(false);
		booking.setCreatedDate(LocalDate.now());
		Seat seat = seatDAO.getSeatById(booking.getSeatId());
		int randomNumber = random.nextInt(900000) + 100000;
		booking.setVerificationCode(randomNumber);
		if (hasAlreadyBookedForDate(user.getId(), booking.getStartDate(), booking.getEndDate())) {
			LOGGER.error("Seat already booked for this {} - {} date range", booking.getStartDate(), booking.getEndDate());
			throw new AlreadyBookedForDateException("You have already booked for this date range.");
		}

		int twoWheelerCount = bookingMappingDAO.getVehicleTypeCount("WHEELER_2", booking.getStartDate(), booking.getEndDate());
		int fourWheelerCount = bookingMappingDAO.getVehicleTypeCount("WHEELER_4", booking.getStartDate(), booking.getEndDate());
		if (booking.isParking()) {
			if ("WHEELER_2".equals(booking.getVehicleType()) && twoWheelerCount > 40) {
				LOGGER.error("Cannot create booking. Two-wheeler parking is full.");
				throw new NoParkingAvailableException("Two-wheeler parking is full. Please use public transport."); 
			} else if ("WHEELER_4".equals(booking.getVehicleType()) && fourWheelerCount > 10) {
				LOGGER.error("Cannot create booking. Four-wheeler parking is full.");
				throw new NoParkingAvailableException("Four-wheeler parking is full. Please use public transport.");
			}
		}

		LOGGER.info("Booking Seat....");
		LOGGER.debug("Booking seat for user : {}", user.getFirstName() + user.getLastName());

		return bookingDAO.createBooking(booking, user, seat);
	}

	public Boolean hasAlreadyBookedForDate(int userId, LocalDate startDate, LocalDate endDate) {
		LOGGER.info("Checking whether seat is booked for particular date range");
		try {

			return bookingDAO.hasAlreadyBookedForDate(userId, startDate, endDate);
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to check whether seat is already booked for particular date range");
		}
	}

	@Override
	public List<Booking> getAllBookings(int userId) {
		try {
			LOGGER.info("Fetching booking of User: {}", userId);

			return bookingDAO.getAllBookings(userId);
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Error getting all the Bookings !");
		}
	}

	@Override
	public List<Booking> getAllUserBookings(int userId) {
		try {
			LOGGER.info("Fetching booking of User: {}", userId);

			return bookingDAO.getAllUserBookings(userId);
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Error getting all the Bookings !");
		}
	}

	@Override
	public void cancelBooking(int bookingId) {
		try {
			LOGGER.debug("Cancelling booking with ID: {}", bookingId);
			bookingDAO.cancelBooking(bookingId);
			LOGGER.info("Booking with ID {} successfully cancelled", bookingId);
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Unable to cancel booking, try again later!");
		}
	}

	@Override
	public List<Booking> getUserPreferredSeats(int floorId, int projectId, LocalDate startDate) {
		try {
			LOGGER.info("Fetching preferred Seats for logged in User !");

			return bookingDAO.userPreferredSeats(floorId, projectId, startDate);
		} catch (DataAccessException e) {
			throw new DataBaseAccessException("Failed to get User preferred Seats !");
		}
	}

	@Override
	public List<Booking> getBookingsforFloorBetweenDates(BookingModel bookingModel) {
		try {
			LOGGER.info("Fetching Bookings between the Date range !");

			return bookingDAO.findBookingsByFloorAndDates(bookingModel.getFloorId(), bookingModel.getStartDate(), bookingModel.getEndDate());
		} catch (RuntimeException e) {
			LOGGER.error("Failed to get Bookings between selected Booking range: {}", e.getMessage());
			throw new DataBaseAccessException("Failed to get Bookings between selected Booking range !");
		}
	}

	public Boolean isAttendanceMarked(int bookingId) {
		LOGGER.debug("Checking if attendance is marked for booking with ID: {}", bookingId);
		List<BookingMapping> bookingMappings = bookingMappingDAO.getBookingMappingsByBookingId(bookingId);
		for (BookingMapping mapping : bookingMappings) {
			if (mapping.isAttendance()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Booking getBookingById(int id) {
		try {
			LOGGER.info("Fetching booking by Id");
			return bookingDAO.getBookingById(id);
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to get User's !");
		}
	}

	@Override
	public void updateBookingByAdmin(BookingModel bookingModel) {
		boolean attendanceMarked = bookingMappingDAO.isAttendanceMarkedForBooking(bookingModel.getId());
		if (attendanceMarked) {
			throw new AttendanceMarkedException("Attendance is already marked for the specified booking.");
		} else {
			bookingModel.setModifiedDate(LocalDateTime.now());
			LOGGER.info("upadting seat of bookingId:{}", bookingModel.getId());
			bookingDAO.updateBookingByAdmin(bookingModel, bookingModel.getId(), bookingModel.getSeatId());
			User user = userDAO.getUserByBookingId(bookingModel.getId());
			try {
				emailService.sendSeatModificationMail(user);
				LOGGER.info("mail sent");
			} catch (MessagingException e) {
				LOGGER.error("Error sending seat modification email to user", e);
			}
		}
	}

	@Override
	public Integer getAttendanceCountForCurrentMonth(int userId) {
		try {
			return bookingDAO.getAttendanceCountForCurrentMonth(userId);
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to get Attendance Count !");
		}
	}

	@Override
	public List<UserDashboardModel> getUserDashboard(int userId) {
		try {
			List<UserDashboardModel> bookings = bookingDAO.getUserDashboard(userId);
			for (UserDashboardModel booking : bookings) {
				boolean attendanceMarked = isAttendanceMarked(booking.getBookingId());
				boolean allowCancel = !attendanceMarked;
				booking.setAllowCancel(allowCancel);
			}
			return bookings;
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to get info for User dashboard !");
		}
	}
}