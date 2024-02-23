package com.valtech.bookmyseat.daoimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.valtech.bookmyseat.entity.Location;
import com.valtech.bookmyseat.exception.DataBaseAccessException;

@ExtendWith(MockitoExtension.class)
class LocationDAOImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	private LocationDaoImpl locationDAOImpl;

	@Test
	void testSave() throws DataBaseAccessException {
		Location location = new Location();
		location.setLocationName("Test Location");
		locationDAOImpl.save(location);
		verify(jdbcTemplate).update("INSERT INTO LOCATION (LOCATION_NAME) VALUES (?)", "Test Location");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testGetAllLocations() throws DataBaseAccessException {
		Location location = new Location();
		location.setLocationId(1);
		location.setLocationName("Location1");
		Location location1 = new Location();
		location1.setLocationId(2);
		location1.setLocationName("Location2");
		List<Location> expectedLocations = new ArrayList<>();
		expectedLocations.add(location);
		expectedLocations.add(location1);
		when(jdbcTemplate.query(eq("SELECT * FROM LOCATION"), any(BeanPropertyRowMapper.class)))
				.thenReturn(expectedLocations);
		List<Location> actualLocations = locationDAOImpl.getAllLocation();
		assertEquals(expectedLocations, actualLocations);
	}

	@Test
	void testDeleteLocation_Success() {
		when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(1);
		locationDAOImpl.deleteLocation(123);
		verify(jdbcTemplate).update("DELETE FROM LOCATION WHERE LOCATION_ID=?", 123);
		verifyNoMoreInteractions(jdbcTemplate);
	}

	@Test
	void testDeleteLocation_LocationNotFound() {
		when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(0);
		locationDAOImpl.deleteLocation(456);
		verify(jdbcTemplate).update("DELETE FROM LOCATION WHERE LOCATION_ID=?", 456);
		verifyNoMoreInteractions(jdbcTemplate);
	}

	@Test
	void testDeleteLocation_DatabaseException() {
		doThrow(new DataAccessException("Database error") {
			private static final long serialVersionUID = 1L;
		}).when(jdbcTemplate).update(anyString(), anyInt());
		assertThrows(DataAccessException.class, () -> locationDAOImpl.deleteLocation(555));
		verify(jdbcTemplate).update("DELETE FROM LOCATION WHERE LOCATION_ID=?", 555);
		verifyNoMoreInteractions(jdbcTemplate);
	}

	@Test
	void testUpdateLocation_Success() {
		when(jdbcTemplate.update(anyString(), anyString(), anyInt())).thenReturn(1);
		Location updatedLocation = new Location();
		updatedLocation.setLocationId(123);
		updatedLocation.setLocationName("New Location Name");
		locationDAOImpl.updateLocation(updatedLocation, 123);
		verify(jdbcTemplate).update("UPDATE LOCATION SET LOCATION_NAME = ? WHERE LOCATION_ID=?", "New Location Name",
				123);
		verifyNoMoreInteractions(jdbcTemplate);
	}

	@Test
	void testUpdateLocation_LocationNotFound() {
		when(jdbcTemplate.update(anyString(), anyString(), anyInt())).thenReturn(0);
		Location updatedLocation = new Location();
		updatedLocation.setLocationId(456);
		updatedLocation.setLocationName("Updated Name");
		locationDAOImpl.updateLocation(updatedLocation, 456);
		verify(jdbcTemplate).update("UPDATE LOCATION SET LOCATION_NAME = ? WHERE LOCATION_ID=?", "Updated Name", 456);
		verifyNoMoreInteractions(jdbcTemplate);
	}

	@Test
	void testUpdateLocation_DatabaseException() {
		doThrow(new DataAccessException("Database error") {
			private static final long serialVersionUID = 1L;
		}).when(jdbcTemplate).update(anyString(), anyString(), anyInt());
		Location updatedLocation = new Location();
		updatedLocation.setLocationId(555);
		updatedLocation.setLocationName("New Location");
		assertThrows(DataAccessException.class, () -> locationDAOImpl.updateLocation(updatedLocation, 555));
		verify(jdbcTemplate).update("UPDATE LOCATION SET LOCATION_NAME = ? WHERE LOCATION_ID=?", "New Location", 555);
		verifyNoMoreInteractions(jdbcTemplate);
	}
}
