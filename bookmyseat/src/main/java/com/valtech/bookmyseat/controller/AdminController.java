package com.valtech.bookmyseat.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valtech.bookmyseat.dao.LocationDAO;
import com.valtech.bookmyseat.entity.Holiday;
import com.valtech.bookmyseat.entity.Location;
import com.valtech.bookmyseat.entity.Project;
import com.valtech.bookmyseat.entity.Reserved;
import com.valtech.bookmyseat.entity.Shift;
import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.exception.DataBaseAccessException;
import com.valtech.bookmyseat.exception.DuplicateEmailException;
import com.valtech.bookmyseat.exception.EmailException;
import com.valtech.bookmyseat.model.AdminDashBoardModel;
import com.valtech.bookmyseat.model.BookingDetailsOfUserForAdminReport;
import com.valtech.bookmyseat.model.ProjectModel;
import com.valtech.bookmyseat.model.UserModel;
import com.valtech.bookmyseat.model.UserRequestsModel;
import com.valtech.bookmyseat.service.AdminService;
import com.valtech.bookmyseat.service.HolidayService;
import com.valtech.bookmyseat.service.ShiftDetailsService;
import com.valtech.bookmyseat.service.UserService;

import io.jsonwebtoken.lang.Collections;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/bookmyseat/admin")
public class AdminController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private AdminService adminService;

	@Autowired
	public UserService userService;

	@Autowired
	private LocationDAO locationDAO;

	@Autowired
	private HolidayService holidayService;

	@Autowired
	private ShiftDetailsService shiftDetailsService;

	@GetMapping("/dashboard")
	public ResponseEntity<List<AdminDashBoardModel>> getAdminDashboardDetails() throws DataBaseAccessException {
		LOGGER.info("Handling the request for Admin Dashboard");
		List<AdminDashBoardModel> dashboardDetails = adminService.fetchAdminDashboardDetails();

		return ResponseEntity.status(HttpStatus.OK).body(dashboardDetails);
	}

	@GetMapping("/requests")
	public ResponseEntity<List<UserRequestsModel>> getUsersPendingRequests() {
		LOGGER.info("Handling the request for Pending Requests");
		List<UserRequestsModel> pendingRequests = adminService.fetchUserRequests();

		return ResponseEntity.ok(pendingRequests);
	}

	@PutMapping("/request/update")
	public ResponseEntity<Integer> updateUserRequests(@RequestBody List<UserRequestsModel> userRequestsModels,
			@AuthenticationPrincipal UserDetails userDetails) {
		LOGGER.info("Handling the request for Updating Multiple Requests");
		User user = userService.findUserByEmail(userDetails.getUsername());
		int updatedCount = adminService.updateUserRequests(userRequestsModels, user.getUserId());

		return ResponseEntity.ok(updatedCount);
	}

	@PostMapping("/createuser")
	public ResponseEntity<String> createUser(@RequestBody UserModel userModel) throws EmailException {
		try {
			userService.createUser(userModel);

			return ResponseEntity.ok("User created successfully");
		} catch (DuplicateEmailException e) {
			LOGGER.error("handling the DuplicateEmailException:{}", e.getMessage());

			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateUserProfileByAdmin(@RequestBody UserModel userModel) {
		userService.updateUserProfileByAdmin(userModel);

		return ResponseEntity.ok("User profile updated successfully");
	}

	@PostMapping("/location")
	public ResponseEntity<String> saveLocation(@RequestBody Location location) {
		locationDAO.save(location);
		LOGGER.info("saving the location added by admin in the databse");

		return ResponseEntity.ok().body("Location added successfully");
	}

	@GetMapping("/user-seat-info")
	public List<Map<String, Object>> getUserSeatInfo() throws DataBaseAccessException {
		LOGGER.info("handling the request for seat-info");

		return userService.getUserSeatInfo();
	}

	@PostMapping("/createProject")
	public ResponseEntity<String> saveNewProject(@RequestBody ProjectModel projectModel) {
		try {
			adminService.createNewProject(projectModel);

			return ResponseEntity.ok().body("Project Added Successfully");
		} catch (SQLException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/projects")
	public ResponseEntity<List<Project>> getAllProjects() {
		try {
			return ResponseEntity.ok().body(adminService.getAllProjects());
		} catch (SQLException e) {
			LOGGER.error("Error occurred while retrieving projects from database", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
		}
	}

	@DeleteMapping("/project/{projectId}")
	public ResponseEntity<String> deleteSelectedProject(@PathVariable int projectId) {
		try {
			adminService.deleteProjectById(projectId);

			return ResponseEntity.ok().body("Project Deleted Successfully");
		} catch (SQLException e) {
			LOGGER.error("Could not delete project with Id {}", projectId);

			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/deleteShiftDetails/{shiftId}")
	public String deleteShiftDetails(@PathVariable("shiftId") int shiftId) {
		LOGGER.info("Handling request for deleting shift");

		return shiftDetailsService.deleteShift(shiftId);
	}

	@PutMapping("/updateProject/{projectId}")
	public ResponseEntity<String> updateProject(@PathVariable int projectId, @RequestBody ProjectModel projectModel) {
		try {
			adminService.updateProject(projectModel, projectId);

			return ResponseEntity.ok().body("Project Updated Successfully");
		} catch (SQLException e) {
			LOGGER.error("Could not update project with Id {}", projectId);

			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/addShiftTime")
	public String addShiftTime(@RequestBody Shift shift) {
		LOGGER.info("Handling request for adding shift");

		return shiftDetailsService.addShiftTime(shift);
	}

	@GetMapping("/getAllShiftDetails")
	public List<Shift> getAllShiftDetails() {
		LOGGER.info("Handling request to fetch allshifts");

		return shiftDetailsService.getAllShiftDetails();
	}

	@PutMapping("/updateShiftTime/{shiftId}")
	public String updateShiftTime(@PathVariable int shiftId, @RequestBody Shift shift) {
		LOGGER.info("Handling request for updating shift");

		return shiftDetailsService.updateShiftTime(shiftId, shift);
	}

	@GetMapping("/allLocation")
	public ResponseEntity<List<Location>> getAllLocation() {
		try {
			LOGGER.info("location fetched successfully");

			return ResponseEntity.ok().body(adminService.getAllLocations());
		} catch (SQLException e) {
			LOGGER.error("Error occurred while retrieving locations from database: {}", e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
		}
	}

	@DeleteMapping("deleteLocation/{locationId}")
	ResponseEntity<String> deleteLocation(@PathVariable int locationId) {
		try {
			LOGGER.info("location deleted successfully");
			adminService.deleteLocation(locationId);

			return ResponseEntity.ok().body("Locations Deleted Successfully");
		} catch (DataBaseAccessException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("updateLocation/{locationId}")
	public ResponseEntity<String> updateLocation(@PathVariable int locationId, @RequestBody Location locationModel) {
		try {
			LOGGER.info("location updated successfully");
			adminService.updateLocation(locationModel, locationId);

			return ResponseEntity.ok().body("Location Updated Successfully");
		} catch (DataBaseAccessException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/reports")
	public List<BookingDetailsOfUserForAdminReport> getAllBookingDetails() {
		LOGGER.info("handling request to fetch all booking details");

		return adminService.getAllBookingDetailsOfUserForAdminReport();
	}

	@GetMapping("/getAllUsers/{userId}")
	public List<User> getUserDetailsById(@PathVariable int userId) {
		return userService.getAllUser(userId);
	}

	@GetMapping("/reserve")
	public List<Reserved> reserveSeat() {
		return adminService.reservedSeats();
	}

	@PostMapping("/reserve/{userId}/{floorId}/{seatId}")
	public ResponseEntity<String> reserveSeats(@PathVariable("userId") int userId, @PathVariable("floorId") int floorId,
			@PathVariable("seatId") int seatId) {
		adminService.reserveSeat(userId, floorId, seatId);

		return ResponseEntity.ok().body("Seat Reserved Successfully");
	}

	@PutMapping("/updateSeat/{seatNumber}/{floorId}/{bookingId}")
	public ResponseEntity<String> updateUserSeat(@PathVariable("seatNumber") int seatNumber,
			@PathVariable("floorId") int floorId, @PathVariable("bookingId") int bookingId) throws EmailException {
		LOGGER.info("handling request to update user seat");
		userService.updateUserSeat(seatNumber, floorId, bookingId);

		return ResponseEntity.status(HttpStatus.OK).body("User seat updated successfully.");
	}

	@PutMapping("/cancelBooking/{bookingId}")
	public ResponseEntity<String> cancelBooking(@PathVariable("bookingId") int bookingId,@AuthenticationPrincipal UserDetails userDetails) throws  EmailException {
		LOGGER.info("Seat Canceled Successfully");
		User user=userService.findUserByEmail(userDetails.getUsername());
		
		userService.cancelUserSeat(bookingId,user.getUserId());

		return ResponseEntity.status(HttpStatus.OK).body("User Booking cancel successfully.");
	}

	@GetMapping("/holidays")
	public ResponseEntity<List<Holiday>> getAllHolidays() {
		return ResponseEntity.ok().body(holidayService.getAllHolidays());
	}

	@PostMapping("/addHolidays")
	public String addHoliday(@RequestBody Holiday holiday) {
		LOGGER.info("Handling request for adding holiday");

		return holidayService.addHolidayByAdmin(holiday);
	}

	@DeleteMapping("/deleteholidays/{holidayId}")
	public String deleteHoildayDetails(@PathVariable("holidayId") int holidayId) {
		LOGGER.info("Handling request for deleting Holiday");

		return holidayService.deleteHoliday(holidayId);
	}

	@GetMapping("/admin/holidays")

	public ResponseEntity<List<Holiday>> getHolidayByDate() {
		LOGGER.info("Handling request for getting List of  Holiday");

		return ResponseEntity.ok().body(holidayService.getHoildayBydate());
	}

	@GetMapping("/adminProfile")
	public ResponseEntity<User> adminProfile(@AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.findUserByEmail(userDetails.getUsername());

		return ResponseEntity.ok(user);
	}
}