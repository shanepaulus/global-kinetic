package com.shanepaulus.challenge.api;

import com.shanepaulus.challenge.model.LoginRequestModel;
import com.shanepaulus.challenge.model.LoginResponseModel;
import com.shanepaulus.challenge.model.LogoutRequestModel;
import com.shanepaulus.challenge.model.LogoutResponseModel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Shane Paulus
 * <p>
 * Date Created : 23-Aug-2021.
 */

@RestController
public class AuthenticationController {

	@PostMapping("/login")
	public ResponseEntity<LoginResponseModel> login(@RequestBody LoginRequestModel loginRequestModel) {
		return null;
	}

	@PostMapping("/logout")
	public ResponseEntity<LogoutResponseModel> logout(@RequestBody LogoutRequestModel logoutRequestModel) {
		return null;
	}
}
