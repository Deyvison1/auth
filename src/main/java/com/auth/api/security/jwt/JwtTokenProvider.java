package com.auth.api.security.jwt;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.auth.api.dto.security.TokenDTO;
import com.auth.api.exception.InvalidJwtAuthenticationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

	@Value("${security.jwt.token.secret-key:secret}")
	private String secretKey = "uH12jPqz0W+o+fSde9q5Tf0mXEtmCqKnm2Vp3xczxUg=";

	@Value("${security.jwt.token.expire-lenght:3600000}")
	private long validityInMilliseconds = 3600000; // 1h

	private final UserDetailsService userDetailsService;

	Algorithm algorithm = null;

	@PostConstruct
	protected void init() {
		algorithm = Algorithm.HMAC256(secretKey.getBytes());
	}

	public TokenDTO createAccessToken(String nick, List<String> roles) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);
		String refreshToken = getRefreshToken(nick, roles, now, validity);
		String accessToken = getAccessToken(nick, roles, now, validity);
		return new TokenDTO(nick, true, now, validity, accessToken, refreshToken);
	}

	public TokenDTO refreshToken(String refreshToken) {
		String refreshTokenSubstring = "";
		if(refreshTokenContainsBearer(refreshToken)) refreshTokenSubstring = refreshToken = refreshToken.substring("Bearer ".length());

		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = verifier.verify(refreshTokenSubstring);

		String nick = decodedJWT.getSubject();
		List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
		return createAccessToken(nick, roles);
	}

	private String getRefreshToken(String nick, List<String> roles, Date now, Date validity) {
		Date refreshTokenValidity = new Date(now.getTime() + (validityInMilliseconds * 3));
		return JWT.create().withClaim("roles", roles).withJWTId(UUID.randomUUID().toString()).withIssuedAt(now).withExpiresAt(refreshTokenValidity).withSubject(nick)
				.sign(algorithm);
	}

	private String getAccessToken(String nick, List<String> roles, Date now, Date validity) {
		Date refreshTokenValidity = new Date(now.getTime() + validityInMilliseconds);
		return JWT.create().withClaim("roles", roles).withJWTId(UUID.randomUUID().toString()).withIssuedAt(now).withExpiresAt(refreshTokenValidity)
				.withSubject(nick).sign(algorithm);
	}

	public Authentication getAuthentication(String token) {
		DecodedJWT decodedJWT = decodedToken(token);
		if(token == null) throw new InvalidJwtAuthenticationException("Invalid token");
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	public DecodedJWT decodedToken(String token) {
		Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
		JWTVerifier verifier = JWT.require(alg).build();
        return verifier.verify(token);
	}
	
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if(refreshTokenContainsBearer(bearerToken)) return bearerToken.substring("Bearer ".length());
		return null;
	}

	private static boolean refreshTokenContainsBearer(String refreshToken) {
		return StringUtils.isNotBlank(refreshToken) && refreshToken.startsWith("Bearer ");
	}
	
	public boolean validateToken(String token) {
		DecodedJWT decodedJWT = decodedToken(token);
		try {
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (Exception e) {
			throw new InvalidJwtAuthenticationException("Expired or Invalid JWT Token");
		}
	}
}
