package com.valtech.team3.bookmyseatbackend.entities;

import java.time.LocalDateTime;

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
public class PasswordToken {

	private int id;
	private String token;
	private LocalDateTime expirationdate;
	private User user;
}