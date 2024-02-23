package com.valtech.bookmyseat.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.valtech.bookmyseat.serviceimpl.CustomUserDetailServiceImpl;

@Configuration
public class SecurityConfiguration {

	@Autowired
	private CustomUserDetailServiceImpl customUserDetailServiceImpl;

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter authenticationFilter,
			JwtAuthenticationEntryPoint authenticationEntryPoint) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(authorize -> {
			authorize
					.requestMatchers("/bookmyseat/login", "/bookmyseat/registration", "/bookmyseat/admin/createuser",
							"/bookmyseat/admin/location", "/bookmyseat/admin/user-seat-info")
					.permitAll()
					.requestMatchers(HttpMethod.PUT, "/bookmyseat/admin/update",
							"/bookmyseat/admin/updateShiftTime/{shiftId}",
							"/bookmyseat/admin/updateProject/{projectId}",
							"/bookmyseat/admin/updateLocation/{locationId}",
							"/bookmyseat/admin/updateSeat/{seatNumber}/{floorId}/{bookingId}",
							"/bookmyseat/admin/cancelBooking/{bookingId}", "/bookmyseat/user/changepassword",
							"/bookmyseat/user/attendanceApproval/{userId}", "/bookmyseat/user/reset-password/{userId}")
					.permitAll()
					.requestMatchers(HttpMethod.GET, "/bookmyseat/admin/getAllShiftDetails",
							"/bookmyseat/admin/projects", "/bookmyseat/admin/reports",
							"bookmyseat/admin/getAllUsers/{userId}",
							"/bookmyseat/admin/reserve", "/bookmyseat/admin/user-seat-info",

							"/bookmyseat/user/userdashboard/{userId}", "/bookmyseat/admin/allLocation",
							"/bookmyseat/user/userProfile", "/bookmyseat/admin/holidays", "/bookmyseat/admin/holidays",
							"/bookmyseat/admin/allLocation", "/bookmyseat/user/userProfile",
							"/bookmyseat/admin/adminProfile", "/bookmyseat/user/attendance")

					.permitAll()
					.requestMatchers(HttpMethod.POST, "/bookmyseat/admin/addShiftTime",
							"/bookmyseat/admin/createProject", "/bookmyseat/admin/reserve/{userId}/{floorId}/{seatId}",
							"bookmyseat/admin/getAllUsers/{userId}", "/bookmyseat/user/projectPrefrenced/floor/seat",
							"bookmyseat/admin/location", "/bookmyseat/admin/addHolidays",
							"/bookmyseat/user/forgot-password", "/bookmyseat/user/verify-otp/{userId}")
					.permitAll()
					.requestMatchers(HttpMethod.DELETE, "/bookmyseat/admin/deleteShiftDetails/{shiftId}",
							"bookmyseat/admin/project/{projectId}", "/bookmyseat/admin/deleteLocation/{locationId}",
							"/booking/user/cancelMyBooking/{userId}", "/bookmyseat/admin/deleteholidays/{holidayId}")
					.permitAll();
			authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
			authorize.anyRequest().authenticated();
		}).httpBasic(Customizer.withDefaults());
		http.exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint));
		http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailServiceImpl).passwordEncoder(passwordEncoder());
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}