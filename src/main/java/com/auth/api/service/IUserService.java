package com.auth.api.service;

import com.auth.api.dto.MinUserDTO;
import com.auth.api.dto.UserDTO;
import com.auth.api.dto.UserFilter;
import com.auth.api.exception.NotFoundEntityException;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

	UserDTO save(UserDTO user);
	UserDTO update(UUID id, UserDTO user) throws NotFoundEntityException;
	UserDTO updateNickAndPassword(UUID id, MinUserDTO minUserDTO);
	UserDTO findByNick(String nick);
	UserDTO findByIdToDTO(UUID uuid);
	Page<UserDTO> findAll(Pageable page, UserFilter userFilter);
    void delete(UUID uuid);
    UUID findUUidByNick(String nick);
}
