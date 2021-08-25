package com.shanepaulus.challenge.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.shanepaulus.challenge.service.JwtService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Shane Paulus
 * <p>
 * Date Created : 23-Aug-2021.
 */

@Service
public class JwtServiceImpl implements JwtService {

	private final String secret = "secret";

	@Value("${session.token.timeout-in-seconds}")
	private Integer sessionTokenTimeoutInSeconds;

	@Override
	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	@Override
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	@Override
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		token = token.replaceAll("Bearer ", "");
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	@Override
	public boolean isTokenExpired(String token) {
		System.out.println("isTokenExpired: token >> " + token + " expiration >> " + extractExpiration(token));
		return extractExpiration(token).before(new Date());
	}

	@Override
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + (1000 * 180)))
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

	@Override
	public boolean validateToken(String token, UserDetails userDetails) {
		String userName = extractUserName(token);
		return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	@Override
	public void expireToken(String token) {
		Claims claims = extractAllClaims(token);
		claims.setExpiration(new Date());

		// TODO: create a token caching mechanism as invalidating these tokens does not work!!!!

		System.out.println("expireToken: token >> " + token + " expireDate >> " + claims.getExpiration());
	}
}
