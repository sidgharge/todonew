package com.bridgelabz.todo.note.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.note.exceptions.LabelNameNotUniqueException;
import com.bridgelabz.todo.note.exceptions.LabelNotFoundException;
import com.bridgelabz.todo.note.models.LabelDto;
import com.bridgelabz.todo.note.services.LabelService;
import com.bridgelabz.todo.user.models.Response;

@RestController
@RequestMapping("/labels")
public class LabelController {
	
	@Autowired
	private LabelService labelService;
	
	@Autowired
	private WebApplicationContext context;

	@PostMapping("/create")
	public ResponseEntity<LabelDto> createLabel(@RequestHeader("name") String name, Principal principal) {
		LabelDto labelDto = labelService.createLabel(name, Long.parseLong(principal.getName()));
		
		return new ResponseEntity<>(labelDto, HttpStatus.CREATED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Response> updateLabel(@RequestBody LabelDto labelDto, Principal principal) throws LabelNotFoundException, LabelNameNotUniqueException {
		labelService.updateLabel(labelDto, Long.parseLong(principal.getName()));
		
		Response response = context.getBean(Response.class);
		response.setMessage("Label updated successfully");
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<LabelDto>> getLabels(Principal principal) {
		List<LabelDto> labelDtos = labelService.getLabels(Long.parseLong(principal.getName()));
		
		return new ResponseEntity<>(labelDtos, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete-label/{id}")
	public ResponseEntity<Response> deleteLabel(@PathVariable long id, Principal principal) throws NumberFormatException, LabelNotFoundException {
		labelService.deleteLabel(id, Long.parseLong(principal.getName()));
		
		Response response = context.getBean(Response.class);
		response.setMessage("Label deleted successfully");
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping("/add-label/{labelId}/{noteId}")
	public ResponseEntity<Response> addLabel(@PathVariable long labelId, @PathVariable long noteId, Principal principal) throws LabelNotFoundException {
		labelService.addLabel(labelId, noteId, Long.parseLong(principal.getName()));
		
		Response response = context.getBean(Response.class);
		response.setMessage("Label added successfully");
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping("/remove-label/{labelId}/{noteId}")
	public ResponseEntity<Response> removeLabel(@PathVariable long labelId, @PathVariable long noteId, Principal principal) throws LabelNotFoundException {
		labelService.removeLabel(labelId, noteId, Long.parseLong(principal.getName()));
		
		Response response = context.getBean(Response.class);
		response.setMessage("Label removed successfully");
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
