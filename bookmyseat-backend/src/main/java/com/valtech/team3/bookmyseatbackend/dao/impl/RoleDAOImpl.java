package com.valtech.team3.bookmyseatbackend.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.valtech.team3.bookmyseatbackend.dao.RoleDAO;
import com.valtech.team3.bookmyseatbackend.entities.Role;

@Repository
public class RoleDAOImpl implements RoleDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Role getRoleByRoleName(String roleName) {
		String selectQuery = "SELECT * FROM ROLE WHERE ROLE_NAME = ?";

		return jdbcTemplate.queryForObject(selectQuery, new BeanPropertyRowMapper<>(Role.class), roleName);
	}

	@Override
	public Role getRoleById(int id) {
		String selectQuery = "SELECT * FROM ROLE WHERE ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new BeanPropertyRowMapper<>(Role.class), id);
	}

	@Override
	public Role getUsersRole(int userId) {
		String selectQuery = "SELECT * FROM ROLE WHERE ID IN (SELECT ROLE_ID FROM USER WHERE ID = ?)";

		return jdbcTemplate.queryForObject(selectQuery, new BeanPropertyRowMapper<>(Role.class), userId);
	}
}