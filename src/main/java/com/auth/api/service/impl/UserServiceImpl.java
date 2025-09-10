package com.auth.api.service.impl;

import com.auth.api.dto.MinUserDTO;
import com.auth.api.dto.UserDTO;
import com.auth.api.dto.UserFilter;
import com.auth.api.exception.NotFoundEntityException;
import com.auth.api.mapper.IRoleMapper;
import com.auth.api.mapper.UserMapper;
import com.auth.api.models.User;
import com.auth.api.repository.IUserReposotiry;
import com.auth.api.repository.UserSpecification;
import com.auth.api.service.IUserService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
	private final IRoleMapper roleMapper;

	@Override
	public UserDTO save(UserDTO userDTO) {
		setUpEntitySave(userDTO);
		User entity = mapper.toEntity(userDTO);
		return mapper.toDto(repository.save(entity));
	}

	private void setUpEntitySave(UserDTO userDTO) {
		userDTO.setPassword(generateHashdPassword(userDTO.getPassword()));
		userDTO.setAccountNonExpired(true);
		userDTO.setAccountNonLocked(true);
		userDTO.setAccountNonExpired(true);
		userDTO.setEnabled(true);
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
	public Page<UserDTO> findAll(Pageable page, UserFilter userFilter) {
	    Specification<User> spec = UserSpecification.filterBy(userFilter);
	    Page<User> entities = repository.findAll(spec, page);
		List<UserDTO> dtos = mapper.listToDto(entities.getContent());
		return new PageImpl<>(dtos, page, entities.getTotalElements());
	}

	private User monthEntity(User userSave, UserDTO userRequest) {
		userSave.setEmail(userRequest.getEmail());
		userSave.setNick(userRequest.getNick());
		userSave.setEmail(userRequest.getEmail());
		userSave.setUpdated(LocalDateTime.now());
		userSave.setRoles(roleMapper.toEntity(userRequest.getRoles()));
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

	@Override
	public UserDTO updateNickAndPassword(UUID id, MinUserDTO minUserDTO) {
		User updatedUser = findById(id);
		updatedUser.setNick(minUserDTO.getNick());
		updatedUser.setPassword(generateHashdPassword(minUserDTO.getPassword()));
		return mapper.toDto(repository.save(updatedUser));
		
	}
}
