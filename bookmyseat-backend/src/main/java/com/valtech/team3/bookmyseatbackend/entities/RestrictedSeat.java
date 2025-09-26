package com.valtech.team3.bookmyseatbackend.entities;

import java.time.LocalDateTime;

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
public class RestrictedSeat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false, columnDefinition = "DATETIME")
	private LocalDateTime createdDate;
	@Column(columnDefinition = "DATETIME")
	private LocalDateTime modifiedDate;

	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", referencedColumnName = "id")
	private User user;

	@ManyToOne(targetEntity = Project.class, cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "projectId", referencedColumnName = "id")
	private Project project;

	public RestrictedSeat(int id, User user, Project project, LocalDateTime createdDate) {
		this.id = id;
		this.user = user;
		this.project = project;
		this.createdDate = createdDate;
	}
}