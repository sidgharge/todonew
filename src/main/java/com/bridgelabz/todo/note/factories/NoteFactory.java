package com.bridgelabz.todo.note.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.note.models.Note;
import com.bridgelabz.todo.note.models.NoteDto;

@Component
public class NoteFactory {

	@Autowired
	private WebApplicationContext context;
	
	public Note getNoteFromNoteDto(NoteDto noteDto) {
		Note note = context.getBean(Note.class);
		
		note.setTitle(noteDto.getTitle());
		note.setBody(noteDto.getBody());
		
		return note;
	}
}
