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
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BookingRange bookingRange;
	@Column(nullable = false, columnDefinition = "DATE")
	private LocalDate startDate;
	@Column(nullable = false, columnDefinition = "DATE")
	private LocalDate endDate;
	@Column(nullable = false, columnDefinition = "DATETIME")
	private LocalDateTime createdDate;
	@Column(nullable = false, columnDefinition = "BIT DEFAULT true")
	private boolean bookingStatus;
	private boolean allowCancel;
	@Column(nullable = false)
	private int verificationCode;
	private LocalDateTime modifiedDate;

	@ManyToOne(targetEntity = User.class, cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
	private User user;

	@ManyToOne(targetEntity = Seat.class, cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "seatId", referencedColumnName = "id", nullable = false)
	private Seat seat;

	private BookingMapping bookingMappings;

	public Booking(int id, LocalDate startDate, LocalDate endDate) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
	}
}