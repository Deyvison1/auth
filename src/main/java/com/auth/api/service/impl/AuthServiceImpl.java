package com.auth.api.service.impl;

import com.auth.api.dto.security.AccountCredentialsDTO;
import com.auth.api.dto.security.TokenDTO;
import com.auth.api.models.User;
import com.auth.api.repository.IUserReposotiry;
import com.auth.api.security.jwt.JwtTokenProvider;
import com.auth.api.service.IAuthService;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final IUserReposotiry userReposotiry;

	@Override
	public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(credentials.getNick(), credentials.getPassword()));
			User user = userReposotiry.findByNick(credentials.getNick());
			if (user == null)
				throw new UsernameNotFoundException("NickName " + credentials.getNick() + " not found");

			TokenDTO tokenResponse = jwtTokenProvider.createAccessToken(credentials.getNick(), user.getRolesName());
			return ResponseEntity.ok(tokenResponse);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public ResponseEntity<TokenDTO> refreshToken(String nick, String refreshToken) {
		User user = userReposotiry.findByNick(nick);
		if (user == null)
			throw new UsernameNotFoundException("NickName " + nick + " not found");
		TokenDTO tokenResponse = jwtTokenProvider.refreshToken(refreshToken);
		return ResponseEntity.ok(tokenResponse);
	}

	
	public static boolean credentialsIsInvalid(AccountCredentialsDTO credentialsDTO) {
		return credentialsDTO == null || StringUtils.isBlank(credentialsDTO.getNick())
				|| StringUtils.isBlank(credentialsDTO.getPassword());
	}

	public static boolean parametersAreInvalid(String nick, String refreshToken) {
		return StringUtils.isBlank(nick) || StringUtils.isBlank(refreshToken);
	}

	@Override
	public DecodedJWT getDecodedToken() {
		return jwtTokenProvider.decodedToken();
	}
}
