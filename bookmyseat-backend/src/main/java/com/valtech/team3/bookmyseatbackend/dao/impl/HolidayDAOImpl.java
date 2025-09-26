package com.valtech.team3.bookmyseatbackend.dao.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.valtech.team3.bookmyseatbackend.dao.HolidayDAO;
import com.valtech.team3.bookmyseatbackend.entities.Holiday;
import com.valtech.team3.bookmyseatbackend.models.HolidayModel;

@Repository
public class HolidayDAOImpl implements HolidayDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Holiday> getAllHolidays() {
		String selectQuery = "SELECT * FROM HOLIDAY";

		return jdbcTemplate.query(selectQuery, new BeanPropertyRowMapper<>(Holiday.class));
	}

	@Override
	public void createHoliday(HolidayModel holiday) {
		String insertQuery = "INSERT INTO HOLIDAY (HOLIDAY_DATE, HOLIDAY_NAME) VALUES (?, ?)";
		jdbcTemplate.update(insertQuery, holiday.getHolidayDate(), holiday.getHolidayName());
	}

	@Override
	public void updateHoliday(HolidayModel holiday) {
		String updateQuery = "UPDATE HOLIDAY SET HOLIDAY_NAME = ?, HOLIDAY_DATE = ? WHERE ID = ?";
		jdbcTemplate.update(updateQuery, holiday.getHolidayName(), holiday.getHolidayDate(), holiday.getId());
	}

	@Override
	public Boolean isHolidayExists(LocalDate date, String holidayName) {
		String selectQuery = "SELECT COUNT(*) FROM HOLIDAY WHERE HOLIDAY_DATE = ? OR (HOLIDAY_NAME = ? AND HOLIDAY_DATE >= CURDATE())";
		int count = jdbcTemplate.queryForObject(selectQuery, Integer.class, date, holidayName);

		return count > 0;
	}

	@Override
	public List<Holiday> getHolidayByDate() {
		String selectQuery = "SELECT HOLIDAY_DATE FROM HOLIDAY";

		return jdbcTemplate.query(selectQuery, new BeanPropertyRowMapper<>(Holiday.class));
	}

	@Override
	public List<Holiday> getAllFutureHolidays() {
		String selectQuery = "SELECT * FROM HOLIDAY WHERE HOLIDAY_DATE >= CURDATE()";

		return jdbcTemplate.query(selectQuery, new BeanPropertyRowMapper<>(Holiday.class));
	}
}