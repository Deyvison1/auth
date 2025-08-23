package com.auth.api.models;


import java.io.Serial;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;

import com.auth.api.models.base.IdBase;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles", schema = "auth")
@Getter
@Setter
public class Role extends IdBase implements GrantedAuthority {

	@Serial
	private static final long serialVersionUID = 1L;
	
	@NotBlank
	private String name;
	@NotBlank
	private String description;

	@Override
	public String getAuthority() {
		return "ROLE_" + name;
	}


	@Override
	public int hashCode() {
		return Objects.hash(description, name);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name);
	}
	
}
