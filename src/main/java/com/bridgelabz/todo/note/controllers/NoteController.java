package com.bridgelabz.todo.note.controllers;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/notes")
public class NoteController {

	@GetMapping("/test")
	public String principal(Principal principal) {
		return principal.getName();
	}
}
