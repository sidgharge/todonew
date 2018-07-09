package com.bridgelabz.todo.note.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.todo.note.exceptions.NoteOwnerNotFound;
import com.bridgelabz.todo.note.factories.NoteFactory;
import com.bridgelabz.todo.note.models.Note;
import com.bridgelabz.todo.note.models.NoteDto;
import com.bridgelabz.todo.note.repositories.NoteRepository;
import com.bridgelabz.todo.note.utils.NotesUtility;
import com.bridgelabz.todo.user.models.User;
import com.bridgelabz.todo.user.repositories.UserRepository;

@Service
public class NoteServiceImpl implements NoteService {
	
	@Autowired
	private NoteFactory noteFactory;
	
	@Autowired
	private NoteRepository noteRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public void createNote(NoteDto noteDto, long userId) {
		NotesUtility.validateNote(noteDto);
		
		Note note = noteFactory.getNoteFromNoteDto(noteDto);
		
		Date createdAt = new Date();
		note.setCreatedAt(createdAt);
		note.setUpdatedAt(createdAt);
		
		Optional<User> optionalUser = userRepository.findById(userId);
		
		if (!optionalUser.isPresent()) {
			throw new NoteOwnerNotFound("Note owner does not exist");
		}
		
		User owner = optionalUser.get();
		
		note.setOwner(owner);
		
		noteRepository.save(note);
	}

}
