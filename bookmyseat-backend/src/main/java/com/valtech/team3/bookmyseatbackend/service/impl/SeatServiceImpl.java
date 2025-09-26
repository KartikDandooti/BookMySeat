package com.valtech.team3.bookmyseatbackend.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.team3.bookmyseatbackend.dao.SeatDAO;
import com.valtech.team3.bookmyseatbackend.entities.Seat;
import com.valtech.team3.bookmyseatbackend.exceptions.DataBaseAccessException;
import com.valtech.team3.bookmyseatbackend.models.BookingModel;
import com.valtech.team3.bookmyseatbackend.service.SeatService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SeatServiceImpl implements SeatService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SeatServiceImpl.class);

	@Autowired
	private SeatDAO seatDAO;

	@Override
	public List<Seat> getAvailableSeatsByFloorOnDate(BookingModel bookingModel) {
		try {
			LOGGER.info("Fetching available Seats for a Floor Between startDate and endDate");
			LOGGER.debug("Fetching seats by floor with floor id: {}", bookingModel.getFloorId());

			return seatDAO.findAvailableSeatsByFloorOnDate(bookingModel.getFloorId(), bookingModel.getStartDate(), bookingModel.getEndDate());
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to get all seats by floor !");
		}
	}

	@Override
	public List<Seat> getAllSeatsByFloor(int floorId) {
		try {
			LOGGER.info("Fetching all Seats for by Floor !");

			return seatDAO.getSeatsByFloor(floorId);
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to get all seats by floor !");
		}
	}

	@Override
	public List<Seat> getAllReservedSeats() {
		try {
			LOGGER.info("Fetching all  Reserved Seats !");

			return seatDAO.getAllReservedSeats();
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to get all Reserved Seats !");
		}
	}

	@Override
	public List<Seat> getAllUserReservedSeats() {
		try {
			LOGGER.info("Fetching all User Reserved Seats !");

			return seatDAO.getAllUserReservedSeats();
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to get all User Reserved Seats !");
		}
	}

	@Override
	public List<Seat> getAllProjectReservedSeats() {
		try {
			LOGGER.info("Fetching all Project Reserved Seats !");

			return seatDAO.getAllProjectReservedSeats();
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to get all Project Reserved Seats !");
		}
	}
}