package com.valtech.bookmyseat.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.bookmyseat.dao.SeatDAO;
import com.valtech.bookmyseat.entity.Seat;
import com.valtech.bookmyseat.exception.DataBaseAccessException;
import com.valtech.bookmyseat.model.BookingModel;
import com.valtech.bookmyseat.service.SeatService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SeatServiceImpl implements SeatService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SeatServiceImpl.class);

	@Autowired
	private SeatDAO seatDAO;

	@Override
	public List<Seat> getAvailableSeatsByFloorOnDate(BookingModel bookingModel) {
		try {
			LOGGER.info("Fetching available Seats for a Floor Between startDate: {} and endDate: {}",
					bookingModel.getStartDate(), bookingModel.getEndDate());
			LOGGER.debug("Fetching seats by floor with floor id: {}", bookingModel.getFloorId());

			return seatDAO.findAvailableSeatsByFloorOnDate(bookingModel.getFloorId(), bookingModel.getStartDate(),
					bookingModel.getEndDate());
		} catch (DataBaseAccessException e) {
			throw new DataBaseAccessException("Failed to get all seats by floor !");
		}
	}
}