package com.bridgelabz.todo.user.exceptionhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.user.exceptions.RegistrationException;
import com.bridgelabz.todo.user.models.Response;

@ControllerAdvice
public class UserExceptionHandler {

	@Autowired
	private WebApplicationContext context;
	
	private final Logger logger = LoggerFactory.getLogger(UserExceptionHandler.class);
	
	@ExceptionHandler(RegistrationException.class)
	public ResponseEntity<?> handleRegistrationException(RegistrationException exception){
		logger.info(exception.getMessage());
		
		Response response = context.getBean(Response.class);
		response.setMessage(exception.getMessage());
		response.setStatus(-2);
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleRegistrationException(Exception exception){
		logger.info(exception.getMessage());
		
		Response response = context.getBean(Response.class);
		response.setMessage("Something went wrong");
		response.setStatus(-1);
		
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
