package com.bridgelabz.todo.note.services;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.note.exceptions.NoteNotFoundException;
import com.bridgelabz.todo.note.exceptions.NoteOwnerNotFound;
import com.bridgelabz.todo.note.exceptions.UnAuthorizedException;
import com.bridgelabz.todo.note.factories.NoteFactory;
import com.bridgelabz.todo.note.models.Note;
import com.bridgelabz.todo.note.models.NoteDto;
import com.bridgelabz.todo.note.models.NoteExtras;
import com.bridgelabz.todo.note.models.NoteExtrasDto;
import com.bridgelabz.todo.note.models.UpdateNoteDto;
import com.bridgelabz.todo.note.models.CreateNoteDto;
import com.bridgelabz.todo.note.repositories.NoteExtrasRepository;
import com.bridgelabz.todo.note.repositories.NoteRepository;
import com.bridgelabz.todo.note.utils.NotesUtility;
import com.bridgelabz.todo.user.factories.UserFactory;
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

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private NoteExtrasRepository noteExtrasRepository;

	@Autowired
	private UserFactory userFactory;
	
	@Override
	public NoteDto createNote(CreateNoteDto createNoteDto, long userId) {
		NotesUtility.validateNote(createNoteDto);

		Note note = noteFactory.getNoteFromCreateNoteDto(createNoteDto);
		
		Date createdAt = new Date();
		note.setCreatedAt(createdAt);

		NoteExtras extras = null;

		if (createNoteDto.getNoteExtras() != null) {
			extras = noteFactory.getNoteExtrasFromCreateNoteExtrasDto(createNoteDto.getNoteExtras());
		} else {
			extras = context.getBean(NoteExtras.class);
		}

		extras.setUpdatedAt(createdAt);
		extras.setNote(note);
		
		if (extras.getColor() == null || extras.getColor().isEmpty()) {
			extras.setColor("#FFFFFF");
		}
		
		List<NoteExtras> noteExtras = new LinkedList<>();
		noteExtras.add(extras);

		note.setNoteExtras(noteExtras);

		Optional<User> optionalUser = userRepository.findById(userId);

		User owner = optionalUser.get();

		note.setOwner(owner);
		extras.setOwner(owner);

		noteRepository.save(note);

		noteExtrasRepository.save(extras);
		
		NoteDto noteDto = noteFactory.getNoteDtoFromNote(note);
		NoteExtrasDto noteExtrasDto = noteFactory.getNoteExtrasDtoFromNoteExtras(extras);
		
		noteDto.setNoteExtras(noteExtrasDto);
		
		return noteDto;
	}

	@Override
	public void updateNote(UpdateNoteDto noteDto, long userId) {
		Optional<Note> optionalNote = noteRepository.findById(noteDto.getId());
		
		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Cannot find note with id " + noteDto.getId());
		}
		
		Note note = optionalNote.get();
		
		if (note.getOwner().getId() != userId) {
			throw new UnAuthorizedException("User does not own the note");
		}
		
		note.setTitle(noteDto.getTitle());
		note.setBody(noteDto.getBody());
		
		noteRepository.save(note);
		
		User owner = context.getBean(User.class);
		owner.setId(userId);
		
		NoteExtras extras = noteExtrasRepository.findByNoteAndOwner(note, owner);
		
		extras.setUpdatedAt(new Date());
		
		noteExtrasRepository.save(extras);
	}

}
