package com.auth.api.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDTO extends BaseDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private String nick;
	private String email;

	private String userName;

	private String fullName;
	private String password;

	private Boolean accountNonExpired;

	private Boolean accountNonLocked;

	private Boolean enabled;

	private List<RoleDTO> roles;
}
