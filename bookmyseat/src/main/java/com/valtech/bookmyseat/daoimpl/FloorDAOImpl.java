package com.valtech.bookmyseat.daoimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.valtech.bookmyseat.dao.FloorDAO;
import com.valtech.bookmyseat.entity.Floor;

@Repository
public class FloorDAOImpl implements FloorDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public class FloorRowMapper implements RowMapper<Floor> {

		@Override
		public Floor mapRow(ResultSet rs, int rowNum) throws SQLException {
			Floor floor = new Floor();
			floor.setFloorId(rs.getInt("floor_id"));
			floor.setFloorName(rs.getString("floor_name"));

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
		String selectQuery = "SELECT * FROM FLOOR WHERE floor_id = ?";

		return jdbcTemplate.queryForObject(selectQuery, new FloorRowMapper(), floorId);
	}
}
