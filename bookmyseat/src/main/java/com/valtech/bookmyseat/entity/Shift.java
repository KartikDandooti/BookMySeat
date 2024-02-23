package com.valtech.bookmyseat.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Shift {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int shiftId;

	@Column(unique = true, nullable = false, length = 20)
	private String shiftName;

	@Column(nullable = false)
	private LocalTime startTime;

	@Column(nullable = false)
	private LocalTime endTime;

	@Column(nullable = false)
	private LocalDate createdDate;

	@Column(nullable = false)
	private LocalDate modifiedDate;

	@Column(nullable = false)
	private int createdBy;

	@Column(nullable = false)
	private int modifiedBy;

	@OneToMany(targetEntity = Booking.class, cascade = CascadeType.ALL, mappedBy = "shift")
	private List<Booking> booking;
}