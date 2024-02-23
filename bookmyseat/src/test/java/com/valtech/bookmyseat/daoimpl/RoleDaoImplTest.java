package com.valtech.bookmyseat.daoimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.valtech.bookmyseat.entity.Role;

@ExtendWith(MockitoExtension.class)
class RoleDaoImplTest {

	@Mock
	private JdbcTemplate jdbcTemplateMock;

	@InjectMocks
	private RoleDaoImpl roleDaoImpl;

	@SuppressWarnings("unchecked")
	@Test
	void testGetUserRoleByRoleID() {
		Role expectedRole = new Role();
		expectedRole.setRoleId(1);
		expectedRole.setRoleName("Admin");
		when(jdbcTemplateMock.queryForObject(anyString(), any(BeanPropertyRowMapper.class), any(Object[].class)))
				.thenReturn(expectedRole);
		Role actualRole = roleDaoImpl.getUserRoleByRoleID(1);
		verify(jdbcTemplateMock).queryForObject(anyString(), any(BeanPropertyRowMapper.class), any(Object[].class));
		assertEquals(expectedRole, actualRole);
	}
}
