package com.bridgelabz.todo.user.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.bridgelabz.todo.user.models.User;
import com.bridgelabz.todo.user.repositories.UserRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userReposiroty;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userReposiroty.findByEmail(username);
		
		if (user == null) {
			throw new BadCredentialsException("Email id or password is incorrect");
		}
		
		if(!user.isActivated()) {
			throw new BadCredentialsException("Email id not verified");
		}

		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
		List<SimpleGrantedAuthority> authorities = Arrays.asList(authority);
		return new org.springframework.security.core.userdetails.User(String.valueOf(user.getId()), user.getPassword(), authorities);
	}
}
