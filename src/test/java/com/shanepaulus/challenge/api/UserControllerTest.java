package com.shanepaulus.challenge.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shanepaulus.challenge.domain.User;
import com.shanepaulus.challenge.model.UpdateUserRequestModel;
import com.shanepaulus.challenge.model.UserListRequestModel;
import com.shanepaulus.challenge.model.UserResponseModel;
import com.shanepaulus.challenge.repo.UserRepository;
import com.shanepaulus.challenge.service.impl.JwtServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author Shane Paulus
 * <p>
 * Date Created : 25-Aug-2021.
 */

@WebMvcTest(UserController.class)
@TestPropertySource(locations = "/application.yml")
public class UserControllerTest {

	private final String USERNAME = "paulie";
	private final String PASSWORD = "test";

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@MockBean
	private UserRepository userRepository;
	@SpyBean
	private JwtServiceImpl jwtService;

	@BeforeEach
	public void before() {
		openMocks(this);
	}

	@Test
	public void testUpdateUser() throws Exception {
		mockUserRepository();
		UpdateUserRequestModel updateUserRequestModel = new UpdateUserRequestModel();
		updateUserRequestModel.setUsername("test");
		updateUserRequestModel.setPassword("test");
		updateUserRequestModel.setPhone("0612345678");

		String token = this.jwtService.generateToken(user());

		mockMvc.perform(MockMvcRequestBuilders
						.put("/users")
						.header("Authorization", "Bearer " + token)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(updateUserRequestModel)))
				.andExpect(status().isOk());
	}

	@Test
	public void testUpdateUser_userNotFound() throws Exception {
		UpdateUserRequestModel updateUserRequestModel = new UpdateUserRequestModel();
		updateUserRequestModel.setUsername("test");
		updateUserRequestModel.setPassword("test");
		updateUserRequestModel.setPhone("0612345678");

		String token = this.jwtService.generateToken(user());

		mockMvc.perform(MockMvcRequestBuilders
						.put("/users")
						.header("Authorization", "Bearer " + token)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(updateUserRequestModel)))
				.andExpect(status().isForbidden());
	}

	@Disabled
	@Test
	public void testGetUserList() throws Exception {
		mockUserRepository();
		User user = user();
		UserListRequestModel userListRequestModel = new UserListRequestModel();
		doReturn(Collections.singletonList(user)).when(this.userRepository).findAll();

		String token = this.jwtService.generateToken(user);
		userListRequestModel.setToken(token);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
						.get("/users")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(userListRequestModel)))
				.andExpect(status().isOk())
				.andReturn();

		assertNotNull(mvcResult.getResponse());
		UserResponseModel[] userResponseModels = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), UserResponseModel[].class);

		assertEquals(1, userResponseModels.length);
		assertEquals(user.getId(), userResponseModels[0].getId());
		assertEquals(user.getPhone(), userResponseModels[0].getPhone());
	}

	private void mockUserRepository() {
		doReturn(user()).when(this.userRepository).findByUsername(USERNAME);
	}

	private User user() {
		User user = new User();
		user.setId(1);
		user.setUsername(USERNAME);
		user.setPassword(this.passwordEncoder.encode(PASSWORD));

		return user;
	}
}
