package com.auth.api.service;

import com.auth.api.dto.security.AccountCredentialsDTO;
import com.auth.api.dto.security.TokenDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IAuthService {
    ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials) throws UsernameNotFoundException;
    ResponseEntity<TokenDTO> refreshToken(String nick, String refreshToken);

}
