package com.shanepaulus.challenge.service;

import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

/**
 * @author Shane Paulus
 * <p>
 * Date Created : 23-Aug-2021.
 */

public interface JwtService {

	String extractUserName(String token);

	Date extractExpiration(String token);

	<T> T extractClaim(String token, Function<Claims, T> claimsResolver);

	boolean isTokenExpired(String token);

	String generateToken(UserDetails userDetails);

	boolean validateToken(String token, UserDetails userDetails);
}
