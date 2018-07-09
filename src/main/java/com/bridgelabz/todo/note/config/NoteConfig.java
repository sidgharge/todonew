package com.bridgelabz.todo.note.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.bridgelabz.todo.note.models.Note;

@Configuration
public class NoteConfig {

	@Bean
	@Scope("prototype")
	public Note note() {
		return new Note();
	}
}
