package com.presidio.hiringchallenge.config;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetailsService;
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
    UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		var jwtTokenOptional = getTokenFromRequest(request);
		jwtTokenOptional.ifPresent(jwtToken->{
			if(JwtUtils.validateToken(jwtToken)) {
				System.out.println(jwtTokenOptional);
				String username = JwtUtils.getUsernameFromToken(jwtToken);
				var userDetails = userDetailsService.loadUserByUsername(username);
				var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		});
		filterChain.doFilter(request, response);
	}

	private Optional<String> getTokenFromRequest(HttpServletRequest request) {
		System.out.println();
		var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(StringUtils.isNotBlank(authHeader)&&authHeader.startsWith("Bearer")) {
			return Optional.of(authHeader.substring(7));
		}
		return Optional.empty();
	}

}