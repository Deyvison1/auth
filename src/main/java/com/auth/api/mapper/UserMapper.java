package com.auth.api.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.auth.api.dto.RoleDTO;
import com.auth.api.dto.UserDTO;
import com.auth.api.models.Role;
import com.auth.api.models.User;

@Service
public class UserMapper {

	private final IRoleMapper roleMapper;

	public UserMapper(final IRoleMapper roleMapper) {
		this.roleMapper = roleMapper;
	}

	public UserDTO toDto(User entity) {
		UserDTO dto = UserDTO.builder().accountNonExpired(entity.getAccountNonExpired())
				.accountNonLocked(entity.getAccountNonLocked()).created(entity.getCreated()).email(entity.getEmail())
				.enabled(entity.getEnabled()).fullName(entity.getFullName()).nick(entity.getNick())
				.password(entity.getPassword()).updated(entity.getUpdated()).userName(entity.getUsername())
				.roles(getRolesDTO(entity)).build();
		return dto;
	}

	public User toEntity(UserDTO dto) {
		User entity = new User();
		entity.setAccountNonExpired(dto.getAccountNonExpired());
		entity.setAccountNonLocked(dto.getAccountNonLocked());
		entity.setCreated(dto.getCreated());
		entity.setEmail(dto.getEmail());

		entity.setEnabled(dto.getEnabled());
		entity.setFullName(dto.getFullName());
		entity.setNick(dto.getNick());
		entity.setPassword(dto.getPassword());
		entity.setUpdated(dto.getUpdated());
		entity.setUserName(dto.getUserName());
		entity.setRoles(getRoles(dto));
		return entity;
	}

	private List<RoleDTO> getRolesDTO(User entity) {
		return roleMapper.toDto(entity.getRoles());
	}

	private List<Role> getRoles(UserDTO dto) {
		return roleMapper.toEntity(dto.getRoles());
	}

	public List<UserDTO> listToDto(List<User> entities) {
		List<UserDTO> dtos = new ArrayList<UserDTO>();
		for (User user : entities) {

			dtos.add(toDto(user));
		}
		return dtos;
	}

	public List<User> listToEntity(List<UserDTO> dtos) {
		List<User> entities = new ArrayList<User>();
		for (UserDTO dto : dtos) {

			entities.add(toEntity(dto));
		}
		return entities;
	}
}
