package com.bridgelabz.todo.note.utils;

import com.bridgelabz.todo.note.exceptions.EmptyNoteException;
import com.bridgelabz.todo.note.models.CreateNoteDto;

public class NotesUtility {

	public static void validateNote(CreateNoteDto noteDto) {
		if (noteDto.getTitle() == null || noteDto.getTitle().isEmpty()) {
			if (noteDto.getBody() == null || noteDto.getBody().isEmpty()) {
				if (noteDto.getImageUrl() == null || noteDto.getImageUrl().isEmpty()) {
					throw new EmptyNoteException("Cannot create empty note");
				}
			}
		}
	}
}
