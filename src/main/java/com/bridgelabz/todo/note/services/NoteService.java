package com.bridgelabz.todo.note.services;

import com.bridgelabz.todo.note.models.CreateNoteDto;

public interface NoteService {

	void createNote(CreateNoteDto noteDto, long userId);

	void updateNote(CreateNoteDto noteDto, long noteId, long userId);
}
