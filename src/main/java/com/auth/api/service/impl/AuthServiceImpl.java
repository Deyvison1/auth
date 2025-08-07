package com.auth.api.service.impl;

import com.auth.api.dto.security.AccountCredentialsDTO;
import com.auth.api.dto.security.TokenDTO;
import com.auth.api.models.User;
import com.auth.api.repository.IUserReposotiry;
import com.auth.api.security.jwt.JwtTokenProvider;
import com.auth.api.service.IAuthService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserReposotiry  userReposotiry;

    @Override
    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials)  {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                credentials.getNick(), credentials.getPassword()
        ));
        User user = userReposotiry.findByNick(credentials.getNick());
        if(user == null) throw new UsernameNotFoundException("NickName "+ credentials.getNick() + " not found");

        TokenDTO tokenResponse = jwtTokenProvider.createAccessToken(
                credentials.getNick(),
                user.getRoles()
        );
        return ResponseEntity.ok(tokenResponse);
    }

    @Override
    public User createUser(User user) {
        user.setPassword(generateHashdPassword(user.getPassword()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setEnabled(true);
        return userReposotiry.save(user);
    }

    @Override
    public ResponseEntity<TokenDTO> refreshToken(String nick, String refreshToken) {
        User user = userReposotiry.findByNick(nick);
        if(user == null) throw new UsernameNotFoundException("NickName "+ nick + " not found");
        TokenDTO tokenResponse = jwtTokenProvider.refreshToken(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    private String generateHashdPassword(String password) {
        PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(
                "", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
        );
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2PasswordEncoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder);
        return passwordEncoder.encode(password);
    }

    public static boolean credentialsIsInvalid(AccountCredentialsDTO credentialsDTO) {
        return credentialsDTO == null || StringUtils.isBlank(credentialsDTO.getNick()) || StringUtils.isBlank(credentialsDTO.getPassword());
    }

    public static boolean parametersAreInvalid(String nick, String refreshToken) {
        return StringUtils.isBlank(nick) || StringUtils.isBlank(refreshToken);
    }
}

