package com.shanepaulus.challenge.model.mapper;

import java.util.List;

import com.shanepaulus.challenge.domain.User;
import com.shanepaulus.challenge.model.UserResponseModel;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Shane Paulus
 * <p>
 * Date Created : 25-Aug-2021.
 */

@Mapper
public interface UserResponseModelMapper {

	UserResponseModelMapper INSTANCE = Mappers.getMapper(UserResponseModelMapper.class);

	List<UserResponseModel> mapFromUserList(List<User> usersList);

}
