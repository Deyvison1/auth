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
		UserDTO dto = new UserDTO();
		dto.setUuid(entity.getUuid());
		dto.setAccountNonExpired(entity.getAccountNonExpired());
		dto.setAccountNonLocked(entity.getAccountNonLocked());
		dto.setCreated(entity.getCreated());
		dto.setEmail(entity.getEmail());

		dto.setEnabled(entity.getEnabled());
		dto.setFullName(entity.getFullName());
		dto.setNick(entity.getNick());
		dto.setPassword(entity.getPassword());
		dto.setUpdated(entity.getUpdated());
		dto.setUserName(entity.getUsername());
		dto.setRoles(getRolesDTO(entity));
		return dto;
	}

	public User toEntity(UserDTO dto) {
		User entity = new User();
		entity.setUuid(dto.getUuid());
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
