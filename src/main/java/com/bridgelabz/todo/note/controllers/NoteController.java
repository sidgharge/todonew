package com.bridgelabz.todo.note.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Principal;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.todo.note.exceptions.CollaborationException;
import com.bridgelabz.todo.note.exceptions.ImageDeletionException;
import com.bridgelabz.todo.note.exceptions.LabelNotFoundException;
import com.bridgelabz.todo.note.exceptions.NoteIdRequredException;
import com.bridgelabz.todo.note.models.CreateNoteDto;
import com.bridgelabz.todo.note.models.NoteDto;
import com.bridgelabz.todo.note.models.UpdateNoteDto;
import com.bridgelabz.todo.note.services.NoteService;
import com.bridgelabz.todo.note.utils.NotesUtility;
import com.bridgelabz.todo.user.exceptions.UserNotFoundException;
import com.bridgelabz.todo.user.models.Response;
import com.bridgelabz.todo.user.models.UserDto;

//@CrossOrigin
@RestController
@RequestMapping("notes")
public class NoteController {

	@Autowired
	private NoteService noteService;

	@Autowired
	private WebApplicationContext context;

	@PostMapping("/create")
	public ResponseEntity<NoteDto> createNote(@RequestBody CreateNoteDto createNoteDto, Principal principal)
			throws LabelNotFoundException {
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

	@PutMapping("/pin-unpin/{id}/{status}")
	public ResponseEntity<Response> pinUnpin(@PathVariable("id") long noteId, @PathVariable("status") boolean status,
			Principal principal) {
		noteService.changePinStatus(noteId, status, Long.parseLong(principal.getName()));

		Response response = context.getBean(Response.class);
		if (status) {
			response.setMessage("Note pinned successfully");
		} else {
			response.setMessage("Note un-pinned successfully");
		}
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/archive-unarchive/{id}/{status}")
	public ResponseEntity<Response> archiveUnarchive(@PathVariable("id") long noteId,
			@PathVariable("status") boolean status, Principal principal) {
		noteService.changeArchiveStatus(noteId, status, Long.parseLong(principal.getName()));

		Response response = context.getBean(Response.class);
		if (status) {
			response.setMessage("Note archived successfully");
		} else {
			response.setMessage("Note un-archived successfully");
		}
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/trash-restore/{id}/{status}")
	public ResponseEntity<Response> trashRestore(@PathVariable("id") long noteId,
			@PathVariable("status") boolean status, Principal principal) {
		noteService.changeTrashStatus(noteId, status, Long.parseLong(principal.getName()));

		Response response = context.getBean(Response.class);
		if (status) {
			response.setMessage("Note trashed successfully");
		} else {
			response.setMessage("Note restored successfully");
		}
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/add-reminder/{id}/{date}")
	public ResponseEntity<Response> addRemoveReminder(@PathVariable("id") long noteId, @PathVariable("date") long time,
			Principal principal) {
		noteService.addReminder(noteId, time, Long.parseLong(principal.getName()));

		Response response = context.getBean(Response.class);
		response.setMessage("Reminder added successfully");
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/remove-reminder/{id}")
	public ResponseEntity<Response> removeReminder(@PathVariable("id") long noteId, Principal principal) {
		noteService.removeReminnder(noteId, Long.parseLong(principal.getName()));

		Response response = context.getBean(Response.class);
		response.setMessage("Reminder removed successfully");
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/color/{id}")
	public ResponseEntity<Response> archiveUnarchive(@PathVariable("id") long noteId,
			@RequestParam("color") String color, Principal principal) {
		noteService.changeColor(noteId, color, Long.parseLong(principal.getName()));

		Response response = context.getBean(Response.class);
		response.setMessage("Note color changed successfully");
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/image-upload")
	public ResponseEntity<Response> uploadImage(HttpServletRequest request, @RequestParam("image") MultipartFile image) throws MalformedURLException {
		String link = noteService.saveImage(image);

		Response response = new Response();
		response.setMessage(NotesUtility.getUrl(request, link));
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/image-upload/{id}")
	public ResponseEntity<Response> uploadImage(HttpServletRequest request, @RequestParam("image") MultipartFile image,
			@PathVariable("id") long id, Principal principal) throws MalformedURLException {
		String link = noteService.saveImageToNote(image, id, NotesUtility.getUrl(request, ""), Long.parseLong(principal.getName()));

		Response response = new Response();
		response.setMessage(link);
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/delete-image")
	public ResponseEntity<Response> deleteImage(@RequestParam("url") String imagename, HttpServletRequest request)
			throws MalformedURLException, NoteIdRequredException, ImageDeletionException {

		noteService.deleteImage(imagename);

		Response response = new Response();
		response.setMessage("Image successfully deleted");
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Response> deleteNote(@PathVariable("id") long noteId, Principal principal) {
		noteService.deleteNote(noteId, Long.parseLong(principal.getName()));
		
		Response response = new Response();
		response.setMessage("Note successfully deleted");
		response.setStatus(1);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<List<NoteDto>> getAllNotes(Principal principal) {
		List<NoteDto> notes = noteService.getAllNotes(Long.parseLong(principal.getName()));

		return new ResponseEntity<>(notes, HttpStatus.OK);
	}
	
	@PostMapping("/collaborate/{noteId}")
	public ResponseEntity<UserDto> collaborate(@PathVariable long noteId, @RequestParam("email") String email, Principal principal) throws UserNotFoundException, NumberFormatException, CollaborationException, MessagingException, IOException {
		UserDto userDto = noteService.collaborate(noteId, email, Long.parseLong(principal.getName()));
		
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}
}
