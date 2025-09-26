package com.valtech.team3.bookmyseatbackend.dao.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.valtech.team3.bookmyseatbackend.dao.impl.BuildingDAOImpl;
import com.valtech.team3.bookmyseatbackend.entities.Building;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class BuildingDAOImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;
	@Mock
	private ResultSet resultSet;
	@InjectMocks
	private BuildingDAOImpl buildingDAO;

	@Test
	void testGetBuildingById() {
		int buildingId = 1;
		Building expectedBuilding = new Building();
		expectedBuilding.setId(buildingId);
		expectedBuilding.setBuildingName("Maas Unique");

		when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), eq(buildingId))).thenReturn(expectedBuilding);

		Building actualBuilding = buildingDAO.getBuildingById(buildingId);

		assertEquals(expectedBuilding, actualBuilding);
	}

	@Test
	void testCreateBuilding() {
		String buildingName = "New Office";
		String locationName = "London";

		doAnswer(invocation -> null).when(jdbcTemplate).update(anyString(), eq(buildingName), eq(locationName));

		buildingDAO.createBuilding(buildingName, locationName);

		verify(jdbcTemplate).update(eq("INSERT INTO BUILDING (BUILDING_NAME, LOCATION_NAME) VALUES (?, ?)"), eq(buildingName), eq(locationName));
	}

	@Test
	void testIsBuildingNameExists() {
		String buildingName = "Headquarters";

		when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(buildingName))).thenReturn(1);

		boolean exists = buildingDAO.isBuildingNameExists(buildingName);

		assertTrue(exists);
	}

	@Test
	void testGetAllBuildings_Multiple() throws SQLException {
		List<Building> expectedBuildings = Arrays.asList(new Building(1, "Headquarters", "New York"), new Building(2, "Branch Office", "Los Angeles"));

		Mockito.when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class))).thenReturn(expectedBuildings);

		List<Building> actualBuildings = buildingDAO.getAllBuildings();

		assertEquals(expectedBuildings.size(), actualBuildings.size());

		verify(jdbcTemplate).query(eq("SELECT * FROM BUILDING"), any(BeanPropertyRowMapper.class));
	}
}