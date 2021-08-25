package com.shanepaulus.challenge.service;

import java.util.List;

import com.shanepaulus.challenge.domain.User;

/**
 * @author Shane Paulus
 * <p>
 * Date Created : 23-Aug-2021.
 */

public interface UserService {

	boolean updateUser(User user);

	List<User> userList();

}
