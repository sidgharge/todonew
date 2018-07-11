package com.bridgelabz.todo.note.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.note.models.CreateNoteDto;
import com.bridgelabz.todo.note.models.NoteDto;
import com.bridgelabz.todo.note.models.UpdateNoteDto;
import com.bridgelabz.todo.note.services.NoteService;
import com.bridgelabz.todo.user.models.Response;

@RestController
@RequestMapping("/notes")
public class NoteController {

	@Autowired
	private NoteService noteService;

	@Autowired
	private WebApplicationContext context;

	@PostMapping("/create")
	public ResponseEntity<NoteDto> createNote(@RequestBody CreateNoteDto createNoteDto, Principal principal) {
		NoteDto noteDto = noteService.createNote(createNoteDto, Long.parseLong(principal.getName()));

		return new ResponseEntity<>(noteDto, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<Response> updateNote(@RequestBody UpdateNoteDto noteDto, Principal principal) {
		noteService.updateNote(noteDto, Long.parseLong(principal.getName()));

		Response response = context.getBean(Response.class);
		response.setMessage("Note updated successfully");
		response.setStatus(1);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// @DeleteMapping("/delete/{id}")
	// public
}
