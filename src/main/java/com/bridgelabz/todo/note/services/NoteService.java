package com.bridgelabz.todo.note.services;

import java.util.List;

import com.bridgelabz.todo.note.models.CreateNoteDto;
import com.bridgelabz.todo.note.models.NoteDto;
import com.bridgelabz.todo.note.models.UpdateNoteDto;

public interface NoteService {

	NoteDto createNote(CreateNoteDto noteDto, long userId);

	void updateNote(UpdateNoteDto noteDto, long userId);

	void deleteNote(long noteId, long userId);

	List<NoteDto> getAllNotes(long userId);
}
