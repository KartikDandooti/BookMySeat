package com.valtech.bookmyseat.service;

import java.sql.SQLException;
import java.util.List;

import com.valtech.bookmyseat.entity.Location;
import com.valtech.bookmyseat.entity.Project;
import com.valtech.bookmyseat.entity.Reserved;
import com.valtech.bookmyseat.exception.DataBaseAccessException;
import com.valtech.bookmyseat.model.AdminDashBoardModel;
import com.valtech.bookmyseat.model.BookingDetailsOfUserForAdminReport;
import com.valtech.bookmyseat.model.ProjectModel;
import com.valtech.bookmyseat.model.UserRequestsModel;

/**
 * Service interface for handling admin-related operations.
 */
public interface AdminService {

	/**
	 * Retrieves daily booking details for the AdminDashboard which includes count
	 * of seat booked,tea/coffee,parking,lunch opted details.
	 * 
	 * @return a list of AdminDashBoardModel objects containing daily booking
	 *         details.
	 * @throws DataBaseAccessException which extends DataAccessException if there is
	 *                                 an issue accessing the data.
	 */
	List<AdminDashBoardModel> fetchAdminDashboardDetails() throws DataBaseAccessException;

	/**
	 * Retrieves a list of user all requests like pending,approved,rejected from the
	 * database.
	 *
	 * @return A list of UserRequestsModel objects representing user requests.
	 * @throws DataBaseAccessException which extends DataAccessException if there is
	 *                                 an issue accessing the data.
	 */
	List<UserRequestsModel> fetchUserRequests() throws DataBaseAccessException;

	/**
	 * Updates the approval status (Approved or Rejected) of a user in the database.
	 *
	 * @param A list of UserRequestsModel in which each userRequestsModel object
	 *          containing the updated approval status and user details and integer
	 *          value of user Id.
	 * @return An integer value representing the number of rows affected by the
	 *         update operation.
	 * @throws DataBaseAccessException which extends DataAccessException if there is
	 *                                 an issue while updating the approval status
	 *                                 of user.
	 */
	int updateUserRequests(List<UserRequestsModel> userRequestsModels, int userId) throws DataBaseAccessException;

	/**
	 * Creates a new project.
	 *
	 * @param projectModel The ProjectModel object containing details of the new
	 *                     project.
	 * @throws SQLException If there is an error occurred during the database
	 *                      operation.
	 */
	void createNewProject(ProjectModel projectModel) throws SQLException;

	/**
	 * Retrieves details of all projects.
	 *
	 * @return List of Project objects containing details of all projects.
	 * @throws SQLException If there is an error occurred during the database
	 *                      operation.
	 */
	List<Project> getAllProjects() throws SQLException;

	/**
	 * Deletes a project based on the project ID.
	 *
	 * @param projectId The ID of the project to be deleted.
	 * @throws SQLException If there is an error occurred during the database
	 *                      operation.
	 */
	void deleteProjectById(int projectId) throws SQLException;

	/**
	 * Updates an existing project.
	 *
	 * @param projectModel The ProjectModel object containing the updated project
	 *                     details.
	 * @param projectId    The ID of the project to be updated.
	 * @throws SQLException If there is an error occurred during the database
	 *                      operation.
	 */
	void updateProject(ProjectModel projectModel, int projectId) throws SQLException;

	/**
	 * Retrieves details of all locations.
	 *
	 * @return List of Location objects containing details of all locations.
	 * @throws SQLException If there is an error occurred during the database
	 *                      operation.
	 */
	List<Location> getAllLocations() throws SQLException;

	/**
	 * 
	 * @throws DataBaseAccessException if there is an error occured during the
	 *                                 database operation.
	 */
	void deleteLocation(int locationId) throws DataBaseAccessException;

	/**
	 * 
	 * @param locationModel This model contains the update location details.
	 * @param locationId    the ID of the location to be updated.
	 */
	void updateLocation(Location location, int locationId);

	/**
	 * Reserved seats on a specific floor.
	 *
	 * @return The reserved seat object containing information about the
	 *         reservation.
	 */
	List<Reserved> reservedSeats();

	/**
	 * Reserves a seat for a user on a specific floor.
	 *
	 * @param userId  The ID of the user for whom the seat is being reserved.
	 * @param floorId The ID of the floor where the seat is located.
	 * @param seatId  The ID of the seat being reserved.
	 * @throws IllegalArgumentException If userId, floorId, or seatId is negative.
	 */
	void reserveSeat(int userId, int floorId, int seatId);

	/**
	 * Retrieves a list of all booking details.
	 *
	 * @return a list containing all booking details
	 */
	List<BookingDetailsOfUserForAdminReport> getAllBookingDetailsOfUserForAdminReport();
}