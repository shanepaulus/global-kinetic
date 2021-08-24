package com.shanepaulus.challenge.model;

import lombok.Data;

/**
 * @author Shane Paulus
 * <p>
 * Date Created : 24-Aug-2021.
 */

@Data
public class UpdateUserRequestModel {

	private String username;
	private String phone;
	private String password;

}
