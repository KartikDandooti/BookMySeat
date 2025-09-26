package com.valtech.team3.bookmyseatbackend.dao.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.valtech.team3.bookmyseatbackend.dao.impl.BuildingDAOImpl;
import com.valtech.team3.bookmyseatbackend.dao.impl.FloorDAOImpl;
import com.valtech.team3.bookmyseatbackend.dao.impl.FloorDAOImpl.FloorRowMapper;
import com.valtech.team3.bookmyseatbackend.entities.Building;
import com.valtech.team3.bookmyseatbackend.entities.Floor;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class FloorDAOImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;
	@Mock
	private BuildingDAOImpl buildingDAO;
	@Mock
	private ResultSet resultSet;
	@InjectMocks
	private FloorDAOImpl floorDAO;

	@Test
	void testGetAllFloors() {
		List<Floor> expectedFloors = Collections.singletonList(createSampleFloor());
		String selectQuery = "SELECT * FROM FLOOR";

		when(jdbcTemplate.query(eq(selectQuery), any(RowMapper.class))).thenReturn(expectedFloors);

		List<Floor> actualFloors = floorDAO.getAllFloors();

		assertEquals(expectedFloors, actualFloors);
	}

	@Test
	void testGetFloorById() {
		int floorId = 1;
		Floor expectedFloor = createSampleFloor();
		String selectQuery = "SELECT * FROM FLOOR WHERE ID = ?";

		when(jdbcTemplate.queryForObject(eq(selectQuery), any(RowMapper.class), eq(floorId))).thenReturn(expectedFloor);

		Floor actualFloor = floorDAO.getFloorById(floorId);

		assertEquals(expectedFloor, actualFloor);
	}

	private Floor createSampleFloor() {
		Floor floor = new Floor();
		floor.setId(1);
		floor.setFloorName("Mezzanine Floor");

		return floor;
	}

	@Test
	void testMapRow() throws SQLException {
		int id = 1;
		String floorName = "First Floor";
		int buildingId = 2;

		when(resultSet.getInt("id")).thenReturn(id);
		when(resultSet.getString("floor_name")).thenReturn(floorName);
		when(resultSet.getInt("building_id")).thenReturn(buildingId);

		Building mockBuilding = new Building();
		mockBuilding.setId(buildingId);
		when(buildingDAO.getBuildingById(buildingId)).thenReturn(mockBuilding);

		FloorRowMapper mapper = floorDAO.new FloorRowMapper();
		Floor floor = mapper.mapRow(resultSet, 1);

		assertEquals(id, floor.getId());
		assertEquals(floorName, floor.getFloorName());
		assertEquals(mockBuilding, floor.getBuilding());
	}
}