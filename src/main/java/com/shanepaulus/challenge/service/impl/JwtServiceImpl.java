package com.shanepaulus.challenge.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.PostConstruct;

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

	@Value("${session.token.timeout-in-seconds:180}")
	private Integer sessionTokenTimeoutInSeconds;
	private List<String> tokenBlockList;     // This is not an ideal design

	@PostConstruct
	public void init() {
		this.tokenBlockList = new ArrayList<>();
	}

	@Override
	public String extractUserName(String token) {
		return this.tokenBlockList.contains(token) ? null : extractClaim(token, Claims::getSubject);
	}

	@Override
	public Date extractExpiration(String token) {
		return this.tokenBlockList.contains(token) ? null : extractClaim(token, Claims::getExpiration);
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
		return this.tokenBlockList.contains(token) || extractExpiration(token).before(new Date());
	}

	@Override
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + (1000L * this.sessionTokenTimeoutInSeconds)))
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

	@Override
	public boolean validateToken(String token, UserDetails userDetails) {
		String userName = extractUserName(token);
		return !this.tokenBlockList.contains(token) && userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	@Override
	public void expireToken(String token) {
		this.tokenBlockList.add(token);
	}
}
