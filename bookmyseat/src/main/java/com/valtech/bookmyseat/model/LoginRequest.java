package com.valtech.bookmyseat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {
	private String emailId;
	private String password;
}
