package com.valtech.team3.bookmyseatbackend.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.valtech.team3.bookmyseatbackend.security.JwtAuthenticationEntryPoint;
import com.valtech.team3.bookmyseatbackend.security.JwtAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

	@Bean
	private static PasswordEncoder passwordEncoder() {
		LOGGER.info("Encrypting Password !");

		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter authenticationFilter) throws Exception {
		LOGGER.info("Filtering requests based on User !");

		http.csrf(csrf -> csrf.disable())
		      .authorizeHttpRequests(request -> request.requestMatchers("/bookmyseat/login", "/bookmyseat/registration", "/bookmyseat/resetpassword/{token}", "/bookmyseat/forgotpassword").permitAll()
		            .anyRequest().authenticated())
		      .httpBasic(Customizer.withDefaults()).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		      .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint)).addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		LOGGER.info("Authenticating User !");

		return configuration.getAuthenticationManager();
	}
}