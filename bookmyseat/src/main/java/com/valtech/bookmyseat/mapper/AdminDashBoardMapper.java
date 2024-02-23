package com.valtech.bookmyseat.mapper;

import com.valtech.bookmyseat.model.AdminDashBoardModel;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AdminDashBoardMapper implements RowMapper<AdminDashBoardModel> {
    @Override
    public AdminDashBoardModel mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        AdminDashBoardModel adminDashBoardModel = new AdminDashBoardModel();
        adminDashBoardModel.setDate(LocalDate.now());
        adminDashBoardModel.setSeatsBooked(resultSet.getInt("seats_booked"));
        adminDashBoardModel.setTotalLunchBooked(resultSet.getInt("total_lunch_booked"));
        adminDashBoardModel.setTotalTeaBooked(resultSet.getInt("total_tea_booked"));
        adminDashBoardModel.setTotalCoffeeBooked(resultSet.getInt("total_coffee_booked"));
        adminDashBoardModel.setTotalParkingBooked(resultSet.getInt("total_parking_booked"));
        adminDashBoardModel.setTotalTwoWheelerParkingBooked(resultSet.getInt("total_two_wheeler_parking_booked"));
        adminDashBoardModel.setTotalFourWheelerParkingBooked(resultSet.getInt("total_four_wheeler_parking_booked"));
        adminDashBoardModel.setTotalDesktopBooked(resultSet.getInt("total_desktop_booked"));

        return adminDashBoardModel;
    }
}
