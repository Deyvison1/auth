package com.auth.api.service.impl;

import com.auth.api.dto.RoleDTO;
import com.auth.api.dto.UserDTO;
import com.auth.api.exception.NotFoundEntityException;
import com.auth.api.mapper.UserMapper;
import com.auth.api.models.User;
import com.auth.api.repository.IUserReposotiry;
import com.auth.api.service.IRoleService;
import com.auth.api.service.IUserService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService, UserDetailsService {

	@Value("${default-role.name}")
	private String nameRoleDefault;

	@Value("${default-role.description}")
	private String descriptionRoleDefault;

	@Value("${default-role.uuid}")
	private UUID uuidRoleDefault;

	private final IUserReposotiry repository;
	private final UserMapper mapper;
	private final IRoleService roleService;

	@Override
	public UserDTO save(UserDTO userDTO) {
		setUpEntitySave(userDTO);
		User entity = mapper.toEntity(userDTO);
		return mapper.toDto(repository.save(entity));
	}

	private void setUpEntitySave(UserDTO userDTO) {
		setUpRole(userDTO);
		userDTO.setPassword(generateHashdPassword(userDTO.getPassword()));
		userDTO.setAccountNonExpired(true);
		userDTO.setAccountNonLocked(true);
		userDTO.setAccountNonExpired(true);
		userDTO.setEnabled(true);
	}

	private void setUpRole(UserDTO userDTO) {
		if (userDTO.getRoles() == null || userDTO.getRoles().isEmpty()) {
			RoleDTO roleDTO = roleService.findByIdToDTO(uuidRoleDefault);
			List<RoleDTO> listRoleDTO = new ArrayList<RoleDTO>();
			listRoleDTO.add(roleDTO);
			userDTO.setRoles(listRoleDTO);
		}
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
		return mapper.listToDto(repository.findAll(Sort.by(Sort.Direction.DESC, "created")));
	}

	private User monthEntity(User userSave, UserDTO userRequest) {
		userSave.setNick(userRequest.getNick());
		userSave.setEmail(userRequest.getEmail());
		return userSave;
	}
	
	@Override
	public UserDTO findByIdToDTO(UUID uuid) {
		return mapper.toDto(findById(uuid));
	}

	private User findById(UUID id) throws NotFoundEntityException {
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

	private String generateHashdPassword(String password) {
		PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder("", 8, 185000,
				Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("pbkdf2", pbkdf2PasswordEncoder);
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
		passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder);
		return passwordEncoder.encode(password);
	}

	@Override
	public UserDetails loadUserByUsername(String nick) throws UsernameNotFoundException {
		UserDTO user = findByNick(nick);
		return mapper.toEntity(user);
	}

	@Override
	public void delete(UUID uuid) {
		findById(uuid);
		repository.deleteById(uuid);
	}

	@Override
	public UUID findUUidByNick(String nick) {
		return findByNick(nick).getUuid();
	}
}
