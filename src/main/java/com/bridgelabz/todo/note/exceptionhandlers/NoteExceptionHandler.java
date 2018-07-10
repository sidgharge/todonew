package com.bridgelabz.todo.note.exceptionhandlers;

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

import com.bridgelabz.todo.note.exceptions.EmptyNoteException;
import com.bridgelabz.todo.note.exceptions.NoteOwnerNotFound;
import com.bridgelabz.todo.user.models.Response;

@ControllerAdvice
public class NoteExceptionHandler {

	@Autowired
	private WebApplicationContext context;

	private final Logger logger = LoggerFactory.getLogger(NoteExceptionHandler.class);

	@ExceptionHandler(EmptyNoteException.class)
	public ResponseEntity<Response> handleEmptyNoteException(EmptyNoteException exception, HttpServletRequest request,
			@RequestAttribute("reqId") String reqId) {
		logger.info("Error occured for " + request.getRequestURI() + " with request id: " + reqId + ": "
				+ exception.getMessage(), exception);

		Response response = context.getBean(Response.class);
		response.setMessage(exception.getMessage());
		response.setStatus(-4);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoteOwnerNotFound.class)
	public ResponseEntity<Response> handleNoteOwnerNotFooundException(NoteOwnerNotFound exception,
			HttpServletRequest request, @RequestAttribute("reqId") String reqId) {
		logger.error("Error occured for " + request.getRequestURI() + " with request id: " + reqId + ": "
				+ exception.getMessage());

		Response response = context.getBean(Response.class);
		response.setMessage(exception.getMessage());
		response.setStatus(-5);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
