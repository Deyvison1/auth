package com.auth.api.dto;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO extends BaseDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@NotBlank
	private String name;
	@NotBlank
	private String description;

	public String getAuthority() {
		return "ROLE_" + name;
	}

}
