package com.shanepaulus.challenge.api;

import java.util.List;

import com.shanepaulus.challenge.domain.User;
import com.shanepaulus.challenge.model.UserResponseModel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Shane Paulus
 * <p>
 * Date Created : 23-Aug-2021.
 */

@RestController
@RequestMapping("/users")
public class UserController {

	@PutMapping
	public ResponseEntity<User> updateUser() {
		return null;
	}

	@GetMapping
	public ResponseEntity<List<UserResponseModel>> getUserList() {
		return null;
	}
}
