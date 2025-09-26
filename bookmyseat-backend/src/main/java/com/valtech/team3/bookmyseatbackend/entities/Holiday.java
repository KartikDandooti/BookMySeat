package com.valtech.team3.bookmyseatbackend.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Holiday {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false, length = 30)
	private String holidayName;
	@Column(nullable = false, columnDefinition = "DATE")
	private LocalDate holidayDate;

	public Holiday(LocalDate holidayDate) {
		this.holidayDate = holidayDate;
	}

	public Holiday(int id, String holidayName, LocalDate holidayDate) {
		super();
		this.id = id;
		this.holidayName = holidayName;
		this.holidayDate = holidayDate;
	}
}