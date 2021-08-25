package com.shanepaulus.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Collections;
import java.util.List;

import com.shanepaulus.challenge.domain.User;
import com.shanepaulus.challenge.repo.UserRepository;
import com.shanepaulus.challenge.service.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Shane Paulus
 * <p>
 * Date Created : 25-Aug-2021.
 */

public class UserServiceTest {

	private final Integer ID = 1;
	private final String USER_NAME = "paulie";
	private final String PHONE = "0161254687";
	private final String PASSWORD = "ThisIsThePassword";

	@InjectMocks
	private UserServiceImpl userService;
	@Mock
	private UserRepository userRepository;

	@BeforeEach
	public void before() {
		openMocks(this);
	}

	@Test
	public void testLoadUserByUsername() {
		doReturn(user()).when(this.userRepository).findByUsername(USER_NAME);
		User user = this.userService.loadUserByUsername(USER_NAME);

		assertNotNull(user);
		assertEquals(ID, user.getId());
		assertEquals(USER_NAME, user.getUsername());
		assertEquals(PHONE, user.getPhone());
		assertEquals(PASSWORD, user.getPassword());

		verify(this.userRepository, times(1)).findByUsername(USER_NAME);
	}

	@Test
	public void testLoadUserByUsername_exception() {
		doReturn(null).when(this.userRepository).findByUsername(USER_NAME);
		assertThrows(UsernameNotFoundException.class, () -> this.userService.loadUserByUsername(USER_NAME));
		verify(this.userRepository, times(1)).findByUsername(USER_NAME);
	}

	@Test
	public void testUpdateUser() {
		doReturn(null).when(this.userRepository).save(any(User.class));
		this.userService.updateUser(user());

		verify(this.userRepository, times(1)).save(any(User.class));
	}

	@Test
	public void testUserList() {
		Iterable<User> userIterable = Collections.singletonList(user());
		doReturn(userIterable).when(this.userRepository).findAll();

		List<User> userList = this.userService.userList();
		assertNotNull(userList);
		assertEquals(1, userList.size());

		User user = userList.get(0);
		assertNotNull(user);
		assertEquals(ID, user.getId());
		assertEquals(USER_NAME, user.getUsername());
		assertEquals(PHONE, user.getPhone());
		assertEquals(PASSWORD, user.getPassword());
	}

	private User user() {
		User user = new User();
		user.setId(ID);
		user.setUsername(USER_NAME);
		user.setPassword(PASSWORD);
		user.setPhone(PHONE);

		return user;
	}
}
