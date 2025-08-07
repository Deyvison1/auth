package com.auth.api.dto.security;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nick;
	private Boolean authenticated;
	private Date created;
	private Date expiration;
	private String accessToken;
	private String refreshToken;

}
