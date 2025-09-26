package com.valtech.team3.bookmyseatbackend.service.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.valtech.team3.bookmyseatbackend.dao.FloorDAO;
import com.valtech.team3.bookmyseatbackend.entities.Building;
import com.valtech.team3.bookmyseatbackend.entities.Floor;
import com.valtech.team3.bookmyseatbackend.exceptions.DataBaseAccessException;
import com.valtech.team3.bookmyseatbackend.service.impl.FloorServiceImpl;

@ExtendWith(MockitoExtension.class)
class FloorServiceImplTest {

	@Mock
	private FloorDAO floorDAO;
	@InjectMocks
	private FloorServiceImpl floorService;

	@Test
	void testGetAllFloors() {
		Floor floor1 = new Floor(1, "First Floor", new Building());
		Floor floor2 = new Floor(2, "Second Floor", new Building());
		List<Floor> expectedFloors = Arrays.asList(floor1, floor2);

		when(floorDAO.getAllFloors()).thenReturn(expectedFloors);
		List<Floor> actualFloors = floorService.getAllFloors();

		assertEquals(expectedFloors, actualFloors);
	}

	@Test
	void testGetAllFloors_RuntimeException() {
		when(floorDAO.getAllFloors()).thenThrow(new RuntimeException("Simulated DAO exception"));

		assertThrows(DataBaseAccessException.class, () -> floorService.getAllFloors());
	}
}