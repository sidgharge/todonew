package com.bridgelabz.todo.note.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.bridgelabz.todo.note.models.Note;
import com.bridgelabz.todo.note.models.NoteDto;
import com.bridgelabz.todo.note.models.NoteExtras;

@Configuration
public class NoteConfig {

	@Bean
	@Scope("prototype")
	public Note note() {
		return new Note();
	}

	@Bean
	@Scope("prototype")
	public NoteExtras noteExtras() {
		return new NoteExtras();
	}
	
//	@Bean
//	@Scope("prototype")
//	public NoteExtrasDto noteExtrasDto() {
//		return new NoteExtrasDto();
//	}
	
	@Bean
	@Scope("prototype")
	public NoteDto noteDto() {
		return new NoteDto();
	}

}
