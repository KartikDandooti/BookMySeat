package com.valtech.bookmyseat.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;

import com.valtech.bookmyseat.model.AdminDashBoardModel;

@ExtendWith(MockitoExtension.class)
class BookingMapperTest {

	@Test
	void testMapRow() throws SQLException {
		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.getDate("Date")).thenReturn(Date.valueOf(LocalDate.of(2023, 10, 15)));
		when(resultSet.getInt("seats_booked")).thenReturn(10);
		when(resultSet.getInt("total_lunch_booked")).thenReturn(20);
		when(resultSet.getInt("total_tea_booked")).thenReturn(30);
		when(resultSet.getInt("total_coffee_booked")).thenReturn(40);
		when(resultSet.getInt("total_parking_booked")).thenReturn(50);
		when(resultSet.getInt("total_two_wheeler_parking_booked")).thenReturn(60);
		when(resultSet.getInt("total_four_wheeler_parking_booked")).thenReturn(70);
		when(resultSet.getInt("total_desktop_booked")).thenReturn(80);
		RowMapper<AdminDashBoardModel> mapper = new AdminDashBoardMapper();
		AdminDashBoardModel result = mapper.mapRow(resultSet, 1);
		assertEquals(LocalDate.of(2023, 10, 15), result.getDate());
		assertEquals(10, result.getSeatsBooked());
		assertEquals(20, result.getTotalLunchBooked());
		assertEquals(30, result.getTotalTeaBooked());
		assertEquals(40, result.getTotalCoffeeBooked());
		assertEquals(50, result.getTotalParkingBooked());
		assertEquals(60, result.getTotalTwoWheelerParkingBooked());
		assertEquals(70, result.getTotalFourWheelerParkingBooked());
		assertEquals(80, result.getTotalDesktopBooked());
	}
}