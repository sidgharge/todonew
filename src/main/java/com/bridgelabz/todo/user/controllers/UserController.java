package com.bridgelabz.todo.user.controllers;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.user.exceptions.RegistrationException;
import com.bridgelabz.todo.user.models.RegistrationDto;
import com.bridgelabz.todo.user.models.Response;
import com.bridgelabz.todo.user.services.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private WebApplicationContext context;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegistrationDto registrationDto) throws RegistrationException, MessagingException, IOException {
		
		userService.register(registrationDto);
		
		Response response = context.getBean(Response.class);
		response.setMessage("Succesfully registered");
		response.setStatus(1);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
}
