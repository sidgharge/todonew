package com.bridgelabz.todo.user.controllers;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.user.exceptions.RegistrationException;
import com.bridgelabz.todo.user.exceptions.UserActivationException;
import com.bridgelabz.todo.user.models.RegistrationDto;
import com.bridgelabz.todo.user.models.Response;
import com.bridgelabz.todo.user.services.UserService;
import com.bridgelabz.todo.user.utils.UserUtility;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private WebApplicationContext context;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegistrationDto registrationDto, HttpServletRequest request) throws MessagingException, IOException {
		
		String url = UserUtility.getRequestUrl(request);
		userService.register(registrationDto, url);
		
		Response response = context.getBean(Response.class);
		response.setMessage("Succesfully registered");
		response.setStatus(1);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/activate")
	public ResponseEntity<?> activateUser(@RequestParam("token") String token) throws UserActivationException{
		userService.activateUser(token);
		
		Response response = context.getBean(Response.class);
		response.setMessage("Email id successfully verified");
		response.setStatus(1);
		
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
}
