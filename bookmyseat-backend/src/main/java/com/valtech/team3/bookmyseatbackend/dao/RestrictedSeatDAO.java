package com.valtech.team3.bookmyseatbackend.dao;

import com.valtech.team3.bookmyseatbackend.entities.RestrictedSeat;

/**
 * This is an interface for RestrictedSeatDAO.
 */
public interface RestrictedSeatDAO {

	/**
	 * Retrieves the restricted seat with the specified ID.
	 *
	 * @param restrictedSeatId the ID of the restricted seat to retrieve
	 * @return the restricted seat with the specified ID, or null if not found
	 */
	RestrictedSeat getRestrictedSeatById(int restrictedSeatId);

	/**
	 * Creates a new restriction for a restricted seat using the provided restricted
	 * seat model.
	 *
	 * @param userId the id user for creating reservation
	 * @return
	 */
	Integer createUserRestriction(int userId);

	/**
	 * Removes the restriction with the specified ID.
	 *
	 * @param restrictionId the ID of the restriction to remove
	 */
	void removeRestriction(int restrictionId);

	/**
	 * Checks if any restrictions exist for the specified user ID.
	 * 
	 * This method verifies whether any restrictions or limitations are currently
	 * imposed on the user identified by the provided user ID. It checks if there
	 * are any access restrictions, permissions, or limitations associated with the
	 * user, such as seat restrictions, project access restrictions, or other types
	 * of limitations.
	 * 
	 * @param userId The unique identifier of the user for whom the restrictions are
	 *               to be checked.
	 * 
	 * @return true if restrictions exist for the specified user ID, false
	 *         otherwise. Returns true if there are any restrictions imposed on the
	 *         user, and false if there are no restrictions.
	 */
	Boolean isExistsRestrictionForUser(int userId);

	/**
	 * Creates a project restriction for the specified project ID.
	 * 
	 * This method generates and applies a project restriction for the project
	 * identified by the provided project ID. It may involve setting access
	 * limitations, permissions, or other restrictions related to the specified
	 * project.
	 * 
	 * @param projectId The unique identifier of the project for which the
	 *                  restriction is to be created.
	 * 
	 * @return An integer value representing the result of the project restriction
	 *         creation process. This value may indicate the success status, error
	 *         code, or any other relevant information related to the creation
	 *         process. A non-negative value typically indicates success, while a
	 *         negative value may represent an error condition.
	 */
	Integer createProjectRestriction(int projectId);

	/**
	 * Retrieves the restricted seat associated with the specified project ID.
	 * 
	 * This method retrieves the restricted seat object that is associated with the
	 * project identified by the provided project ID. The restricted seat typically
	 * contains information about seat restrictions, access limitations, or
	 * permissions specific to the project.
	 * 
	 * @param projectId The unique identifier of the project for which the
	 *                  restricted seat is to be retrieved.
	 * 
	 * @return A RestrictedSeat object representing the seat restriction associated
	 *         with the specified project ID. If no restricted seat is found for the
	 *         given project ID, returns null.
	 * 
	 * @see RestrictedSeat
	 */
	RestrictedSeat getRestrictedSeatByProjectId(int projectId);

	/**
	 * Checks if any restrictions exist for the specified project ID.
	 * 
	 * This method verifies whether any restrictions or limitations are currently
	 * imposed on the project identified by the provided project ID. It checks if
	 * there are any access restrictions, permissions, or limitations associated
	 * with the project, such as restricted seats, user access limitations, or other
	 * types of restrictions.
	 * 
	 * @param projectId The unique identifier of the project for which the
	 *                  restrictions are to be checked.
	 * 
	 * @return true if restrictions exist for the specified project ID, false
	 *         otherwise. Returns true if there are any restrictions imposed on the
	 *         project, and false if there are no restrictions.
	 */
	Boolean isExistsRestrictionForProject(int projectId);
}