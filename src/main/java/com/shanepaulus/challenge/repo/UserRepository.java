package com.shanepaulus.challenge.repo;

import com.shanepaulus.challenge.domain.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Shane Paulus
 * <p>
 * Date Created : 24-Aug-2021.
 */

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	User findByUsername(String username);

}
