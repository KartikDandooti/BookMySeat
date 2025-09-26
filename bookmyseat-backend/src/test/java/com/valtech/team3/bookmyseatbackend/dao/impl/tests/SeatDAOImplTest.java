package com.valtech.team3.bookmyseatbackend.dao.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.valtech.team3.bookmyseatbackend.dao.FloorDAO;
import com.valtech.team3.bookmyseatbackend.dao.RestrictedSeatDAO;
import com.valtech.team3.bookmyseatbackend.dao.impl.SeatDAOImpl;
import com.valtech.team3.bookmyseatbackend.entities.Building;
import com.valtech.team3.bookmyseatbackend.entities.Floor;
import com.valtech.team3.bookmyseatbackend.entities.Project;
import com.valtech.team3.bookmyseatbackend.entities.RestrictedSeat;
import com.valtech.team3.bookmyseatbackend.entities.Seat;
import com.valtech.team3.bookmyseatbackend.entities.User;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class SeatDAOImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;
	@Mock
	private FloorDAO floorDAO;
	@Mock
	private RestrictedSeatDAO restrictedSeatDAO;
	@InjectMocks
	private SeatDAOImpl seatDAO;

	@Test
	void testGetSeatById() {
		int seatId = 1;
		Seat expectedSeat = createSampleSeat();

		when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(seatId))).thenReturn(expectedSeat);

		Seat actualSeat = seatDAO.getSeatById(seatId);

		assertEquals(expectedSeat, actualSeat);
	}

	@Test
	void testGetSeatsByFloor() {
		int floorId = 1;
		Seat expectedSeat = createSampleSeat();

		when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(floorId))).thenReturn(Collections.singletonList(expectedSeat));

		List<Seat> actualSeats = seatDAO.getSeatsByFloor(floorId);

		assertEquals(Collections.singletonList(expectedSeat), actualSeats);
	}

	@Test
	void testSeatRowMap() throws SQLException {
		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.getInt("id")).thenReturn(1);
		when(resultSet.getInt("seat_number")).thenReturn(101);
		when(resultSet.getInt("floor_id")).thenReturn(2);
		when(resultSet.getInt("restriction_id")).thenReturn(0);

		Floor floor = new Floor();
		floor.setId(2);
		when(floorDAO.getFloorById(anyInt())).thenReturn(floor);

		SeatDAOImpl.SeatRowMapper rowMapper = seatDAO.new SeatRowMapper();
		Seat seat = rowMapper.mapRow(resultSet, 1);

		assertEquals(1, seat.getId());
		assertEquals(101, seat.getSeatNumber());
		assertEquals(floor, seat.getFloor());
		assertEquals(null, seat.getRestrictedSeat());
	}

	@Test
	void testSeatRowMapWithRestrictedSeat() throws SQLException {
		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.getInt("id")).thenReturn(1);
		when(resultSet.getInt("seat_number")).thenReturn(101);
		when(resultSet.getInt("floor_id")).thenReturn(2);
		when(resultSet.getInt("restriction_id")).thenReturn(3);

		Floor floor = new Floor();
		floor.setId(2);
		when(floorDAO.getFloorById(anyInt())).thenReturn(floor);

		RestrictedSeat restrictedSeat = new RestrictedSeat();
		restrictedSeat.setId(3);
		when(restrictedSeatDAO.getRestrictedSeatById(anyInt())).thenReturn(restrictedSeat);

		SeatDAOImpl.SeatRowMapper rowMapper = seatDAO.new SeatRowMapper();
		Seat seat = rowMapper.mapRow(resultSet, 1);

		assertEquals(1, seat.getId());
		assertEquals(101, seat.getSeatNumber());
		assertEquals(floor, seat.getFloor());
		assertEquals(restrictedSeat, seat.getRestrictedSeat());
	}

	private Seat createSampleSeat() {
		Seat seat = new Seat();
		Floor sampleFloor = createSampleFloor();
		RestrictedSeat sampleRestrictedSeat = createSampleRestrictedSeat();
		seat.setId(1);
		seat.setSeatNumber(101);
		seat.setAvailability(true);
		seat.setFloor(sampleFloor);
		seat.setRestrictedSeat(sampleRestrictedSeat);

		return seat;
	}

	public static RestrictedSeat createSampleRestrictedSeat() {
		RestrictedSeat restrictedSeat = new RestrictedSeat();
		restrictedSeat.setId(1);
		restrictedSeat.setCreatedDate(LocalDateTime.now().withNano(0));
		restrictedSeat.setModifiedDate(LocalDateTime.now().withNano(0));

		User user = new User();
		user.setId(1);
		user.setFirstName("sample_user");
		user.setPassword("password");
		restrictedSeat.setUser(user);

		Project project = new Project();
		project.setId(1);
		project.setProjectName("Sample Project");
		restrictedSeat.setProject(project);

		return restrictedSeat;
	}

	public static Floor createSampleFloor() {
		Floor floor = new Floor();
		floor.setId(1);
		floor.setFloorName("Sample Floor");

		Building building = new Building();
		building.setId(1);
		building.setBuildingName("Sample Building");
		floor.setBuilding(building);

		return floor;
	}

	@Test
	void testUpdateSeatRestriction() {
		int seatId = 1;
		int restrictionId = 10;

		seatDAO.updateSeatRestriction(seatId, restrictionId);

		verify(jdbcTemplate).update(anyString(), eq(restrictionId), eq(seatId));
	}

	@Test
	void testFindAvailableSeatsByFloorOnDate() {
		int floorId = 1;
		LocalDate startDate = LocalDate.of(2024, 1, 1);
		LocalDate endDate = LocalDate.of(2024, 1, 2);
		List<Seat> expectedSeats = Arrays.asList(new Seat(1, 101, true, new Floor(1, "Floor 1", null), null), new Seat(2, 102, true, new Floor(1, "Floor 1", null), null));

		when(jdbcTemplate.query(anyString(), any(RowMapper.class), any(), any(), any(), any(), any())).thenReturn(expectedSeats);

		List<Seat> actualSeats = seatDAO.findAvailableSeatsByFloorOnDate(floorId, startDate, endDate);

		assertEquals(expectedSeats.size(), actualSeats.size());
		assertTrue(actualSeats.containsAll(expectedSeats));
	}

	@Test
	void testGetAllReservedSeats() {
		Seat seat1 = new Seat();
		seat1.setId(1);
		Seat seat2 = new Seat();
		seat2.setId(2);
		List<Seat> expectedSeats = Arrays.asList(seat1, seat2);

		when(jdbcTemplate.query(ArgumentMatchers.anyString(), ArgumentMatchers.any(RowMapper.class))).thenReturn(expectedSeats);

		List<Seat> actualSeats = seatDAO.getAllReservedSeats();

		assertEquals(expectedSeats.size(), actualSeats.size());
		assertTrue(actualSeats.containsAll(expectedSeats));
	}

	@Test
	void testGetAllUserReservedSeats() {
		Seat seat1 = new Seat();
		seat1.setId(1);
		Seat seat2 = new Seat();
		seat2.setId(2);
		List<Seat> expectedSeats = Arrays.asList(seat1, seat2);

		when(jdbcTemplate.query(ArgumentMatchers.anyString(), ArgumentMatchers.any(RowMapper.class))).thenReturn(expectedSeats);

		List<Seat> actualSeats = seatDAO.getAllUserReservedSeats();

		assertEquals(expectedSeats.size(), actualSeats.size());
		assertTrue(actualSeats.containsAll(expectedSeats));
	}

	@Test
	void testGetAllProjectReservedSeats() {
		Seat seat1 = new Seat();
		seat1.setId(1);
		Seat seat2 = new Seat();
		seat2.setId(2);
		List<Seat> expectedSeats = Arrays.asList(seat1, seat2);

		when(jdbcTemplate.query(ArgumentMatchers.anyString(), ArgumentMatchers.any(RowMapper.class))).thenReturn(expectedSeats);

		List<Seat> actualSeats = seatDAO.getAllProjectReservedSeats();

		assertEquals(expectedSeats.size(), actualSeats.size());
		assertTrue(actualSeats.containsAll(expectedSeats));
	}

	@Test
	void testUpdateProjectReserveation() {
		int restrictionId = 5;
		String updateQuery = "UPDATE SEAT SET RESTRICTION_ID = NULL WHERE RESTRICTION_ID = ?";

		when(jdbcTemplate.update(eq(updateQuery), eq(restrictionId))).thenReturn(1);

		seatDAO.updateProjectReserveation(restrictionId);

		verify(jdbcTemplate).update(eq(updateQuery), eq(restrictionId));
	}
}