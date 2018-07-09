package com.bridgelabz.todo.note.utils;

import com.bridgelabz.todo.note.exceptions.EmptyNoteException;
import com.bridgelabz.todo.note.models.NoteDto;

public class NotesUtility {

	public static void validateNote(NoteDto noteDto) {
		if (noteDto.getTitle() == null || noteDto.getTitle().isEmpty()) {
			if (noteDto.getBody() == null || noteDto.getBody().isEmpty()) {
				throw new EmptyNoteException("Cannot create empty note");
			}
		}
	}
}
