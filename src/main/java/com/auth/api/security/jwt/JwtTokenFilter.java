package com.auth.api.security.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
			throws IOException, ServletException {
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
		if (StringUtils.isNotBlank(token) && jwtTokenProvider.validateToken(token)) {
			Authentication auth = jwtTokenProvider.getAuthentication(token);
			if (auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		filter.doFilter(request, response);
	}

}
