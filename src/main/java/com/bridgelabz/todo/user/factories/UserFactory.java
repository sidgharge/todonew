package com.bridgelabz.todo.user.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.user.models.Email;
import com.bridgelabz.todo.user.models.RegistrationDto;
import com.bridgelabz.todo.user.models.User;

@Component
public class UserFactory {
	
	@Autowired
	private WebApplicationContext context;

	public User getUserFromRegistrationDto(RegistrationDto registrationDto) {
		User user = context.getBean(User.class);
		user.setFirstname(registrationDto.getFirstname());
		user.setLastname(registrationDto.getLastname());
		user.setEmail(registrationDto.getEmail());
		user.setContact(registrationDto.getContact());
		user.setPassword(registrationDto.getPassword());
		return user;
	}
	
	public Email getEmail(String to, String subject, String text) {
		Email email = context.getBean(Email.class);
		email.setTo(to);
		email.setText(text);
		email.setSubject(subject);
		return email;
	}
}
