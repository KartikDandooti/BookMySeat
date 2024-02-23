package com.valtech.bookmyseat.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Seat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int seatId;

	@Column(nullable = false)
	private int seatNumber;

	@ManyToOne(targetEntity = Floor.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "floorId", nullable = false)
	private Floor floor;

	@OneToOne(targetEntity = Booking.class, cascade = CascadeType.ALL, mappedBy = "seat")
	private Booking booking;

	@OneToOne(targetEntity = Reserved.class, cascade = CascadeType.ALL, mappedBy = "seat")
	private Reserved reserved;
	
	private boolean seatStatus;
}