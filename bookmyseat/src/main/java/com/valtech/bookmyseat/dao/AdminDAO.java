package com.valtech.bookmyseat.dao;

import java.util.List;

import com.valtech.bookmyseat.entity.Reserved;
import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.exception.DataBaseAccessException;
import com.valtech.bookmyseat.model.AdminDashBoardModel;
import com.valtech.bookmyseat.model.BookingDetailsOfUserForAdminReport;
import com.valtech.bookmyseat.model.UserRequestsModel;

/**
 * Data Access Object (DAO) interface for admin-related operations.
 */
public interface AdminDAO {

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
	 * Updates the approval status(Approved or Rejected) of a user in the database.
	 * 
	 * @param The User object containing the updated approval status and user ID and
	 *            Integer value of User ID
	 * @return The integer value representing the number of row updated.
	 * @throws DataBaseAccessException which extends DataAccessException if there is
	 *                                 an issue while updating the approval status
	 */
	int updateUserRequests(User user, int userId) throws DataBaseAccessException;

	/**
	 * Reserves a seat for a user on a specific floor.
	 * 
	 * @return An object representing the reserved seat, containing information such
	 *         as user ID, floor ID, and seat ID.
	 */
	List<Reserved> reserveSeat();

	/**
	 * Reserves a seat for a user on a specific floor and stores the reservation in
	 * the database.
	 * 
	 * @param userId  The ID of the user for whom the seat is being reserved.
	 * @param floorId The ID of the floor where the seat is located.
	 * @param seatId  The ID of the seat being reserved.
	 */
	void reserveSeatInDB(int userId, int floorId, int seatId);

	/**
	 * Retrieves a list of all booking details.
	 *
	 * @return A list containing all booking details.
	 */
	List<BookingDetailsOfUserForAdminReport> getAllBookingDetailsOfUserForAdminReport();
}