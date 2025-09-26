package com.valtech.team3.bookmyseatbackend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.valtech.team3.bookmyseatbackend.entities.Booking;
import com.valtech.team3.bookmyseatbackend.entities.Building;
import com.valtech.team3.bookmyseatbackend.entities.Floor;
import com.valtech.team3.bookmyseatbackend.entities.Holiday;
import com.valtech.team3.bookmyseatbackend.entities.Project;
import com.valtech.team3.bookmyseatbackend.entities.Seat;
import com.valtech.team3.bookmyseatbackend.entities.ShiftDetails;
import com.valtech.team3.bookmyseatbackend.entities.User;
import com.valtech.team3.bookmyseatbackend.models.AdminDashboardModel;
import com.valtech.team3.bookmyseatbackend.models.BookingModel;
import com.valtech.team3.bookmyseatbackend.models.BuildingModel;
import com.valtech.team3.bookmyseatbackend.models.HolidayModel;
import com.valtech.team3.bookmyseatbackend.models.ProjectModel;
import com.valtech.team3.bookmyseatbackend.models.RestrictedSeatModel;
import com.valtech.team3.bookmyseatbackend.models.SeatBookingResponse;
import com.valtech.team3.bookmyseatbackend.models.UserDisplayModel;
import com.valtech.team3.bookmyseatbackend.models.UserModel;
import com.valtech.team3.bookmyseatbackend.models.UserShiftModel;
import com.valtech.team3.bookmyseatbackend.models.UserStatusResponse;
import com.valtech.team3.bookmyseatbackend.service.BookingMappingService;
import com.valtech.team3.bookmyseatbackend.service.BookingService;
import com.valtech.team3.bookmyseatbackend.service.BuildingService;
import com.valtech.team3.bookmyseatbackend.service.EmailService;
import com.valtech.team3.bookmyseatbackend.service.FloorService;
import com.valtech.team3.bookmyseatbackend.service.HolidayService;
import com.valtech.team3.bookmyseatbackend.service.ProjectService;
import com.valtech.team3.bookmyseatbackend.service.RestrictedSeatService;
import com.valtech.team3.bookmyseatbackend.service.SeatService;
import com.valtech.team3.bookmyseatbackend.service.ShiftDetailsService;
import com.valtech.team3.bookmyseatbackend.service.UserService;
import com.valtech.team3.bookmyseatbackend.service.UserShiftsService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/bookmyseat/admin")
@CrossOrigin(origins = "*")
public class AdminController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private FloorService floorService;
	@Autowired
	private SeatService seatService;
	@Autowired
	private RestrictedSeatService restrictedSeatService;
	@Autowired
	private BookingService bookingService;
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private BookingMappingService bookingMappingService;
	@Autowired
	private ShiftDetailsService shiftDetailsService;
	@Autowired
	private UserShiftsService userShiftsService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private HolidayService holidayService;

	@GetMapping("/userDetails")
	public ResponseEntity<UserStatusResponse> userDetails() {
		LOGGER.info("Getting approved, pending, rejected users");
		UserStatusResponse userStatusResponse = new UserStatusResponse();
		userStatusResponse.setApprovedUsers(userService.getApprovedUsers());
		userStatusResponse.setPendingUser(userService.getPendingUsers());
		userStatusResponse.setRejectedUser(userService.getRejectedUsers());

		return ResponseEntity.status(HttpStatus.OK).body(userStatusResponse);
	}

	@PostMapping("/approveregistration")
	public ResponseEntity<String> approveUserRegistration(@RequestParam String email) {
		User user = userService.getUserByEmail(email);
		userService.approveUserRegistration(user);
		LOGGER.info("User registration approved!");

		return ResponseEntity.status(HttpStatus.OK).body("User registration approved");
	}

	@PostMapping("/rejectregistration")
	public ResponseEntity<String> rejectUserRegistration(@RequestParam String email) {
		User user = userService.getUserByEmail(email);
		userService.rejectUserRegistration(user);
		LOGGER.info("User registration rejected");

		return ResponseEntity.status(HttpStatus.OK).body("User registration rejected");
	}

	@PostMapping("/createProject")
	public ResponseEntity<String> saveNewProject(@RequestBody ProjectModel projectModel) {
		LOGGER.info("Creating new project");
		projectService.createNewProject(projectModel);

		return ResponseEntity.ok().body("Project Added Successfully");
	}

	@GetMapping("/projects")
	public ResponseEntity<List<Project>> getAllProjects() {
		LOGGER.info("Sending projects list");

		return ResponseEntity.ok().body(projectService.getAllProjects());
	}

	@PutMapping("/updateProject")
	public ResponseEntity<String> updateProject(@RequestBody ProjectModel projectModel) {
		LOGGER.info("updating the existing project");
		projectService.updateProject(projectModel);

		return ResponseEntity.ok().body("Project Updated Successfully");
	}

	@GetMapping("/seatrestriction/{floorId}")
	public ResponseEntity<List<Seat>> getAllSeatsByFloor(@PathVariable int floorId) {
		LOGGER.info("Sending all seats by floor Id to add restriction");

		return ResponseEntity.ok().body(seatService.getAllSeatsByFloor(floorId));
	}

	@PostMapping("/seatrestriction/add")
	public ResponseEntity<String> addSeatRestriction(@RequestBody RestrictedSeatModel restrictedSeatModel) {
		LOGGER.info("Adding restriction for seat");

		return ResponseEntity.ok().body(restrictedSeatService.createUserRestriction(restrictedSeatModel));
	}

	@GetMapping("/seatrestriction/remove")
	public ResponseEntity<Map<String, List<Seat>>> getAllReservedSeats() {
		LOGGER.info("Sending floors to add seat restriction");
		HashMap<String, List<Seat>> reservedSeats = new HashMap<>();
		reservedSeats.put("userReservedSeats", seatService.getAllUserReservedSeats());
		reservedSeats.put("projectReservedSeats", seatService.getAllProjectReservedSeats());

		return ResponseEntity.ok().body(reservedSeats);
	}

	@PostMapping("/seatrestriction/remove")
	public ResponseEntity<String> removeSeatRestriction(@RequestBody RestrictedSeatModel restrictedSeatModel) {
		LOGGER.info("Removing restriction on seat for user");

		return ResponseEntity.ok().body(restrictedSeatService.removeRestriction(restrictedSeatModel.getSeatId()));
	}

	@PostMapping("/seatrestriction/project/add")
	public ResponseEntity<String> addSeatRestrictionForProject(@RequestBody RestrictedSeatModel restrictedSeatModel) {
		LOGGER.info("Adding restriction to seat for project");

		return ResponseEntity.ok().body(restrictedSeatService.createProjectRestriction(restrictedSeatModel));
	}

	@PostMapping("/seatrestriction/project/remove")
	public ResponseEntity<String> removeSeatRestrictionForProject(
			@RequestBody RestrictedSeatModel restrictedSeatModel) {
		LOGGER.info("Removing restriction on seat for project");
		if (restrictedSeatModel.getProjectId() != 0) {
			return ResponseEntity.ok()
					.body(restrictedSeatService.removeProjectRestriction(restrictedSeatModel.getProjectId()));
		} else {
			return ResponseEntity.ok()
					.body(restrictedSeatService.removeProjectRestrictionForSeat(restrictedSeatModel.getSeatId()));
		}
	}

	@GetMapping("/dashboard")
	public ResponseEntity<AdminDashboardModel> getAdminDashboardData() {
		LOGGER.info("Sending required data for admin dashboard ");

		return new ResponseEntity<>(bookingMappingService.getAdminDashboardModel(), HttpStatus.OK);
	}

	@GetMapping("/floors")
	public ResponseEntity<List<Floor>> getFloors() {
		LOGGER.info("Retrieving All Floors From controller !");

		return ResponseEntity.status(HttpStatus.OK).body(floorService.getAllFloors());
	}

	@PostMapping("/seats")
	public ResponseEntity<SeatBookingResponse> getSeatsandPreference(@RequestBody BookingModel bookingModel) {
		SeatBookingResponse seatBookingResponse = new SeatBookingResponse();
		LOGGER.info("Floor Selected is: {}", bookingModel.getFloorId());
		seatBookingResponse.setAvailableSeats((seatService.getAvailableSeatsByFloorOnDate(bookingModel)));
		seatBookingResponse.setBookedSeats(bookingService.getBookingsforFloorBetweenDates(bookingModel));
		LOGGER.info("Sending Avaiable seats and booked seats by floor !");

		return ResponseEntity.status(HttpStatus.OK).body(seatBookingResponse);
	}

	@PostMapping("/updateseat")
	public ResponseEntity<String> updateSeat(@RequestBody BookingModel bookingModel) {
		LOGGER.info("Updating seat");
		bookingService.updateBookingByAdmin(bookingModel);
		LOGGER.info("Seat updated of booking id:{}", bookingModel.getId());

		return ResponseEntity.ok().body("Seat updated successfully");
	}

	@PutMapping("/cancel/{bookingId}")
	public ResponseEntity<String> cancelBooking(@PathVariable int bookingId) {
		bookingService.cancelBooking(bookingId);
		User user = userService.getUserByBookingId(bookingId);
		try {
			emailService.sendSeatCanceledMail(user);
		} catch (MessagingException e) {
			LOGGER.error("Error sending seat modification email to user", e);
		}
		LOGGER.info("Booking cancelled successfully of booking Id:{}", bookingId);

		return ResponseEntity.status(HttpStatus.OK)
				.body("Booking with ID " + bookingId + " The Booking has been Cancelled !");
	}

	@GetMapping("/users")
	public ResponseEntity<UserDisplayModel> listOfUsers() {
		LOGGER.info("Sending all users and projects");
		UserDisplayModel userDisplayModel = new UserDisplayModel();
		userDisplayModel.setUsers(userService.getAllUsers());
		userDisplayModel.setProjects(projectService.getAllProjects());

		return ResponseEntity.ok().body(userDisplayModel);
	}

	@PostMapping("/createUser")
	public ResponseEntity<String> createNewUser(@RequestBody UserModel userModel) {
		LOGGER.info("Creating a new user by admin");
		userService.createNewUser(userModel);

		return ResponseEntity.ok().body("User was created Succesfully !");
	}

	@PutMapping("/updateUser")
	public ResponseEntity<String> updateUserProject(@RequestBody UserModel userModel) {
		LOGGER.info("Updating user's Project");
		userService.updateUserProject(userModel);

		return ResponseEntity.ok().body("User's project Updated Successfully !");
	}

	@PostMapping("/userBookings/{userId}")
	public ResponseEntity<List<Booking>> listOfBookingsForaUser(@PathVariable int userId) {
		LOGGER.info("Getting all bookings of user");

		return ResponseEntity.ok().body(bookingService.getAllUserBookings(userId));
	}

	@GetMapping("/listofshiftdetails")
	public ResponseEntity<List<ShiftDetails>> listofShiftDetails() {
		LOGGER.info("Handling Request to fetch all the shifts");
		List<ShiftDetails> shiftDetails = shiftDetailsService.getAllShiftDetails();
		LOGGER.info("Sending all the shifts");

		return ResponseEntity.ok().body(shiftDetails);
	}

	@GetMapping("/usershifts/{userId}")
	public ResponseEntity<List<ShiftDetails>> listofUserShiftDetails(@PathVariable("userId") int userId) {
		LOGGER.info("Handling request to Send all shift details based on userId");

		return ResponseEntity.status(HttpStatus.OK).body(userShiftsService.getAllShiftDetailsByUserId(userId));
	}

	@PostMapping("/mapusershifts")
	public ResponseEntity<String> addShiftToUser(@RequestBody UserShiftModel userShiftModel) {
		LOGGER.info("Handling request to map user with the shift");
		userShiftsService.deleteShiftsForUser(userShiftModel.getUserId());
		for (int shiftId : userShiftModel.getShiftIds()) {
			userShiftsService.addShiftsToUser(userShiftModel.getUserId(), shiftId);
			LOGGER.debug("Adding shifts to user with userId: {} and shift with shiftId: {}", userShiftModel.getUserId(),
					shiftId);
		}

		LOGGER.info("User successfully mapped with the shifts");

		return ResponseEntity.ok("Added Shifts to User");
	}

	@PostMapping("/addbuilding")
	public ResponseEntity<String> addBuilding(@RequestBody BuildingModel building) {
		try {
			LOGGER.info("Adding new Building");
			buildingService.addBuilding(building.getBuildingName(), building.getLocationName());

			return ResponseEntity.ok().body("New Location Added");
		} catch (RuntimeException e) {

			return ResponseEntity.badRequest().body("Building name already exists");
		}
	}

	@GetMapping("/buildings")
	public ResponseEntity<List<Building>> getAllBuildings() {
		LOGGER.info("Getting all Buildings");

		return new ResponseEntity<>(buildingService.getAllBuildings(), HttpStatus.OK);
	}

	@GetMapping("/holidays")
	public ResponseEntity<List<Holiday>> getAllHolidays() {
		LOGGER.info("Getting all holidays");

		return new ResponseEntity<>(holidayService.getAllHolidays(), HttpStatus.OK);
	}

	@PostMapping("/createHoliday")
	public ResponseEntity<String> createHoliday(@RequestBody HolidayModel holiday) {
		LOGGER.info("Adding new holiday");
		holidayService.createNewHoliday(holiday);

		return ResponseEntity.ok("Holiday was created Successfully !");
	}

	@PutMapping("/updateHoliday")
	public ResponseEntity<String> updateHoliday(@RequestBody HolidayModel holiday) {
		LOGGER.info("Updating the existing holiday");
		holidayService.updateHoliday(holiday);

		return ResponseEntity.ok("Holiday was Updated Successfully !");
	}
}