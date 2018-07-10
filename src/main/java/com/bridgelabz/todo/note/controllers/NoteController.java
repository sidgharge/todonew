package com.bridgelabz.todo.note.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.todo.note.models.CreateNoteDto;
import com.bridgelabz.todo.note.models.UpdateNoteDto;
import com.bridgelabz.todo.note.services.NoteService;

@RestController
@RequestMapping("/notes")
public class NoteController {

	@Autowired
	private NoteService noteService;

	@PostMapping("/create")
	public void createNote(@RequestBody CreateNoteDto noteDto, Principal principal) {
		noteService.createNote(noteDto, Long.parseLong(principal.getName()));
	}
	
	@PutMapping("/update")
	public void updateNote(@RequestBody UpdateNoteDto noteDto, Principal principal) {
		noteService.updateNote(noteDto, Long.parseLong(principal.getName()));
	}
}