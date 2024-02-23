package com.valtech.bookmyseat.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.valtech.bookmyseat.dao.ShiftDAO;
import com.valtech.bookmyseat.entity.Shift;
import com.valtech.bookmyseat.service.ShiftDetailsService;

@Service
public class ShiftServiceImpl implements ShiftDetailsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ShiftServiceImpl.class);

	@Autowired
	private ShiftDAO shiftServiceDAO;

	@Override
	public List<Shift> getAllShiftDetails() throws DataAccessException {
		return shiftServiceDAO.getAllShiftDetails();
	}

	@Override
	public String deleteShift(int shiftId) throws DataAccessException {
		LOGGER.info("deleting the shift timings from the database");
		boolean success = shiftServiceDAO.deleteShiftDetailsById(shiftId);
		if (success) {
			LOGGER.debug("Shift Details with Id : {} has been deleted successfully", shiftId);

			return "Shift Details with Id " + shiftId + " has been deleted successfully";
		} else {
			return "Failed to delete the shift details with id " + shiftId;
		}
	}

	@Override
	public String addShiftTime(Shift shift) throws DataAccessException {
		LOGGER.info("adding the new shift timings to the database");
		try {
			shiftServiceDAO.addShiftTime(shift);
			LOGGER.debug("Shift Time added successfully");

			return "Shift Time added successfully";
		} catch (Exception e) {
			LOGGER.error("handling the exception to add the shift timings:{}", e.getMessage());

			return "Failed to add the shift time " + e.getMessage();
		}
	}

	@Override
	public String updateShiftTime(int id, Shift shift) throws DataAccessException {
		LOGGER.info("updating the shift timings in te database");
		try {
			shiftServiceDAO.updateShiftTime(id, shift);
			LOGGER.debug("shift has been updated succesfully");

			return "Shift time updated successfully";
		} catch (Exception e) {
			LOGGER.error("handling the exceptions of updateShiftTime:{}", e.getMessage());

			return "Failed to update the shift time " + e.getMessage();
		}
	}
}