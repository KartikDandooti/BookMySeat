package com.valtech.team3.bookmyseatbackend.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.valtech.team3.bookmyseatbackend.exceptions.UserNotFoundException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Value("${app.jwt-secret}")
	private String jwtSecret;

	@Value("${app.jwt-expiration-milliseconds}")
	private long jwtExpirationDate;

	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
		String token = Jwts.builder().subject(username).issuedAt(new Date()).expiration(expireDate).signWith(key()).compact();
		LOGGER.info("Generated token: {}", token);
		LOGGER.info("Generated JWT authentication token");

		return token;
	}

	private Key key() {
		LOGGER.debug("Decoding JWT secret key");

		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	public String getUsername(String token) {
		LOGGER.info("Getting username from JWT token");

		return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token).getPayload().getSubject();
	}

	public Boolean validateToken(String token) {
		try {
			LOGGER.info("Validating Jwt token in request");
			Jwts.parser().verifyWith((SecretKey) key()).build().parse(token);

			return true;
		} catch (RuntimeException e) {
			throw new UserNotFoundException("Jwt token has Expired");
		}
	}
}