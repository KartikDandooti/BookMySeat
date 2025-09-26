package com.valtech.team3.bookmyseatbackend.service.impl.tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.valtech.team3.bookmyseatbackend.dao.BuildingDAO;
import com.valtech.team3.bookmyseatbackend.entities.Building;
import com.valtech.team3.bookmyseatbackend.exceptions.DataBaseAccessException;
import com.valtech.team3.bookmyseatbackend.service.impl.BuildingServiceImpl;

@ExtendWith(MockitoExtension.class)
class BuildingServiceImplTest {

	@Mock
	private BuildingDAO buildingDAO;
	@InjectMocks
	private BuildingServiceImpl buildingService;

	@Test
	void testAddBuilding_Success() {
		String buildingName = "Mass Unique";
		String locationName = "Banglore";

		when(buildingDAO.isBuildingNameExists(buildingName)).thenReturn(false);

		doNothing().when(buildingDAO).createBuilding(buildingName, locationName);

		assertDoesNotThrow(() -> buildingService.addBuilding(buildingName, locationName));

		verify(buildingDAO).createBuilding(buildingName, locationName);
	}

	@Test
	void testAddBuilding_BuildingNameExists() {
		String buildingName = "Mass Unique";
		String locationName = "Banglore";

		when(buildingDAO.isBuildingNameExists(buildingName)).thenReturn(true);

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class,
				() -> buildingService.addBuilding(buildingName, locationName));

		assertEquals("Failed to add building !", exception.getMessage());

		verify(buildingDAO, never()).createBuilding(anyString(), anyString());
	}

	@Test
	void testAddBuilding_Failure() {
		String buildingName = "Mass Unique";
		String locationName = "Banglore";

		doThrow(new RuntimeException()).when(buildingDAO).createBuilding(buildingName, locationName);

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class,
				() -> buildingService.addBuilding(buildingName, locationName));

		assertEquals("Failed to add building !", exception.getMessage());
	}

	@Test
	void testGetAllBuildings_Success() {
		List<Building> buildings = new ArrayList<>();
		buildings.add(new Building("Mass Unique", "Banglore"));
		buildings.add(new Building("Forum grounds", "Banglore"));
		when(buildingDAO.getAllBuildings()).thenReturn(buildings);

		List<Building> result = buildingService.getAllBuildings();

		assertEquals(buildings, result);
	}

	@Test
	void testGetAllBuildings_Failure() {
		when(buildingDAO.getAllBuildings()).thenThrow(new RuntimeException());

		DataBaseAccessException exception = assertThrows(DataBaseAccessException.class,
				() -> buildingService.getAllBuildings());

		assertEquals("Failed to fetch all the buildings !", exception.getMessage());
	}
}