package com.bridgelabz.todo.user.exceptionhandlers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.user.exceptions.RegistrationException;
import com.bridgelabz.todo.user.exceptions.UserActivationException;
import com.bridgelabz.todo.user.exceptions.UserNotFoundException;
import com.bridgelabz.todo.user.models.Response;

@ControllerAdvice
public class UserExceptionHandler {

	@Autowired
	private WebApplicationContext context;

	private final Logger logger = LoggerFactory.getLogger(UserExceptionHandler.class);

	@ExceptionHandler(RegistrationException.class)
	public ResponseEntity<?> handleRegistrationException(RegistrationException exception, HttpServletRequest request,
			@RequestAttribute("reqId") String reqId) {
		logger.info("Error occured for " + request.getRequestURI() + " with request id: " + reqId + ": "
				+ exception.getMessage(), exception);

		Response response = context.getBean(Response.class);
		response.setMessage(exception.getMessage());
		response.setStatus(-2);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserActivationException.class)
	public ResponseEntity<Response> handleActivationException(UserActivationException exception,
			HttpServletRequest request, @RequestAttribute("reqId") String reqId) {
		logger.info("Error occured for " + request.getRequestURI() + " with request id: " + reqId + ": "
				+ exception.getMessage(), exception);

		Response response = context.getBean(Response.class);
		response.setMessage(exception.getMessage());
		response.setStatus(-3);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleRegistrationException(Exception exception, HttpServletRequest request,
			@RequestAttribute("reqId") String reqId) {
		logger.error("Error occured for " + request.getRequestURI() + " with request id: " + reqId + ": "
				+ exception.getMessage(), exception);

		Response response = context.getBean(Response.class);
		response.setMessage("Something went wrong");
		response.setStatus(-1);

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception, HttpServletRequest request,
			@RequestAttribute("reqId") String reqId) {
		logger.info("Error occured for " + request.getRequestURI() + " with request id: " + reqId + ": "
				+ exception.getMessage(), exception);

		Response response = context.getBean(Response.class);
		response.setMessage(exception.getMessage());
		response.setStatus(-11);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
