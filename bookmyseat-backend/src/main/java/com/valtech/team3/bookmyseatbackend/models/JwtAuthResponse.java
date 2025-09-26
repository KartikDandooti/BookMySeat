package com.valtech.team3.bookmyseatbackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JwtAuthResponse {
	private String accessToken;
	private String tokenType = "Bearer";
	private String role;
}