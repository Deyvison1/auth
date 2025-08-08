package com.auth.api.service.impl;

import com.auth.api.dto.UserDTO;
import com.auth.api.exception.NotFoundEntityException;
import com.auth.api.mapper.UserMapper;
import com.auth.api.models.User;
import com.auth.api.repository.IUserReposotiry;
import com.auth.api.service.IUserService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService, UserDetailsService {

	private final IUserReposotiry repository;
	private final UserMapper mapper;

	@Override
	public UserDTO save(UserDTO userDTO) {
		User entity = mapper.toEntity(userDTO);
		return mapper.toDto(repository.save(entity));
	}

	@Override
	public UserDTO update(UUID id, UserDTO user) throws NotFoundEntityException {
		User updatedUser = findById(id);
		if (thereHaveBeenChanges(updatedUser, user)) {
			return mapper.toDto(repository.save(monthEntity(updatedUser, user)));
		}
		return mapper.toDto(updatedUser);
	}

	public UserDTO findByNick(String nick) {
		return mapper.toDto(repository.findByNick(nick));
	}

	@Override
	public List<UserDTO> findAll() {
		return mapper.listToDto(repository.findAll());
	}

	private User monthEntity(User userSave, UserDTO userRequest) {
		userSave.setNick(userRequest.getNick());
		userSave.setEmail(userRequest.getEmail());
		return userSave;
	}

	public User findById(UUID id) throws NotFoundEntityException {
		return repository.findById(id).orElseThrow(NotFoundEntityException::new);
	}

	private Boolean thereHaveBeenChanges(User userSave, UserDTO userRequest) {
		if (!Objects.equals(userSave.getNick(), userRequest.getNick())
				|| !Objects.equals(userSave.getPassword(), userRequest.getPassword())
				|| !Objects.equals(userSave.getEmail(), userRequest.getEmail())) {
			return true;
		}
		return false;
	}

	@Override
	public UserDetails loadUserByUsername(String nick) throws UsernameNotFoundException {
		UserDTO user = findByNick(nick);
		return mapper.toEntity(user);
	}
}
