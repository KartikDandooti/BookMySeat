package com.valtech.team3.bookmyseatbackend.service;

import com.valtech.team3.bookmyseatbackend.models.RestrictedSeatModel;

public interface RestrictedSeatService {

	/**
	 * Creates a user restriction based on the provided RestrictedSeatModel.
	 * 
	 * This method is responsible for generating a user restriction based on the
	 * information provided in the RestrictedSeatModel. The RestrictedSeatModel
	 * typically contains details such as seat restrictions, user permissions, or
	 * access limitations.
	 * 
	 * @param restrictedSeatModel The RestrictedSeatModel object containing the
	 *                            necessary information to create the user
	 *                            restriction.
	 * 
	 * @return A String representing the result of the user restriction creation
	 *         process. It may contain messages indicating success, failure, or any
	 *         other relevant information.
	 * 
	 * @see RestrictedSeatModel
	 */
	String createUserRestriction(RestrictedSeatModel restrictedSeatModel);

	/**
	 * Removes a restriction associated with the specified seat ID.
	 * 
	 * This method is responsible for removing any restriction or limitation
	 * associated with the seat identified by the provided seat ID. It performs the
	 * necessary operations to revoke any access restrictions or permissions that
	 * were previously applied to the specified seat.
	 * 
	 * @param seatId The unique identifier of the seat from which the restriction is
	 *               to be removed.
	 * 
	 * @return A String indicating the result of the removal operation. This may
	 *         contain messages indicating success, failure, or any other relevant
	 *         information regarding the removal process.
	 */
	String removeRestriction(int seatId);

	/**
	 * Creates a project restriction based on the provided RestrictedSeatModel.
	 *
	 * This method generates a project restriction based on the information provided
	 * in the RestrictedSeatModel. The RestrictedSeatModel typically contains
	 * details such as seat restrictions, user permissions, or access limitations
	 * related to a specific project.
	 *
	 * @param restrictedSeatModel The RestrictedSeatModel object containing the
	 *                            necessary information to create the project
	 *                            restriction.
	 *
	 * @return A String indicating the result of the project restriction creation
	 *         process. This may contain messages indicating success, failure, or
	 *         any other relevant information related to the creation process.
	 *
	 * @see RestrictedSeatModel
	 */
	String createProjectRestriction(RestrictedSeatModel restrictedSeatModel);

	/**
	 * Removes a project restriction associated with the specified project ID.
	 * 
	 * This method is responsible for removing any restriction or limitation
	 * associated with the project identified by the provided project ID. It
	 * performs the necessary operations to revoke any access restrictions or
	 * permissions that were previously applied to the specified project.
	 * 
	 * @param projectId The unique identifier of the project from which the
	 *                  restriction is to be removed.
	 * 
	 * @return A String indicating the result of the removal operation. This may
	 *         contain messages indicating success, failure, or any other relevant
	 *         information regarding the removal process.
	 */
	String removeProjectRestriction(int projectId);

	/**
<<<<<<< HEAD
	 * Removes a project restriction associated with the specified seat Id.
	 * 
	 * This method is responsible for removing any restriction on particular seat.
	 * 
	 * @param seatId The unique identifier of the seat for which the restriction is
	 *               to be removed.
	 * 
	 * @return A String indicating the result of the removal operation. This may
	 *         contain messages indicating success, failure, or any other relevant
	 *         information regarding the removal process.
=======
	 * Removes any project restriction associated with the given seat ID.
	 *
	 * @param seatId The ID of the seat for which the project restriction is to be
	 *               removed.
	 * @return A {@code String} indicating the result of the operation. This may be
	 *         a success message or an error message if the operation fails. Returns
	 *         {@code null} if no project restriction was found for the specified
	 *         seat.
>>>>>>> bbcd19f9783d357c3a08b2c82b28c35e3f2a7006
	 */
	String removeProjectRestrictionForSeat(int seatId);
}