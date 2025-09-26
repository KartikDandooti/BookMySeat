package com.valtech.team3.bookmyseatbackend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class Seat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false)
	private int seatNumber;
	@Column(nullable = false, columnDefinition = "BIT DEFAULT false")
	private boolean availability;

	@ManyToOne(targetEntity = Floor.class, cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinColumn(name = "floorId", referencedColumnName = "id", nullable = false)
	private Floor floor;

	@OneToOne(targetEntity = RestrictedSeat.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "restrictionId", referencedColumnName = "id")
	private RestrictedSeat restrictedSeat;

	public Seat(int id, int seatNumber, Floor floor, boolean availability) {
		this.id = id;
		this.seatNumber = seatNumber;
		this.floor = floor;
		this.availability = availability;
	}

	public Seat(int id, int seatNumber, boolean availability) {
		this.id = id;
		this.seatNumber = seatNumber;
		this.availability = availability;
	}
}
