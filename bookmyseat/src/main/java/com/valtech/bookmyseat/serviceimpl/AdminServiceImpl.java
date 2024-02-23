package com.valtech.bookmyseat.serviceimpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valtech.bookmyseat.dao.AdminDAO;
import com.valtech.bookmyseat.dao.LocationDAO;
import com.valtech.bookmyseat.dao.ProjectDAO;
import com.valtech.bookmyseat.dao.UserDAO;
import com.valtech.bookmyseat.entity.ApprovalStatus;
import com.valtech.bookmyseat.entity.Location;
import com.valtech.bookmyseat.entity.Project;
import com.valtech.bookmyseat.entity.Reserved;
import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.exception.DataBaseAccessException;
import com.valtech.bookmyseat.model.AdminDashBoardModel;
import com.valtech.bookmyseat.model.BookingDetailsOfUserForAdminReport;
import com.valtech.bookmyseat.model.ProjectModel;
import com.valtech.bookmyseat.model.UserRequestsModel;
import com.valtech.bookmyseat.service.AdminService;
import com.valtech.bookmyseat.service.EmailService;

@Service
public class AdminServiceImpl implements AdminService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired
	private AdminDAO adminDAO;

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private LocationDAO locationDAO;

	public List<AdminDashBoardModel> fetchAdminDashboardDetails() throws DataBaseAccessException {
		LOGGER.info("Fetching the daily booking details...");
		List<AdminDashBoardModel> dailyBookingDetails = adminDAO.fetchAdminDashboardDetails();
		if (dailyBookingDetails.isEmpty()) {
			throw new DataBaseAccessException("Error occurred while fetching Daily Booking Details...");
		}
		LOGGER.info("Successfully fetched daily booking details....");

		return dailyBookingDetails;
	}

	@Override
	public List<UserRequestsModel> fetchUserRequests() throws DataBaseAccessException {
		LOGGER.info("Fetching User Requests....");
		List<UserRequestsModel> userRequests = adminDAO.fetchUserRequests();
		if (userRequests.isEmpty()) {
			throw new DataBaseAccessException("Error occurred while fetching User Requests...");
		}
		LOGGER.info("Successfully fetched User Requests....");

		return userRequests;
	}

	@Override
	public int updateUserRequests(List<UserRequestsModel> userRequestsModels, int userId)
			throws DataBaseAccessException {
		LOGGER.info("Updating multiple User approval requests...");
		if (Objects.isNull(userRequestsModels) || userRequestsModels.isEmpty()) {
			throw new IllegalArgumentException("userRequestsModels cannot be null or empty...");
		}
		int totalRowsUpdated = 0;
		for (UserRequestsModel userRequestsModel : userRequestsModels) {
			if (Objects.isNull(userRequestsModel)) {
				LOGGER.warn("userRequestsModel cannot be null");
				continue;
			}
			LOGGER.info("updating the User approval request...");
			LOGGER.debug("Updating the User approval request for User {} with Approval Status {}...",
					userRequestsModel.getUserID(), userRequestsModel.getApprovalStatus());
			User user = new User();
			user.setUserId(userRequestsModel.getUserID());
			user.setApprovalStatus(ApprovalStatus.valueOf(userRequestsModel.getApprovalStatus()));
			int rowsUpdated = adminDAO.updateUserRequests(user, userId);
			if (rowsUpdated == 0) {
				throw new DataBaseAccessException("Failed to update user request. No rows were updated.");
			}
			LOGGER.info("Successfully updated the User approval request...");
			LOGGER.debug("Successfully updated the approval status for User: {} with Approval Status: {}...",
					userRequestsModel.getUserID(), userRequestsModel.getApprovalStatus());
			user = userDAO.findUserByuserId(userRequestsModel.getUserID());
			try {
				if ("APPROVED".equals(userRequestsModel.getApprovalStatus())) {
					emailService.sendApprovalEmailToUser(user);
				} else {
					emailService.sendRejectionEmailToUser(user);
				}
			} catch (Exception e) {
				LOGGER.error("Error sending email for User {}. Error: {}", userRequestsModel.getUserID(),
						e.getMessage());
			}
			totalRowsUpdated += rowsUpdated;
		}
		LOGGER.info("Successfully updated approval statuses for {} users.", totalRowsUpdated);

		return totalRowsUpdated;
	}

	@Override
	public void createNewProject(ProjectModel projectModel) throws SQLException {
		LOGGER.info("Creating new Project by the name: {}", projectModel.getProjectName());
		Project project = projectModel.getProjectDetails();
		projectDAO.createProject(project);
	}

	@Override
	public List<Project> getAllProjects() throws SQLException {
		LOGGER.info("Retrieving all Projects");

		return projectDAO.getAllProjects();
	}

	@Override
	public void deleteProjectById(int projectId) throws SQLException {
		LOGGER.info("Deleting project by Id");
		projectDAO.deleteProjectById(projectId);
	}

	@Override
	public void updateProject(ProjectModel projectModel, int projectId) throws SQLException {
		LOGGER.info("Updating existing project");
		Project project = projectModel.getProjectDetails();
		project.setModifiedDate(LocalDate.now());
		projectDAO.updateProject(project, projectId);
	}

	@Override
	public List<Location> getAllLocations() throws DataBaseAccessException {
		LOGGER.info("fetching all Location");

		return locationDAO.getAllLocation();
	}

	@Override
	public void deleteLocation(int locationId) throws DataBaseAccessException {
		LOGGER.info("deleting the location");
		locationDAO.deleteLocation(locationId);
	}

	@Override
	public void updateLocation(Location location, int locationId) {
		LOGGER.info("updating the location");

		locationDAO.updateLocation(location, locationId);
	}

	@Override
	public List<BookingDetailsOfUserForAdminReport> getAllBookingDetailsOfUserForAdminReport() {
		LOGGER.info("fetching all the booking details");

		return adminDAO.getAllBookingDetailsOfUserForAdminReport();
	}

	@Override
	public List<Reserved> reservedSeats() throws DataBaseAccessException {
		LOGGER.info("Reserved seats Details");
		
		return adminDAO.reserveSeat();
	}

	@Override
	public void reserveSeat(int userId, int floorId, int seatId) throws DataBaseAccessException {
		LOGGER.info("Reserving a seat in the Database");
		adminDAO.reserveSeatInDB(userId, floorId, seatId);
	}
}