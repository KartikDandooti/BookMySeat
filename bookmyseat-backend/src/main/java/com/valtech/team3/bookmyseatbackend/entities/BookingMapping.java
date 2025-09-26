package com.valtech.team3.bookmyseatbackend.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class BookingMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false, columnDefinition = "DATE")
	private LocalDate bookedDate;
	@Column(nullable = false, columnDefinition = "BIT DEFAULT false")
	private boolean attendance;
	@Column(nullable = false, columnDefinition = "BIT DEFAULT false")
	private boolean lunch;
	@Column(nullable = false, columnDefinition = "BIT DEFAULT false")
	private boolean beverage;
	@Column(nullable = false, columnDefinition = "BIT DEFAULT false")
	private boolean parking;
	@Enumerated(EnumType.STRING)
	private VehicleType vehicleType;
	@Column(nullable = false, columnDefinition = "BIT DEFAULT false")
	private boolean additionalDesktop;
	@Column(columnDefinition = "DATETIME")
	private LocalDateTime modifiedDate;

	@ManyToOne(targetEntity = Booking.class, cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "bookingId", referencedColumnName = "id", nullable = false)
	private Booking booking;

	@ManyToOne(targetEntity = ShiftDetails.class, cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "shiftId", referencedColumnName = "id", nullable = false)
	private ShiftDetails shiftDetails;

	public BookingMapping(int id, Booking booking, LocalDate bookedDate, boolean attendance) {
		this.id = id;
		this.booking = booking;
		this.bookedDate = bookedDate;
		this.attendance = attendance;
	}
}