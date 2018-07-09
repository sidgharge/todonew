package com.bridgelabz.todo.note.exceptions;

public class EmptyNoteException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EmptyNoteException(String message) {
		super(message);
	}

}
