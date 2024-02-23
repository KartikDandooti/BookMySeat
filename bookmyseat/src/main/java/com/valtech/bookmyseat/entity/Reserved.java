package com.valtech.bookmyseat.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reserved {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int reservedId;

	@Column(nullable = false)
	private boolean reservedStatus;

	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@OneToOne(targetEntity = Seat.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "seatId", nullable = false)
	private Seat seat;
}
