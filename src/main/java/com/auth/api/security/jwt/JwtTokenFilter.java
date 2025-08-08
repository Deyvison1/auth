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
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenFilter extends GenericFilterBean {

	private final String tokenKeyAutorization;

	private final String key;

	private final JwtTokenProvider jwtTokenProvider;


	public JwtTokenFilter(final String tokenKeyAutorization, final String key, JwtTokenProvider jwtTokenProvider) {
		this.key = key;
		this.tokenKeyAutorization = tokenKeyAutorization;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// ✅ 2. VALIDAR HEADER DE GATEWAY
		String header = httpRequest.getHeader(key);
		if (!tokenKeyAutorization.equals(header)) {
			httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			httpResponse.getWriter().write("Access denied. Gateway only.");
			return;
		}

		// ✅ 3. VALIDAR TOKEN JWT
		String token = jwtTokenProvider.resolveToken(httpRequest);
		
		if (StringUtils.isNotBlank(token) && jwtTokenProvider.validateToken(token)) {

			Authentication auth = jwtTokenProvider.getAuthentication(token);
			if (auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			}

			filter.doFilter(request, response); // ⬅️ segue a requisição normalmente
			return;
		}
		filter.doFilter(request, response);
	}

}
