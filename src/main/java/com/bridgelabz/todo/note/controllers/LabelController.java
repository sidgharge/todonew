package com.bridgelabz.todo.note.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.todo.note.exceptions.LabelNameNotUniqueException;
import com.bridgelabz.todo.note.exceptions.LabelNotFoundException;
import com.bridgelabz.todo.note.repositories.LabelDto;
import com.bridgelabz.todo.note.services.LabelService;

@RestController
@RequestMapping("/labels")
public class LabelController {
	
	@Autowired
	private LabelService labelService;

	@PostMapping("/create")
	public ResponseEntity<LabelDto> createLabel(@RequestHeader("name") String name, Principal principal) {
		LabelDto labelDto = labelService.createLabel(name, Long.parseLong(principal.getName()));
		
		return new ResponseEntity<>(labelDto, HttpStatus.CREATED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Void> updateLabel(@RequestBody LabelDto labelDto, Principal principal) throws LabelNotFoundException, LabelNameNotUniqueException {
		labelService.updateLabel(labelDto, Long.parseLong(principal.getName()));
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<LabelDto>> getLabels(Principal principal) {
		List<LabelDto> labelDtos = labelService.getLabels(Long.parseLong(principal.getName()));
		
		return new ResponseEntity<>(labelDtos, HttpStatus.OK);
	}
}
