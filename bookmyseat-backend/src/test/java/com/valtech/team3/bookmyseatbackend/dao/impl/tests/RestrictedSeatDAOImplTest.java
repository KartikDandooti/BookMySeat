package com.valtech.team3.bookmyseatbackend.dao.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.valtech.team3.bookmyseatbackend.dao.ProjectDAO;
import com.valtech.team3.bookmyseatbackend.dao.UserDAO;
import com.valtech.team3.bookmyseatbackend.dao.impl.RestrictedSeatDAOImpl;
import com.valtech.team3.bookmyseatbackend.entities.RestrictedSeat;
import com.valtech.team3.bookmyseatbackend.models.RestrictedSeatModel;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class RestrictedSeatDAOImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;
	@Mock
	private ResultSet resultSet;
	@Mock
	private UserDAO userDAO;
	@Mock
	private RestrictedSeatModel restrictedSeatModel;
	@Mock
	private ProjectDAO projectDAO;
	@InjectMocks
	private RestrictedSeatDAOImpl restrictedSeatDAO;

	@Test
	void testGetRestrictedSeatById() {
		int restrictedSeatId = 1;
		RestrictedSeat expectedRestrictedSeat = createSampleRestrictedSeat();
		String selectQuery = "SELECT * FROM RESTRICTED_SEAT WHERE ID = ?";

		when(jdbcTemplate.queryForObject(eq(selectQuery), any(RowMapper.class), eq(restrictedSeatId))).thenReturn(expectedRestrictedSeat);

		RestrictedSeat actualRestrictedSeat = restrictedSeatDAO.getRestrictedSeatById(restrictedSeatId);

		assertEquals(expectedRestrictedSeat, actualRestrictedSeat);
	}

	@Test
	void testRestrictedSeatRowMapper() throws SQLException {
		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.getInt("id")).thenReturn(1);
		when(resultSet.getInt("user_id")).thenReturn(1);
		when(resultSet.getInt("project_id")).thenReturn(1);

		RestrictedSeatDAOImpl.RestrictedSeatRowMapper rowMapper = restrictedSeatDAO.new RestrictedSeatRowMapper();

		when(userDAO.getUserById(1)).thenReturn(null);
		when(projectDAO.getProjectById(1)).thenReturn(null);

		RestrictedSeat restrictedSeat = rowMapper.mapRow(resultSet, 1);

		assertNotNull(restrictedSeat);
		assertEquals(1, restrictedSeat.getId());
		assertNull(restrictedSeat.getUser());
		assertNull(restrictedSeat.getProject());
	}

	private RestrictedSeat createSampleRestrictedSeat() {
		RestrictedSeat restrictedSeat = new RestrictedSeat();
		restrictedSeat.setId(1);

		return restrictedSeat;
	}

	@Test
	void testCreateRestriction() {
		int userId = 1;
		int expectedRestrictionId = 2;

		when(jdbcTemplate.update(anyString(), anyInt(), any(LocalDateTime.class))).thenReturn(1);
		when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(expectedRestrictionId);

		int restrictionId = restrictedSeatDAO.createUserRestriction(userId);

		assertEquals(expectedRestrictionId, restrictionId);
		verify(jdbcTemplate, times(1)).update(anyString(), eq(userId), any(LocalDateTime.class));
		verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class));
		verifyNoMoreInteractions(jdbcTemplate);
	}

	@Test
	void testRemoveRestriction() {
		int restrictionId = 1;
		when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(1);

		restrictedSeatDAO.removeRestriction(restrictionId);

		verify(jdbcTemplate).update("DELETE FROM RESTRICTED_SEAT WHERE ID = ?", restrictionId);
	}

	@Test
	void testIsExistsRestrictionForUserTrue() {
		int userId = 1;
		int expectedCount = 1;

		when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(userId))).thenReturn(expectedCount);

		boolean hasRestriction = restrictedSeatDAO.isExistsRestrictionForUser(userId);

		assertTrue(hasRestriction);
		verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), eq(userId));
		verifyNoMoreInteractions(jdbcTemplate);
	}

	@Test
	void testIsExistsRestrictionForprojectTrue() {
		int projectId = 1;
		int expectedCount = 1;

		when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(projectId))).thenReturn(expectedCount);

		boolean hasRestriction = restrictedSeatDAO.isExistsRestrictionForProject(projectId);

		assertTrue(hasRestriction);
		verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), eq(projectId));
		verifyNoMoreInteractions(jdbcTemplate);
	}

	@Test
	void testCreateProjectRestriction() {
		int projectId = 2;
		String insertQuery = "INSERT INTO RESTRICTED_SEAT (PROJECT_ID,CREATED_DATE) VALUES (?,?)";

		when(jdbcTemplate.update(eq(insertQuery), eq(projectId), any(LocalDateTime.class))).thenReturn(1);
		when(jdbcTemplate.queryForObject(anyString(), any(Class.class))).thenReturn(10);

		int id = restrictedSeatDAO.createProjectRestriction(projectId);

		assertEquals(10, id);
		verify(jdbcTemplate).update(eq(insertQuery), eq(projectId), any(LocalDateTime.class));
		verify(jdbcTemplate).queryForObject(anyString(), eq(Integer.class));
	}

	@Test
	void testGetRestrictedSeatByProjectId_Found() {
		int projectId = 1;
		RestrictedSeat expectedRestrictedSeat = createSampleRestrictedSeat();
		String selectQuery = "SELECT * FROM RESTRICTED_SEAT WHERE PROJECT_ID = ?";

		when(jdbcTemplate.queryForObject(eq(selectQuery), any(RowMapper.class), eq(projectId))).thenReturn(expectedRestrictedSeat);

		RestrictedSeat actualRestrictedSeat = restrictedSeatDAO.getRestrictedSeatByProjectId(projectId);

		assertEquals(expectedRestrictedSeat, actualRestrictedSeat);
	}
}