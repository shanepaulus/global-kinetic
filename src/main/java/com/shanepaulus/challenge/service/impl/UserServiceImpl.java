package com.shanepaulus.challenge.service.impl;

import com.shanepaulus.challenge.domain.User;
import com.shanepaulus.challenge.repo.UserRepository;
import com.shanepaulus.challenge.service.UserService;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Shane Paulus
 * <p>
 * Date Created : 24-Aug-2021.
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = this.userRepository.findByUsername(userName);

		if (user != null) {
			return user;
		}

		throw new UsernameNotFoundException("Unable to find user with username={" + userName + "}");
	}

	@Override
	public boolean updateUser(User user) {
		boolean merged = true;

		try {
			this.userRepository.save(user);
		} catch (HibernateException exc) {
			log.error("HibernateException: occurred while trying to update user!", exc);
			merged = false;
		}

		return merged;
	}
}
