package com.valtech.team3.bookmyseatbackend.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.valtech.team3.bookmyseatbackend.dao.BuildingDAO;
import com.valtech.team3.bookmyseatbackend.entities.Building;

@Repository
public class BuildingDAOImpl implements BuildingDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Building getBuildingById(int id) {
		String selectQuery = "SELECT * FROM BUILDING WHERE ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new BeanPropertyRowMapper<>(Building.class), id);
	}

	@Override
	public void createBuilding(String buildingName, String locationName) {
		String insertQuery = "INSERT INTO BUILDING (BUILDING_NAME, LOCATION_NAME) VALUES (?, ?)";
		jdbcTemplate.update(insertQuery, buildingName, locationName);
	}

	@Override
	public List<Building> getAllBuildings() {
		String selectQuery = "SELECT * FROM BUILDING";

		return jdbcTemplate.query(selectQuery, new BeanPropertyRowMapper<>(Building.class));
	}

	@Override
	public Boolean isBuildingNameExists(String buildingName) {
		String selectQuery = "SELECT COUNT(*) FROM BUILDING WHERE BUILDING_NAME = ?";
		int count = jdbcTemplate.queryForObject(selectQuery, Integer.class, buildingName);

		return count > 0;
	}
}