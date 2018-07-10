package com.bridgelabz.todo.note.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.note.models.Note;
import com.bridgelabz.todo.note.models.NoteExtras;
import com.bridgelabz.todo.note.models.CreateNoteDto;
import com.bridgelabz.todo.note.models.CreateNoteExtrasDto;

@Component
public class NoteFactory {

	@Autowired
	private WebApplicationContext context;
	
	public Note getNoteFromCreateNoteDto(CreateNoteDto noteDto) {
		Note note = context.getBean(Note.class);
		
		note.setTitle(noteDto.getTitle());
		note.setBody(noteDto.getBody());
		note.setImageUrl(noteDto.getImageUrl());
		
		return note;
	}

	public NoteExtras getNoteExtrasFromCreateNoteExtrasDto(CreateNoteExtrasDto createNoteExtrasDto) {
		NoteExtras noteExtras = context.getBean(NoteExtras.class);
		
		noteExtras.setArchived(createNoteExtrasDto.isArchived());
		noteExtras.setColor(createNoteExtrasDto.getColor());
		noteExtras.setPinned(createNoteExtrasDto.isPinned());
		noteExtras.setReminder(createNoteExtrasDto.getReminder());
		noteExtras.setTrashed(createNoteExtrasDto.isTrashed());
		
		return noteExtras;
	}
}
