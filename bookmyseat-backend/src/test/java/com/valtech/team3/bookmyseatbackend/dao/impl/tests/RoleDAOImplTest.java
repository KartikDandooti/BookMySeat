package com.valtech.team3.bookmyseatbackend.dao.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.valtech.team3.bookmyseatbackend.dao.impl.RoleDAOImpl;
import com.valtech.team3.bookmyseatbackend.entities.Role;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class RoleDAOImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;
	@Mock
	private ResultSet resultSet;
	@InjectMocks
	private RoleDAOImpl roleDAO;

	@Test
	void testGetRoleByRoleName() {
		Role expectedRole = new Role(1, "ADMIN");
		when(jdbcTemplate.queryForObject(any(String.class), ArgumentMatchers.<BeanPropertyRowMapper<Role>>any(), any(String.class))).thenReturn(expectedRole);

		Role actualRole = roleDAO.getRoleByRoleName("Admin");

		assertEquals(expectedRole, actualRole);
	}

	@Test
	void testGetRoleById() {
		Role expectedRole = new Role(1, "ADMIN");
		when(jdbcTemplate.queryForObject(any(String.class), any(BeanPropertyRowMapper.class), any(Integer.class))).thenReturn(expectedRole);

		Role actualRole = roleDAO.getRoleById(1);

		assertEquals(expectedRole, actualRole);
	}

	@Test
	void testGetUsersRole_ValidUser() {
		int userId = 1;
		int roleId = 2;
		String roleName = "ADMIN";
		Role expectedRole = new Role(roleId, roleName);
		Mockito.when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq(userId))).thenReturn(expectedRole);

		Role actualRole = roleDAO.getUsersRole(userId);

		assertEquals(expectedRole, actualRole);

		verify(jdbcTemplate).queryForObject(eq("SELECT * FROM ROLE WHERE ID IN (SELECT ROLE_ID FROM USER WHERE ID = ?)"), any(BeanPropertyRowMapper.class), eq(userId));
	}
}