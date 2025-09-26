package com.valtech.team3.bookmyseatbackend.dao;

import com.valtech.team3.bookmyseatbackend.entities.Role;

/**
 * This is an interface for the RoleDAO class.
 */
public interface RoleDAO {

	/**
	 * Retrieves a role by its name.
	 *
	 * @param roleName The name of the role to retrieve.
	 * @return The role corresponding to the provided name, or null if not found.
	 */
	Role getRoleByRoleName(String roleName);

	/**
	 * Retrieves a role by its unique ID.
	 *
	 * @param id The unique ID of the role to retrieve.
	 * @return The role corresponding to the provided ID, or null if not found.
	 */
	Role getRoleById(int id);

	/**
	 * Retrieves the role of the user identified by the specified user ID.
	 * 
	 * This method retrieves the role assigned to the user identified by the
	 * provided user ID. The role typically represents the level of access,
	 * permissions, or responsibilities associated with the user within the system.
	 * 
	 * @param userId The unique identifier of the user for whom the role is to be
	 *               retrieved.
	 * 
	 * @return A Role object representing the role of the user. If no role is found
	 *         for the given user ID, returns null.
	 * 
	 * @see Role
	 */
	Role getUsersRole(int userId);
}