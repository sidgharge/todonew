package com.bridgelabz.todo.note.services;

import com.bridgelabz.todo.note.models.NoteDto;

public interface NoteService {

	void createNote(NoteDto noteDto, long userId);
}
