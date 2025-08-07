package com.auth.api.dto.security;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCredentialsDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String nick;
	private String password;
}
