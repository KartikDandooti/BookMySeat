package com.valtech.bookmyseat.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Floor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int floorId;

	@Column(unique = true, nullable = false, length = 30)
	private String floorName;

	@ManyToOne(targetEntity = Location.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "locationId", nullable = false)
	private Location location;

	@OneToMany(targetEntity = Seat.class, cascade = CascadeType.ALL, mappedBy = "floor")
	private List<Seat> seat;

	@OneToMany(targetEntity = Restrain.class, cascade = CascadeType.ALL, mappedBy = "floor")
	private List<Restrain> restrain;
}