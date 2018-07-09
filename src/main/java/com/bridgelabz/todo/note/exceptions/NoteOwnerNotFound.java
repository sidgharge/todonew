package com.bridgelabz.todo.note.exceptions;

public class NoteOwnerNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NoteOwnerNotFound(String message) {
		super(message);
	}

}
