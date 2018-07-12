package com.bridgelabz.todo.note.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
		note.setUpdatedAt(createdAt);

		NoteExtras extras = null;

		if (createNoteDto.getNoteExtras() != null) {
			extras = noteFactory.getNoteExtrasFromCreateNoteExtrasDto(createNoteDto.getNoteExtras());
		} else {
			extras = context.getBean(NoteExtras.class);
		}

		extras.setNote(note);

		if (extras.getColor() == null || extras.getColor().isEmpty()) {
			extras.setColor("#FFFFFF");
		}

		List<NoteExtras> noteExtras = new LinkedList<>();
		noteExtras.add(extras);

		note.setNoteExtras(noteExtras);

		User owner = context.getBean(User.class);
		owner.setId(userId);

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
		note.setUpdatedAt(new Date());

		noteRepository.save(note);
	}

	@Override
	public void deleteNote(long noteId, long userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<NoteDto> getAllNotes(long userId) {
		User owner = context.getBean(User.class);
		owner.setId(userId);

		List<Note> notes = noteRepository.findByOwner(owner);

		List<NoteDto> noteDtos = new LinkedList<>();
		for (Note note : notes) {
			NoteDto noteDto = noteFactory.getNoteDtoFromNote(note);

			NoteExtras noteExtras = noteExtrasRepository.findByNoteAndOwner(note, owner);
			NoteExtrasDto noteExtrasDto = noteFactory.getNoteExtrasDtoFromNoteExtras(noteExtras);

			noteDto.setNoteExtras(noteExtrasDto);

			noteDtos.add(noteDto);
		}

		return noteDtos;
	}

	@Override
	public void changePinStatus(long noteId, boolean status, long userId) {
		Note note = context.getBean(Note.class);
		note.setId(noteId);

		User owner = context.getBean(User.class);
		owner.setId(userId);

		NoteExtras noteExtras = noteExtrasRepository.findByNoteAndOwner(note, owner);

		if (noteExtras == null) {
			throw new NoteNotFoundException("Either user doesn't own the note or note doesn't exist");
		}

		if (status) {
			noteExtras.setArchived(false);
		}

		noteExtras.setPinned(status);

		noteExtrasRepository.save(noteExtras);
	}

	@Override
	public void changeArchiveStatus(long noteId, boolean status, long userId) {
		Note note = context.getBean(Note.class);
		note.setId(noteId);

		User owner = context.getBean(User.class);
		owner.setId(userId);

		NoteExtras noteExtras = noteExtrasRepository.findByNoteAndOwner(note, owner);

		if (noteExtras == null) {
			throw new NoteNotFoundException("Either user doesn't own the note or note doesn't exist");
		}

		if (status) {
			noteExtras.setPinned(false);
		}

		noteExtras.setArchived(status);

		noteExtrasRepository.save(noteExtras);
	}

	@Override
	public void changeTrashStatus(long noteId, boolean status, long userId) {
		Note note = context.getBean(Note.class);
		note.setId(noteId);

		User owner = context.getBean(User.class);
		owner.setId(userId);

		NoteExtras noteExtras = noteExtrasRepository.findByNoteAndOwner(note, owner);

		if (noteExtras == null) {
			throw new NoteNotFoundException("Either user doesn't own the note or note doesn't exist");
		}

		if (status) {
			noteExtras.setPinned(false);
		}

		noteExtras.setTrashed(status);

		noteExtrasRepository.save(noteExtras);
	}

	@Override
	public String saveImage(MultipartFile image, long userId) {
		String ext = image.getOriginalFilename();
		ext = ext.substring(ext.lastIndexOf('.'));
		File file = new File("images/" + UUID.randomUUID().toString() + ext);
		try(InputStream inputStream = image.getInputStream(); OutputStream outputStream = new FileOutputStream(file)) {
			if (!file.exists()) {
				file.createNewFile();
			}

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "images/" + file.getName();
	}

	// @Override
	// public void deleteNote(long noteId, long userId) {
	// Optional<Note> optionalNote = noteRepository.findById(noteId);
	//
	// if (!optionalNote.isPresent()) {
	// throw new NoteNotFoundException("Cannot find note with id " + noteId);
	// }
	//
	// Note note = optionalNote.get();
	//
	// if (note.getOwner().getId() != userId) {
	// throw new UnAuthorizedException("User does not own the note");
	// }
	//
	// noteRepository.delete(note);
	//
	// }

}
