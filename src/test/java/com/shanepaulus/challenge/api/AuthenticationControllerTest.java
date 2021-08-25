package com.shanepaulus.challenge.api;

import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.shanepaulus.challenge.domain.User;
import com.shanepaulus.challenge.repo.UserRepository;
import com.shanepaulus.challenge.service.impl.JwtServiceImpl;

import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author Shane Paulus
 * <p>
 * Date Created : 25-Aug-2021.
 */

@WebMvcTest
public class AuthenticationControllerTest {

	private final String USERNAME = "paulie";
	private final String PASSWORD = "test";

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@MockBean
	private UserRepository userRepository;
	@MockBean
	private JwtServiceImpl jwtService;

	@BeforeEach
	public void before() throws IllegalAccessException {
		openMocks(this);
		FieldUtils.writeField(this.jwtService, "sessionTokenTimeoutInSeconds", 180, true);
	}

	@Test
	public void testLogin() throws Exception {
		mockUserRepository();
		mockMvc.perform(MockMvcRequestBuilders
						.post("/login")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \"" + USERNAME + "\", \"password\": \"" + PASSWORD + "\"}"))
				.andExpect(status().isOk());
	}

	@Test
	public void testLogin_userNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
						.post("/login")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \"" + USERNAME + "\", \"password\": \"" + PASSWORD + "\"}"))
				.andExpect(status().isForbidden());
	}

	private void mockUserRepository() {
		User user = new User();
		user.setId(1);
		user.setUsername(USERNAME);
		user.setPassword(this.passwordEncoder.encode(PASSWORD));

		doReturn(user).when(this.userRepository).findByUsername(USERNAME);
	}
}
