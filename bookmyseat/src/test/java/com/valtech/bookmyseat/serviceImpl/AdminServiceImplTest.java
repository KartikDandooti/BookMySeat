package com.valtech.bookmyseat.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import com.valtech.bookmyseat.dao.AdminDAO;
import com.valtech.bookmyseat.dao.LocationDAO;
import com.valtech.bookmyseat.dao.ProjectDAO;
import com.valtech.bookmyseat.dao.UserDAO;
import com.valtech.bookmyseat.entity.Location;
import com.valtech.bookmyseat.entity.Project;
import com.valtech.bookmyseat.entity.Reserved;
import com.valtech.bookmyseat.entity.User;
import com.valtech.bookmyseat.exception.DataBaseAccessException;
import com.valtech.bookmyseat.exception.EmailException;
import com.valtech.bookmyseat.model.AdminDashBoardModel;
import com.valtech.bookmyseat.model.BookingDetailsOfUserForAdminReport;
import com.valtech.bookmyseat.model.ProjectModel;
import com.valtech.bookmyseat.model.UserRequestsModel;
import com.valtech.bookmyseat.service.EmailService;
import com.valtech.bookmyseat.serviceimpl.AdminServiceImpl;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

	@Mock
	private AdminDAO adminDAO;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private ProjectDAO projectDAO;

	@Mock
	private UserDAO userDAO;

	@Mock
	private EmailService emailService;

	@InjectMocks
	private AdminServiceImpl adminServiceImpl;

	@Mock
	private LocationDAO locationDAO;

	@Test
	void testFetchDailyBookingDetails_givenData() throws DataBaseAccessException {
		List<AdminDashBoardModel> mockData = new ArrayList<>();
		mockData.add(new AdminDashBoardModel(LocalDate.of(2024, 2, 7), 50, 30, 20, 10, 40, 20, 20, 5));
		when(adminDAO.fetchAdminDashboardDetails()).thenReturn(mockData);
		List<AdminDashBoardModel> result = adminServiceImpl.fetchAdminDashboardDetails();
		verify(adminDAO, times(1)).fetchAdminDashboardDetails();
		Assertions.assertEquals(mockData.size(), result.size());
	}

	@Test
	void testFetchDailyBookingDetails_EmptyResult() {
		when(adminDAO.fetchAdminDashboardDetails()).thenReturn(new ArrayList<>());
		DataBaseAccessException exception = Assertions.assertThrows(DataBaseAccessException.class,
				() -> adminServiceImpl.fetchAdminDashboardDetails());
		Assertions.assertEquals("Error occurred while fetching Daily Booking Details...", exception.getMessage());
		verify(adminDAO, times(1)).fetchAdminDashboardDetails();
	}

	@Test
	void testUpdateUserRequests_Success() throws DataBaseAccessException {
		List<UserRequestsModel> userRequestsModels = new ArrayList<>();
		UserRequestsModel model1 = new UserRequestsModel();
		model1.setUserID(5711);
		model1.setApprovalStatus("APPROVED");
		userRequestsModels.add(model1);
		when(adminDAO.updateUserRequests(any(), anyInt())).thenReturn(1);
		int totalRowsUpdated = adminServiceImpl.updateUserRequests(userRequestsModels, 5716);
		Assertions.assertEquals(1, totalRowsUpdated);
	}

	@Test
	void testUpdateUserRequests_ExceptionHandling() throws DataBaseAccessException {
		List<UserRequestsModel> userRequestsModels = new ArrayList<>();
		UserRequestsModel model1 = new UserRequestsModel();
		model1.setUserID(5711);
		model1.setApprovalStatus("APPROVED");
		userRequestsModels.add(model1);
		when(adminDAO.updateUserRequests(any(), anyInt())).thenReturn(0);
		Assertions.assertThrows(DataBaseAccessException.class,
				() -> adminServiceImpl.updateUserRequests(userRequestsModels, 5716));
	}

	@Test
	void testSendRejectionEmailToUser() throws EmailException {
		UserRequestsModel userRequestsModel = new UserRequestsModel();
		userRequestsModel.setUserID(123);
		userRequestsModel.setApprovalStatus("REJECTED");
		List<UserRequestsModel> userRequestsModels = new ArrayList<>();
		userRequestsModels.add(userRequestsModel);
		User user = new User();
		user.setUserId(123);
		when(userDAO.findUserByuserId(123)).thenReturn(user);
		when(adminDAO.updateUserRequests(any(User.class), anyInt())).thenReturn(1);
		doThrow(new RuntimeException("Error sending email")).when(emailService).sendRejectionEmailToUser(user);
		adminServiceImpl.updateUserRequests(userRequestsModels, 456);
		verify(emailService).sendRejectionEmailToUser(user);
	}

	@Test
	void testUpdateUserRequestsWith_NullParameter() {
		assertThrows(IllegalArgumentException.class, () -> adminServiceImpl.updateUserRequests(null, 0));
	}

	@Test
	void testGetAllLocations_Success() throws SQLException, DataBaseAccessException {
		List<Location> expectedLocations = new ArrayList<>();
		Location location = new Location();
		location.setLocationId(1);
		location.setLocationName("Location1");
		Location location1 = new Location();
		location1.setLocationId(2);
		location1.setLocationName("Location2");
		List<Location> actualLocations = adminServiceImpl.getAllLocations();
		assertEquals(expectedLocations, actualLocations);
	}

	@Test
	void testCreateNewProject() throws SQLException {
		ProjectModel projectModel = mock(ProjectModel.class);
		Project project = mock(Project.class);
		when(projectModel.getProjectName()).thenReturn("Test Project");
		when(projectModel.getProjectDetails()).thenReturn(project);
		adminServiceImpl.createNewProject(projectModel);
		verify(projectModel).getProjectName();
		verify(projectModel).getProjectDetails();
	}

	@Test
	void testGetAllProjects() throws SQLException {
		List<Project> mockProjects = new ArrayList<>();
		when(projectDAO.getAllProjects()).thenReturn(mockProjects);
		List<Project> result = adminServiceImpl.getAllProjects();
		assertEquals(mockProjects, result);
	}

	@Test
	void testDeleteProjectById() throws SQLException {
		int projectId = 123;
		adminServiceImpl.deleteProjectById(projectId);
		verify(projectDAO).deleteProjectById(projectId);
	}

	@Test
	void testUpdateProject() throws SQLException {
		int projectId = 456;
		ProjectModel projectModel = new ProjectModel();
		Project project = new Project();
		adminServiceImpl.updateProject(projectModel, projectId);
		assertNotNull(project);
	}

	@Test
	void testDeleteLocation() throws DataBaseAccessException {
		int locationId = 123;
		doNothing().when(locationDAO).deleteLocation(anyInt());
		adminServiceImpl.deleteLocation(locationId);
		verify(locationDAO, times(1)).deleteLocation(locationId);
	}

	@Test
	void testGetAllBookingDetails() {
		List<BookingDetailsOfUserForAdminReport> mockBookingDetailsList = new ArrayList<>();
		mockBookingDetailsList.add(new BookingDetailsOfUserForAdminReport());
		when(adminDAO.getAllBookingDetailsOfUserForAdminReport()).thenReturn(mockBookingDetailsList);
		List<BookingDetailsOfUserForAdminReport> result = adminServiceImpl.getAllBookingDetails();
		verify(adminDAO).getAllBookingDetailsOfUserForAdminReport();
		Assertions.assertEquals(mockBookingDetailsList.size(), result.size());
	}

	@Test
	void testReserveSeat() throws DataBaseAccessException {
		int userId = 1;
		int floorId = 2;
		int seatId = 3;
		adminServiceImpl.reserveSeat(userId, floorId, seatId);
		verify(adminDAO).reserveSeatInDB(userId, floorId, seatId);
	}

	@Test
	void testUpdateLocation() {
		Location updatedLocation = new Location();
		updatedLocation.setLocationName("Bangalore");
		int locationId = 1;
		adminServiceImpl.updateLocation(updatedLocation, locationId);
		verify(locationDAO).updateLocation(updatedLocation, locationId);
	}

	@Test
	void testReservedSeats() throws DataBaseAccessException {
		Reserved reserved1 = new Reserved();
		Reserved reserved2 = new Reserved();
		List<Reserved> expectedReservedSeats = new ArrayList<>();
		expectedReservedSeats.add(reserved1);
		expectedReservedSeats.add(reserved2);
		when(adminDAO.reserveSeat()).thenReturn(expectedReservedSeats);
		List<Reserved> result = adminServiceImpl.reservedSeats();
		assertEquals(expectedReservedSeats.size(), result.size());
		assertEquals(expectedReservedSeats.get(0), result.get(0));
		assertEquals(expectedReservedSeats.get(1), result.get(1));
	}

	@Test
	void testReservedSeats_WithException() {
		when(adminDAO.reserveSeat()).thenThrow(new DataBaseAccessException("Mocked exception"));
		assertThrows(DataBaseAccessException.class, () -> adminServiceImpl.reservedSeats());
	}

	@Test
	void testFetchUserRequests_Success() throws DataBaseAccessException {
		List<UserRequestsModel> expectedUserRequests = new ArrayList<>();
		expectedUserRequests.add(new UserRequestsModel());
		when(adminDAO.fetchUserRequests()).thenReturn(expectedUserRequests);
		List<UserRequestsModel> result = adminServiceImpl.fetchUserRequests();
		assertTrue(!result.isEmpty());
		assertEquals(expectedUserRequests, result);
	}

	@Test
	void testFetchUserRequests_Error() {
		when(adminDAO.fetchUserRequests()).thenReturn(new ArrayList<>());
		assertThrows(DataBaseAccessException.class, () -> adminServiceImpl.fetchUserRequests());
	}
}