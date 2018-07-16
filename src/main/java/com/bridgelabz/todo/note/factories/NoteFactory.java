package com.bridgelabz.todo.note.factories;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.note.models.Note;
import com.bridgelabz.todo.note.models.NoteDto;
import com.bridgelabz.todo.note.models.NoteExtras;
import com.bridgelabz.todo.note.repositories.LabelDto;
import com.bridgelabz.todo.note.models.CreateNoteDto;
import com.bridgelabz.todo.note.models.Label;

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

	public LabelDto getLabelDtoFromLabel(Label label) {
		LabelDto labelDto = context.getBean(LabelDto.class);
		
		labelDto.setId(label.getId());
		labelDto.setName(label.getName());
		
		return labelDto;
	}
}
