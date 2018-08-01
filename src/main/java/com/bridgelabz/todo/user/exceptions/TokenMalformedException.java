package com.bridgelabz.todo.user.exceptions;

public class TokenMalformedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public TokenMalformedException (String message) {
		super(message);
	}

}
