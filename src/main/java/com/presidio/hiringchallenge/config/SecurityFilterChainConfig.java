package com.presidio.hiringchallenge.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class SecurityFilterChainConfig{
	@Autowired
	AuthenticationEntryPoint AuthenticationEntryPoint;
	@Autowired
	JWTAuthenticationFilter JwtAuthenticationFilter;
	
	
	public SecurityFilterChainConfig(JWTAuthenticationFilter jwtAuthenticationFilter) {
		super();
		JwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
			httpSecurity.cors(corsConfig->corsConfig.configurationSource(getConfigurationSource()));
		httpSecurity.csrf(AbstractHttpConfigurer::disable);
		
		httpSecurity.authorizeHttpRequests(auth -> {
			auth.requestMatchers("/rentify/registerUser","/rentify/verify/**",
					"/rentify/getProperties"
					,"/rentify/login").permitAll().
			anyRequest().authenticated();
		});
		httpSecurity.exceptionHandling(exceptionConfig -> exceptionConfig.authenticationEntryPoint(AuthenticationEntryPoint));
		httpSecurity.sessionManagement(SessionConfig->SessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		httpSecurity.headers(headers -> headers.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin", "http://localhost:4200")));
		httpSecurity.addFilterBefore(JwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}
	private static CorsConfigurationSource getConfigurationSource() {
		var corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedMethods(List.of("*"));
		corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
	    corsConfiguration.setAllowedHeaders(List.of("Content-Type", "Authorization"));
		var source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**",corsConfiguration);
		return source;
	}
}
