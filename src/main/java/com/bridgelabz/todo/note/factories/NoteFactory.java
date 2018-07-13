package com.bridgelabz.todo.note.factories;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.note.models.Note;
import com.bridgelabz.todo.note.models.NoteDto;
import com.bridgelabz.todo.note.models.NoteExtras;
import com.bridgelabz.todo.note.models.CreateNoteDto;

@Component
public class NoteFactory {

	@Autowired
	private WebApplicationContext context;
	
	public Note getNoteFromCreateNoteDto(CreateNoteDto noteDto) {
		Note note = context.getBean(Note.class);
		
		note.setTitle(noteDto.getTitle());
		note.setBody(noteDto.getBody());
		note.setImageUrl(noteDto.getImageUrl());
		
		NoteExtras extras = context.getBean(NoteExtras.class);
		
		extras.setNote(note);
		extras.setPinned(noteDto.isPinned());
		extras.setReminder(noteDto.getReminder());
		extras.setTrashed(noteDto.isTrashed());
		extras.setArchived(noteDto.isArchived());
		
		if (noteDto.getColor() == null) {
			extras.setColor("#fff");
		} else {
			extras.setColor(noteDto.getColor());
		}
		
		note.setNoteExtras(Arrays.asList(extras));
		
		return note;
	}

//	public NoteExtras getNoteExtrasFromCreateNoteExtrasDto(CreateNoteExtrasDto createNoteExtrasDto) {
//		NoteExtras noteExtras = context.getBean(NoteExtras.class);
//		
//		noteExtras.setArchived(createNoteExtrasDto.isArchived());
//		noteExtras.setColor(createNoteExtrasDto.getColor());
//		noteExtras.setPinned(createNoteExtrasDto.isPinned());
//		noteExtras.setReminder(createNoteExtrasDto.getReminder());
//		noteExtras.setTrashed(createNoteExtrasDto.isTrashed());
//		
//		return noteExtras;
//	}
	
	public NoteDto getNoteDtoFromNoteAndExtras(Note note, NoteExtras extras) {
		NoteDto noteDto = context.getBean(NoteDto.class);
		
		noteDto.setId(note.getId());
		noteDto.setImageUrl(note.getImageUrl());
		noteDto.setTitle(note.getTitle());
		noteDto.setBody(note.getBody());
		noteDto.setCreatedAt(note.getCreatedAt());
		noteDto.setUpdatedAt(note.getUpdatedAt());
		
		noteDto.setArchived(extras.isArchived());
		noteDto.setColor(extras.getColor());
		noteDto.setPinned(extras.isPinned());
		noteDto.setReminder(extras.getReminder());
		noteDto.setTrashed(noteDto.isTrashed());
		
		return noteDto;
	}

//	public NoteExtrasDto getNoteExtrasDtoFromNoteExtras(NoteExtras noteExtras) {
//		NoteExtrasDto noteExtrasDto = context.getBean(NoteExtrasDto.class);
//		
//		noteExtrasDto.setId(noteExtras.getId());
//		noteExtrasDto.setArchived(noteExtras.isArchived());
//		noteExtrasDto.setColor(noteExtras.getColor());
//		noteExtrasDto.setPinned(noteExtras.isPinned());
//		noteExtrasDto.setReminder(noteExtras.getReminder());
//		noteExtrasDto.setTrashed(noteExtras.isTrashed());
//		
//		return noteExtrasDto;
//	}
}
