package com.valtech.bookmyseat.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.valtech.bookmyseat.dao.RoleDAO;
import com.valtech.bookmyseat.entity.Role;

@Repository
public class RoleDaoImpl implements RoleDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Role getUserRoleByRoleID(int roleId) {
		String selectQuery = "SELECT R.ROLE_ID, R.ROLE_NAME " + "FROM ROLE R " + "WHERE R.ROLE_ID=?";

		return jdbcTemplate.queryForObject(selectQuery, new BeanPropertyRowMapper<>(Role.class), (Object) roleId);
	}
}