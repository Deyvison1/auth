package com.auth.api.service;

import com.auth.api.dto.UserDTO;
import com.auth.api.exception.NotFoundEntityException;

import java.util.List;
import java.util.UUID;

public interface IUserService {

	UserDTO save(UserDTO user);
	UserDTO update(UUID id, UserDTO user) throws NotFoundEntityException;
	UserDTO findByNick(String nick);
    List<UserDTO> findAll();
}
