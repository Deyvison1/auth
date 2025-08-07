package com.auth.api.models;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.auth.api.models.base.IdBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends IdBase implements UserDetails {

	@Serial
	private static final long serialVersionUID = 1L;

	@NotBlank
	@Size(min = 5, max = 30, message = "Nick tem que ter um minimo de 5 caracteres e maximo de 30")
	@Column(unique = true)
	private String nick;
	@Email
	@NotBlank
	@Size(min = 6, max = 40, message = "E-mail tem que ter um minimo de 6 caracteres e maximo de 40")
	private String email;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "full_name")
	private String fullName;
	@NotBlank
	@Size(min = 5, message = "Password tem que ter um minimo de 5")
	private String password;

	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime created;

	private LocalDateTime updated;

	@Column(name = "account_non_expired")
	private Boolean accountNonExpired;

	@Column(name = "account_non_locked")
	private Boolean accountNonLocked;

	private Boolean enabled;
	
	public User() {}

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = { @JoinColumn(name = "user_uuid") }, inverseJoinColumns = {
			@JoinColumn(name = "roles_uuid") })
	private List<Role> roles;

	public List<String> getRoles() {
		List<String> namesRole = new ArrayList<String>();
		for (Role role : roles) {
			namesRole.add(role.getName());
		}
		return namesRole;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

}
