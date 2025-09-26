package com.valtech.team3.bookmyseatbackend.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.valtech.team3.bookmyseatbackend.dao.PasswordTokenDAO;
import com.valtech.team3.bookmyseatbackend.dao.UserDAO;
import com.valtech.team3.bookmyseatbackend.entities.PasswordToken;

@Repository
public class PasswordTokenDAOImpl implements PasswordTokenDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private UserDAO userDAO;

	public class PasswordTokenRowMapper implements RowMapper<PasswordToken> {

		@Override
		public PasswordToken mapRow(ResultSet rs, int rowNum) throws SQLException {
			PasswordToken passwordToken = new PasswordToken();
			passwordToken.setId(rs.getInt("id"));
			passwordToken.setToken(rs.getString("token"));
			passwordToken.setExpirationdate(rs.getTimestamp("expiration_date").toLocalDateTime());
			passwordToken.setUser(userDAO.getUserById(rs.getInt("user_id")));

			return passwordToken;
		}
	}

	@Override
	public void createPasswordToken(PasswordToken passwordToken) {
		String insertQuery = "INSERT INTO PASSWORD_TOKEN(TOKEN,EXPIRATION_DATE,USER_ID)" + "VALUES (?,?,?)";
		jdbcTemplate.update(insertQuery, passwordToken.getToken(), passwordToken.getExpirationdate(), passwordToken.getUser().getId());
	}

	@Override
	public PasswordToken getPasswordTokenByToken(String token) {
		String selectQuery = "SELECT * FROM PASSWORD_TOKEN WHERE TOKEN= ?";

		return jdbcTemplate.queryForObject(selectQuery, new PasswordTokenRowMapper(), token);
	}

	@Override
	public Boolean isPasswordTokenExists(int userId) {
		String selectQuery = "SELECT COUNT(*) FROM PASSWORD_TOKEN WHERE USER_ID = ?";
		Integer count = jdbcTemplate.queryForObject(selectQuery, Integer.class, userId);

		return Objects.requireNonNull(count) > 0;
	}

	@Override
	public void updatePasswordToken(int userId, String token, LocalDateTime expirationDate) {
		String updateQuery = "UPDATE PASSWORD_TOKEN SET TOKEN = ?,EXPIRATION_DATE=?  WHERE USER_ID = ?";
		jdbcTemplate.update(updateQuery, token, expirationDate, userId);
	}
}