package com.shanepaulus.challenge.api;

import java.util.List;

import com.shanepaulus.challenge.domain.User;
import com.shanepaulus.challenge.model.UpdateUserRequestModel;
import com.shanepaulus.challenge.model.UserResponseModel;
import com.shanepaulus.challenge.model.mapper.UserResponseModelMapper;
import com.shanepaulus.challenge.service.JwtService;
import com.shanepaulus.challenge.service.impl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Shane Paulus
 * <p>
 * Date Created : 23-Aug-2021.
 */

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtService jwtService;

	@PutMapping
	public ResponseEntity updateUser(@RequestBody UpdateUserRequestModel updateUserRequestModel, @RequestHeader("Authorization") String authorizationToken) throws Exception {
		try {
			String userNameByToken = this.jwtService.extractUserName(authorizationToken);
			User user = this.userService.loadUserByUsername(userNameByToken);
			user.setUsername(updateUserRequestModel.getUsername());
			user.setPassword(updateUserRequestModel.getPhone());
			user.setPassword(this.passwordEncoder.encode(updateUserRequestModel.getPassword()));

			if (this.userService.updateUser(user)) {
				return new ResponseEntity(HttpStatus.OK);
			} else {
				return new ResponseEntity("Unable to update the user details for user with username={" + updateUserRequestModel.getUsername() + "}", HttpStatus.BAD_REQUEST);
			}
		} catch (UsernameNotFoundException exc) {
			throw new Exception("Invalid username provided by the request authorization token!", exc);
		}
	}

	@GetMapping
	public ResponseEntity<List<UserResponseModel>> getUserList(@RequestHeader("Authorization") String authorizationTokenHeader) {
		boolean isValidToken = !this.jwtService.isTokenExpired(authorizationTokenHeader);

		if (isValidToken) {
			return new ResponseEntity<>(UserResponseModelMapper.INSTANCE.mapFromUserList(this.userService.userList()), HttpStatus.OK);
		} else {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
	}
}
