package com.shanepaulus.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.ArrayList;
import java.util.Date;

import com.shanepaulus.challenge.service.impl.JwtServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Shane Paulus
 * <p>
 * Date Created : 25-Aug-2021.
 */

public class JwtServiceTest {

	private final String USER_NAME = "paulie";
	private final String PASSWORD = "password";

	@InjectMocks
	private JwtServiceImpl jwtService;

	@BeforeEach
	public void before() {
		openMocks(this);
	}

	@Test
	public void testExtractUsername() {
		UserDetails userDetails = new User(USER_NAME, PASSWORD, new ArrayList<>());
		String token = this.jwtService.generateToken(userDetails);
		assertNotNull(token);
		assertEquals(USER_NAME, this.jwtService.extractUserName(token));
	}

	@Test
	public void testExtractExpiration() {
		Date currentDate = new Date();
		UserDetails userDetails = new User(USER_NAME, PASSWORD, new ArrayList<>());
		String token = this.jwtService.generateToken(userDetails);
		assertNotNull(token);

		Date expirationDate = this.jwtService.extractExpiration(token);
		assertNotNull(expirationDate);
		assertTrue(currentDate.before(expirationDate));
		assertTrue(((expirationDate.getTime() - currentDate.getTime()) / 1000) <= 180);
	}

	@Test
	public void testIsTokenExpired() {
		UserDetails userDetails = new User(USER_NAME, PASSWORD, new ArrayList<>());
		String token = this.jwtService.generateToken(userDetails);
		assertNotNull(token);
		assertFalse(this.jwtService.isTokenExpired(token));

		this.jwtService.expireToken(token);
		assertTrue(this.jwtService.isTokenExpired(token));
	}
}
