package com.auth.api.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO extends BaseDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private String nick;
	private String email;

	private String userName;

	private String fullName;
	private String password;

	private LocalDateTime created;

	private LocalDateTime updated;

	private Boolean accountNonExpired;

	private Boolean accountNonLocked;

	private Boolean enabled;

	private List<RoleDTO> roles;
}
