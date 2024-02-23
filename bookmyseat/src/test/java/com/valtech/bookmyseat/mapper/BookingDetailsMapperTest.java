package com.valtech.bookmyseat.mapper;

import java.sql.ResultSet;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingDetailsMapperTest {

	@Mock
	private ResultSet resultSet;

//	@InjectMocks
//	private BookingDetailsMapper bookingDetailsMapper;
//
//	@Test
//	void testMapRow() throws SQLException {
//		when(resultSet.getInt("user_id")).thenReturn(1);
//		when(resultSet.getString("user_name")).thenReturn("John Doe");
//		when(resultSet.getBoolean("parking_opted")).thenReturn(true);
//		when(resultSet.getString("parking_type")).thenReturn("TWO_WHEELER");
//		when(resultSet.getBoolean("lunch")).thenReturn(false);
//		when(resultSet.getBoolean("tea_coffee")).thenReturn(true);
//		when(resultSet.getString("tea_coffee_type")).thenReturn("COFFEE");
//		when(resultSet.getBoolean("additional_desktop")).thenReturn(true);
//		when(resultSet.getDate("start_date")).thenReturn(Date.valueOf("2024-02-20"));
//		when(resultSet.getDate("end_date")).thenReturn(Date.valueOf("2024-02-22"));
//		when(resultSet.getString("booking_type")).thenReturn("DAILY");
//		BookingModel bookingModel = bookingDetailsMapper.mapRow(resultSet, 1);
//		assertEquals(1, bookingModel.getUserId());
//		assertEquals("John Doe", bookingModel.getUserName());
//		assertEquals(true, bookingModel.getParkingOpted());
//		assertEquals("TWO_WHEELER", bookingModel.getParkingType());
//		assertEquals(false, bookingModel.getLunch());
//		assertEquals(true, bookingModel.getTeaCoffee());
//		assertEquals("COFFEE", bookingModel.getTeaCoffeeType());
//		assertEquals(true, bookingModel.getAdditionalDesktop());
//		assertEquals(java.sql.Date.valueOf("2024-02-20"), bookingModel.getStartDate());
//		assertEquals(java.sql.Date.valueOf("2024-02-22"), bookingModel.getEndDate());
//		assertEquals(BookingType.DAILY, bookingModel.getBookingType());
//	}
}
