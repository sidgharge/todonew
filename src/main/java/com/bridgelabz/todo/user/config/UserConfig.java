package com.bridgelabz.todo.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.bridgelabz.todo.user.models.Email;
import com.bridgelabz.todo.user.models.Response;
import com.bridgelabz.todo.user.models.User;
import com.bridgelabz.todo.user.models.UserDto;

@Configuration
public class UserConfig {

	@Bean
	@Scope("prototype")
	public User user() {
		return new User();
	}
	
	@Bean
	@Scope("prototype")
	public UserDto userDto() {
		return new UserDto();
	}
	
	@Bean
	@Scope("prototype")
	public Response response() {
		return new Response();
	}
	
	@Bean
	@Scope("prototype")
	public Email email() {
		return new Email();
	}
}
