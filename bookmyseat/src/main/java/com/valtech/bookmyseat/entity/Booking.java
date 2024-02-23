package com.valtech.bookmyseat.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int bookingId;

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = false)
	private LocalDate endDate;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BookingType bookingType;

	@Column(nullable = false)
	private boolean bookingStatus;

	@ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@OneToOne(targetEntity = Seat.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "seatId", nullable = false)
	private Seat seat;

	@ManyToOne(targetEntity = Shift.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "shiftId", nullable = false)
	private Shift shift;

	@OneToMany(targetEntity = BookingMapping.class, cascade = CascadeType.ALL, mappedBy = "booking")
	private List<BookingMapping> bookingMapping;
}
