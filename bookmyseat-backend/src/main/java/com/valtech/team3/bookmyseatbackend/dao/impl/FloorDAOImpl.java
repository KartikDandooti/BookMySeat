package com.valtech.team3.bookmyseatbackend.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.valtech.team3.bookmyseatbackend.dao.BuildingDAO;
import com.valtech.team3.bookmyseatbackend.dao.FloorDAO;
import com.valtech.team3.bookmyseatbackend.entities.Floor;

@Repository
public class FloorDAOImpl implements FloorDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private BuildingDAO buildingDAO;

	public class FloorRowMapper implements RowMapper<Floor> {

		@Override
		public Floor mapRow(ResultSet rs, int rowNum) throws SQLException {
			Floor floor = new Floor();
			floor.setId(rs.getInt("id"));
			floor.setFloorName(rs.getString("floor_name"));
			floor.setBuilding(buildingDAO.getBuildingById(rs.getInt("building_id")));

			return floor;
		}
	}

	@Override
	public List<Floor> getAllFloors() {
		String selectQuery = "SELECT * FROM FLOOR";

		return jdbcTemplate.query(selectQuery, new FloorRowMapper());
	}

	@Override
	public Floor getFloorById(int floorId) {
		String selectQuery = "SELECT * FROM FLOOR WHERE ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new FloorRowMapper(), floorId);
	}
}