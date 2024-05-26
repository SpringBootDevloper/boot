package com.presidio.hiringchallenge.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
//

import com.presidio.hiringchallenge.repository.UserRepo;
import com.presidio.hiringchallenge.entity.*;
//
import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	UserRepo register;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user =register.findByEmail(username);
		if(user==null) {
			throw new UsernameNotFoundException("Could not find user !!");
		}
		return new User(user.getEmail(), user.getPassword(), user.getAuthorities());
	}

}
