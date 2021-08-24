package com.shanepaulus.challenge.api;

import com.shanepaulus.challenge.model.LoginRequestModel;
import com.shanepaulus.challenge.model.LoginResponseModel;
import com.shanepaulus.challenge.model.LogoutRequestModel;
import com.shanepaulus.challenge.model.LogoutResponseModel;
import com.shanepaulus.challenge.service.JwtService;
import com.shanepaulus.challenge.service.impl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Shane Paulus
 * <p>
 * Date Created : 23-Aug-2021.
 */

@RestController
@Slf4j
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private JwtService jwtService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseModel> login(@RequestBody LoginRequestModel loginRequestModel) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestModel.getUsername(), loginRequestModel.getPassword()));
		} catch (BadCredentialsException exc) {
			throw new Exception("Invalid username and password provided!", exc);
		}

		UserDetails userDetails = this.userService.loadUserByUsername(loginRequestModel.getUsername());
		String token = this.jwtService.generateToken(userDetails);

		return new ResponseEntity<>(new LoginResponseModel(token), HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<LogoutResponseModel> logout(@RequestBody LogoutRequestModel logoutRequestModel) {
		return null;
	}
}
