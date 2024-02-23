package com.valtech.bookmyseat.daoimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.valtech.bookmyseat.entity.Shift;

@ExtendWith(MockitoExtension.class)
class ShiftDaoImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	private ShiftServiceDaoImpl shiftServiceDao;
	private List<Shift> shiftList;

	@BeforeEach
	public void setup() {
		shiftList = new ArrayList<>();
		Shift shift1 = new Shift();
		shift1.setShiftId(1);
		shift1.setShiftName("Morning");
		shift1.setStartTime(LocalTime.parse("08:00:00"));
		shift1.setEndTime(LocalTime.parse("16:00:00"));
		Shift shift2 = new Shift();
		shift2.setShiftId(2);
		shift2.setShiftName("Evening");
		shift2.setStartTime(LocalTime.parse("16:00:00"));
		shift2.setEndTime(LocalTime.parse("00:00:00"));
		shiftList.add(shift1);
		shiftList.add(shift2);
	}

	@SuppressWarnings("unchecked")
	@Test
	void testGetAllShiftDetails() {
		List<Shift> ShiftList = Arrays.asList();
		when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class))).thenReturn(ShiftList);
		List<Shift> result = shiftServiceDao.getAllShiftDetails();
		verify(jdbcTemplate).query(eq("SELECT * FROM shift"), any(BeanPropertyRowMapper.class));
		assertEquals(ShiftList.size(), result.size());
	}

	@Test
	void testDeleteShiftDetailsById() {
		int shiftIdToDelete = 1;
		when(jdbcTemplate.update(anyString(), eq(shiftIdToDelete))).thenReturn(1);
		boolean result = shiftServiceDao.deleteShiftDetailsById(shiftIdToDelete);
		assertEquals(true, result);
	}

	@Test
	void testUpdateShiftTime() {
		Shift shiftToUpdate = new Shift();
		shiftToUpdate.setShiftId(1);
		shiftToUpdate.setShiftName("Morning");
		shiftToUpdate.setStartTime(LocalTime.of(7, 0));
		shiftToUpdate.setEndTime(LocalTime.of(15, 0));
		when(jdbcTemplate.update(anyString(), any(), any(), any(), anyInt())).thenReturn(1);
		shiftServiceDao.updateShiftTime(1, shiftToUpdate);
		verify(jdbcTemplate).update(anyString(), any(), any(), any(), anyInt());
	}

	@Test
	void testAddShiftTime() {
		Shift shiftToAdd = new Shift();
		shiftToAdd.setShiftName("Test Shift");
		shiftToAdd.setStartTime(LocalTime.of(8, 0));
		shiftToAdd.setEndTime(LocalTime.of(16, 0));
		when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(1);
		shiftServiceDao.addShiftTime(shiftToAdd);
		verify(jdbcTemplate).update(anyString(), eq("Test Shift"), eq(LocalTime.of(8, 0)), eq(LocalTime.of(16, 0)),
				eq(1), eq(1), any(LocalDateTime.class), any(LocalDateTime.class));
	}
}