package com.valtech.team3.bookmyseatbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Building {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false, length = 50)
	private String buildingName;
	@Column(nullable = false, length = 50)
	private String locationName;

	public Building(String buildingName, String locationName) {
		this.buildingName = buildingName;
		this.locationName = locationName;
	}
}