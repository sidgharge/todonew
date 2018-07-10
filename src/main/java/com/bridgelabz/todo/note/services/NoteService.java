package com.bridgelabz.todo.note.services;

import com.bridgelabz.todo.note.models.CreateNoteDto;
import com.bridgelabz.todo.note.models.UpdateNoteDto;

public interface NoteService {

	void createNote(CreateNoteDto noteDto, long userId);

	void updateNote(UpdateNoteDto noteDto, long userId);
}