package com.valtech.bookmyseat.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class BookingMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private LocalDate bookingDate;

	@Column(nullable = false, columnDefinition = "DEFAULT false")
	private boolean lunch;

	@Column(nullable = false, columnDefinition = "DEFAULT false")
	private boolean teaCoffee;

	@Enumerated(EnumType.STRING)
	private TeaAndCoffee teaCoffeeType;

	@Column(nullable = false, columnDefinition = "DEFAULT false")
	private boolean parking;

	@Enumerated(EnumType.STRING)
	private ParkingType parkingType;

	@Column(nullable = false, columnDefinition = "DEFAULT false")
	private boolean additionalDesktop;

	@Column(nullable = false)
	private LocalDateTime modifiedDate;

	@Column(nullable = false, columnDefinition = "DEFAULT false")
	private boolean markedStatus;

	@ManyToOne(targetEntity = Booking.class, cascade = { CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "bookingId", nullable = false)
	private Booking booking;
}