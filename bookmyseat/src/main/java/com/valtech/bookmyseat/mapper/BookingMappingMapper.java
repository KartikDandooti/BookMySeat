package com.valtech.bookmyseat.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import org.springframework.jdbc.core.RowMapper;

import com.valtech.bookmyseat.entity.Booking;
import com.valtech.bookmyseat.entity.BookingMapping;
import com.valtech.bookmyseat.entity.ParkingType;
import com.valtech.bookmyseat.entity.TeaAndCoffee;

public class BookingMappingMapper implements RowMapper<BookingMapping> {

	@Override
	public BookingMapping mapRow(ResultSet rs, int rowNum) throws SQLException {
		BookingMapping bookingMapping = new BookingMapping();
		bookingMapping.setId(rs.getInt("id"));
		bookingMapping.setBookingDate(rs.getDate("booking_date").toLocalDate());
		bookingMapping.setAdditionalDesktop(rs.getBoolean("additional_desktop"));
		bookingMapping.setMarkedStatus(rs.getBoolean("marked_status"));
		bookingMapping.setLunch(rs.getBoolean("lunch"));
		bookingMapping.setTeaCoffee(rs.getBoolean("tea_coffee"));
		bookingMapping.setParking(rs.getBoolean("parking"));
		String parkingTypeString = rs.getString("parking_type");
		String teaCoffeTypeString = rs.getString("tea_coffe_type");
		if (Objects.nonNull(parkingTypeString)) {
			ParkingType parkingType = ParkingType.valueOf(parkingTypeString);
			bookingMapping.setParkingType(parkingType);
		}
		if (Objects.nonNull(teaCoffeTypeString)) {
			TeaAndCoffee teaCoffeeType = TeaAndCoffee.valueOf(teaCoffeTypeString);
			bookingMapping.setTeaCoffeeType(teaCoffeeType);
		}
		Booking booking = new Booking();
		booking.setBookingId(rs.getInt("booking_id"));
		booking.setStartDate(rs.getDate("start_date").toLocalDate());
		booking.setEndDate(rs.getDate("end_date").toLocalDate());
		bookingMapping.setBooking(booking);
		return bookingMapping;
	}
}
