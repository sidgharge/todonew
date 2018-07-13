package com.bridgelabz.todo.note.factories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.note.models.Note;
import com.bridgelabz.todo.note.models.NoteDto;
import com.bridgelabz.todo.note.models.NoteExtras;
import com.bridgelabz.todo.note.models.NoteExtrasDto;
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
	
	public NoteDto getNoteDtoFromNote(Note note) {
		NoteDto noteDto = context.getBean(NoteDto.class);
		
		noteDto.setId(note.getId());
		noteDto.setImageUrl(note.getImageUrl());
		noteDto.setTitle(note.getTitle());
		noteDto.setBody(note.getBody());
		noteDto.setCreatedAt(note.getCreatedAt());
		noteDto.setUpdatedAt(note.getUpdatedAt());
		
		return noteDto;
	}

	public NoteExtrasDto getNoteExtrasDtoFromNoteExtras(NoteExtras noteExtras) {
		NoteExtrasDto noteExtrasDto = context.getBean(NoteExtrasDto.class);
		
		noteExtrasDto.setId(noteExtras.getId());
		noteExtrasDto.setArchived(noteExtras.isArchived());
		noteExtrasDto.setColor(noteExtras.getColor());
		noteExtrasDto.setPinned(noteExtras.isPinned());
		noteExtrasDto.setReminder(noteExtras.getReminder());
		noteExtrasDto.setTrashed(noteExtras.isTrashed());
		
		return noteExtrasDto;
	}
}
