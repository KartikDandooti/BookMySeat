package com.valtech.team3.bookmyseatbackend.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.valtech.team3.bookmyseatbackend.dao.FloorDAO;
import com.valtech.team3.bookmyseatbackend.dao.RestrictedSeatDAO;
import com.valtech.team3.bookmyseatbackend.dao.SeatDAO;
import com.valtech.team3.bookmyseatbackend.entities.Seat;

@Repository
public class SeatDAOImpl implements SeatDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private FloorDAO floorDAO;
	@Autowired
	private RestrictedSeatDAO restrictedSeatDAO;

	public class SeatRowMapper implements RowMapper<Seat> {

		@Override
		public Seat mapRow(ResultSet rs, int rowNum) throws SQLException {

			Seat seat = new Seat();
			seat.setId(rs.getInt("id"));
			seat.setSeatNumber(rs.getInt("seat_number"));
			seat.setAvailability(true);
			seat.setFloor(floorDAO.getFloorById(rs.getInt("floor_id")));
			seat.setRestrictedSeat(rs.getInt("restriction_id") != 0
					? restrictedSeatDAO.getRestrictedSeatById(rs.getInt("restriction_id"))
					: null);

			return seat;
		}
	}

	@Override
	public Seat getSeatById(int seatId) {
		String selectQuery = "SELECT * FROM SEAT WHERE ID = ?";

		return jdbcTemplate.queryForObject(selectQuery, new SeatRowMapper(), seatId);
	}

	@Override
	public List<Seat> getSeatsByFloor(int floorId) {
		String selectQuery = "SELECT * FROM SEAT WHERE FLOOR_ID = ?";

		return jdbcTemplate.query(selectQuery, new SeatRowMapper(), floorId);
	}

	@Override
	public List<Seat> findAvailableSeatsByFloorOnDate(int floorId, LocalDate startDate, LocalDate endDate) {
		String selectQuery = "SELECT * FROM SEAT S " +
                "WHERE S.FLOOR_ID = ? " +
                "AND S.ID NOT IN ( " +
                "    SELECT B.SEAT_ID " +
                "    FROM BOOKING B " +
                "    WHERE BOOKING_STATUS = TRUE AND ( " +
                "        (B.START_DATE <= ? AND B.END_DATE >= ?) " +
                "        OR (B.START_DATE BETWEEN ? AND ?) " +
                "        OR (B.END_DATE BETWEEN ? AND ?) " +
                "    ) " +
                ")";

		return jdbcTemplate.query(selectQuery, new SeatRowMapper(), floorId, startDate, endDate, startDate, endDate);
	}

	@Override
	public void updateSeatRestriction(int seatId, Integer restrictionId) {
		String updateQuery = "UPDATE SEAT SET RESTRICTION_ID = ? WHERE ID = ?";
		jdbcTemplate.update(updateQuery, restrictionId, seatId);
	}

	@Override
	public List<Seat> getAllReservedSeats() {
		String selectQuery = "SELECT * FROM SEAT WHERE RESTRICTION_ID IS NOT NULL";

		return jdbcTemplate.query(selectQuery, new SeatRowMapper());
	}

	@Override
	public List<Seat> getAllUserReservedSeats() {
		String selectQuery = "SELECT S.* FROM SEAT S JOIN RESTRICTED_SEAT RS ON S.RESTRICTION_ID=RS.ID WHERE S.RESTRICTION_ID IS NOT NULL AND RS.USER_ID IS NOT NULL";

		return jdbcTemplate.query(selectQuery, new SeatRowMapper());
	}

	@Override
	public List<Seat> getAllProjectReservedSeats() {
		String selectQuery = "SELECT S.* FROM SEAT S JOIN RESTRICTED_SEAT RS ON S.RESTRICTION_ID=RS.ID WHERE S.RESTRICTION_ID IS NOT NULL AND RS.PROJECT_ID IS NOT NULL";

		return jdbcTemplate.query(selectQuery, new SeatRowMapper());
	}

	@Override
	public void updateProjectReserveation(int restrictionId) {
		String updateQuery = "UPDATE SEAT SET RESTRICTION_ID = NULL WHERE RESTRICTION_ID = ?";
		jdbcTemplate.update(updateQuery, restrictionId);
	}
}