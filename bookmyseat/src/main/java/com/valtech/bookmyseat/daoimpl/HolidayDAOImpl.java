package com.valtech.bookmyseat.daoimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.valtech.bookmyseat.dao.HolidayDAO;
import com.valtech.bookmyseat.entity.Holiday;

@Repository
public class HolidayDAOImpl implements HolidayDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(HolidayDAOImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Holiday> getAllHolidays() {
		LOGGER.info("query For Fetching holidays");
		String selectQuery = "SELECT * FROM HOLIDAY";

		return jdbcTemplate.query(selectQuery, new BeanPropertyRowMapper<>(Holiday.class));
	}

	@Override
	public void addHoliday(Holiday holiday) {
		String sql = "INSERT INTO HOLIDAY (holiday_name, holiday_date, holiday_month) VALUES (?, ?, ?)";
		LOGGER.info("query For adding holidays");
		jdbcTemplate.update(sql, holiday.getHolidayName(), holiday.getHolidayDate(), holiday.getHolidayMonth());
	}

	@Override
	public boolean deleteHoliday(int holidayId) {
		int rowsAffected = jdbcTemplate.update("DELETE FROM holiday WHERE id = ?", holidayId);
		if (rowsAffected > 0) {
			LOGGER.info("Deleted holiday details with ID {} from the database", holidayId);

			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Holiday> getHolidayByDate() {
		String selectQuery = "SELECT HOLIDAY_DATE FROM HOLIDAY";
		LOGGER.info("Get holidays By Date");

		return jdbcTemplate.query(selectQuery, new BeanPropertyRowMapper<>(Holiday.class));
	}
}