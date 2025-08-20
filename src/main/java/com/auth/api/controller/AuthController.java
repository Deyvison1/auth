package com.auth.api.controller;

import com.auth.api.dto.UserDTO;
import com.auth.api.dto.security.AccountCredentialsDTO;
import com.auth.api.dto.security.TokenDTO;
import com.auth.api.service.IAuthService;
import com.auth.api.service.impl.AuthServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final IAuthService authService;

	@PostMapping("/signin")
	public ResponseEntity<?> signIn(@RequestBody AccountCredentialsDTO credentialsDTO)
			throws UsernameNotFoundException {
		if (AuthServiceImpl.credentialsIsInvalid((credentialsDTO)))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid client request!");
		ResponseEntity<TokenDTO> token = authService.signIn(credentialsDTO);

		if (token == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid client request!");
		return ResponseEntity.ok().body(token);
	}

	@PutMapping("/refresh/{nick}")
	public ResponseEntity<?> refreshToken(@PathVariable String nick,
			@RequestHeader("Authorization") String refreshToken) throws UsernameNotFoundException {
		if (AuthServiceImpl.parametersAreInvalid(nick, refreshToken))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid client request!");
		ResponseEntity<TokenDTO> token = authService.refreshToken(nick, refreshToken);

		if (token == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid client request!");
		return ResponseEntity.ok().body(token);
	}

	@PostMapping
	public ResponseEntity<UserDTO> register(@RequestBody UserDTO user) {
		return ResponseEntity.ok(authService.createUser(user));
	}
}
