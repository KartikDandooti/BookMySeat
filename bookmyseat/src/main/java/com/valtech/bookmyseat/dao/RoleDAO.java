package com.valtech.bookmyseat.dao;

import com.valtech.bookmyseat.entity.Role;

/**
 * Data Access Object (DAO) interface for retrieving roles.
 */
public interface RoleDAO {

	/**
	 * Retrieves a role based on the provided role ID.
	 * 
	 * @param roleId The ID of the role to retrieve.
	 * @return The Role object associated with the provided role ID.
	 */
	Role getUserRoleByRoleID(int roleId);
}
