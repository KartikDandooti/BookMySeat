package com.valtech.team3.bookmyseatbackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.valtech.team3.bookmyseatbackend.dao.RestrictedSeatDAO;
import com.valtech.team3.bookmyseatbackend.dao.SeatDAO;
import com.valtech.team3.bookmyseatbackend.entities.RestrictedSeat;
import com.valtech.team3.bookmyseatbackend.entities.Seat;
import com.valtech.team3.bookmyseatbackend.exceptions.DataBaseAccessException;
import com.valtech.team3.bookmyseatbackend.models.RestrictedSeatModel;
import com.valtech.team3.bookmyseatbackend.service.RestrictedSeatService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RestrictedSeatServiceImpl implements RestrictedSeatService {

	@Autowired
	private RestrictedSeatDAO restrictedSeatDAO;
	@Autowired
	private SeatDAO seatDAO;

	@Override
	public String createUserRestriction(RestrictedSeatModel restrictedSeatModel) {
		if (Boolean.TRUE.equals(restrictedSeatDAO.isExistsRestrictionForUser(restrictedSeatModel.getUserId()))) {
			throw new DataBaseAccessException("Seat is already reserved for user !");
		}
		try {
			int restrictionId = restrictedSeatDAO.createUserRestriction(restrictedSeatModel.getUserId());
			seatDAO.updateSeatRestriction(restrictedSeatModel.getSeatId(), restrictionId);

			return "Seat Restriction created successfully !";
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to create Restriction. Please try again !");
		}
	}

	@Override
	public String removeRestriction(int seatId) {
		try {
			Seat seat = seatDAO.getSeatById(seatId);
			seatDAO.updateSeatRestriction(seat.getId(), null);
			restrictedSeatDAO.removeRestriction(seat.getRestrictedSeat().getId());

			return "Seat Restriction removed successfully !";
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to remove Restriction. Please try again !");
		}
	}

	@Override
	public String createProjectRestriction(RestrictedSeatModel restrictedSeatModel) {
		try {
			if (Boolean.TRUE.equals(restrictedSeatDAO.isExistsRestrictionForProject(restrictedSeatModel.getProjectId()))) {
				RestrictedSeat restrictedSeat = restrictedSeatDAO.getRestrictedSeatByProjectId(restrictedSeatModel.getProjectId());
				for (int seatId : restrictedSeatModel.getSeats()) {
					seatDAO.updateSeatRestriction(seatId, restrictedSeat.getId());
				}
				return "Seat Restriction for Project created successfully !";
			}
			int restrictionId = restrictedSeatDAO.createProjectRestriction(restrictedSeatModel.getProjectId());
			for (int seatId : restrictedSeatModel.getSeats()) {
				seatDAO.updateSeatRestriction(seatId, restrictionId);
			}
			return "Seat Restriction for Project created successfully !";
		} catch (Exception e) {
			throw new DataBaseAccessException("Failed to create Restriction. Please try again !" + e);
		}

	}

	@Override
	public String removeProjectRestriction(int projectId) {
		try {
			RestrictedSeat restrictedSeat = restrictedSeatDAO.getRestrictedSeatByProjectId(projectId);
			seatDAO.updateProjectReserveation(restrictedSeat.getId());
			restrictedSeatDAO.removeRestriction(restrictedSeat.getId());

			return "Seat Restriction for Project removed successfully !";
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to remove Restriction. Please try again !");
		}
	}

	@Override
	public String removeProjectRestrictionForSeat(int seatId) {

		try {
			Seat seat = seatDAO.getSeatById(seatId);
			seatDAO.updateSeatRestriction(seat.getId(), null);

			return "Seat Restriction for Project removed successfully !";
		} catch (RuntimeException e) {
			throw new DataBaseAccessException("Failed to remove Restriction. Please try again !");
		}
	}
}