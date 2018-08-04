package com.bridgelabz.todo.note.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.todo.note.exceptions.CollaborationException;
import com.bridgelabz.todo.note.exceptions.ImageDeletionException;
import com.bridgelabz.todo.note.exceptions.LabelNotFoundException;
import com.bridgelabz.todo.note.exceptions.NoteIdRequredException;
import com.bridgelabz.todo.note.exceptions.NoteNotFoundException;
import com.bridgelabz.todo.note.exceptions.UnAuthorizedException;
import com.bridgelabz.todo.note.factories.NoteFactory;
import com.bridgelabz.todo.note.models.Note;
import com.bridgelabz.todo.note.models.NoteDto;
import com.bridgelabz.todo.note.models.NoteExtras;
import com.bridgelabz.todo.note.models.UpdateNoteDto;
import com.bridgelabz.todo.note.models.CreateNoteDto;
import com.bridgelabz.todo.note.models.Label;
import com.bridgelabz.todo.note.repositories.LabelRepository;
import com.bridgelabz.todo.note.repositories.NoteExtrasRepository;
import com.bridgelabz.todo.note.repositories.NoteRepository;
import com.bridgelabz.todo.note.repositories.NoteTemplateRepository;
import com.bridgelabz.todo.note.utils.NotesUtility;
import com.bridgelabz.todo.user.exceptions.UserNotFoundException;
import com.bridgelabz.todo.user.factories.UserFactory;
import com.bridgelabz.todo.user.models.User;
import com.bridgelabz.todo.user.models.UserDto;
import com.bridgelabz.todo.user.repositories.UserRepository;
import com.bridgelabz.todo.user.repositories.UserTemplateRepository;

@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	private NoteFactory noteFactory;

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private NoteExtrasRepository noteExtrasRepository;

	@Autowired
	private LabelRepository labelRepository;
	
	@Autowired
	private UserFactory userFactory;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserTemplateRepository userTemplateRepository;
	
	@Autowired
	private NoteTemplateRepository noteTemplateRepository;
	
	@Override
	public NoteDto createNote(CreateNoteDto createNoteDto, long userId) throws LabelNotFoundException {
		NotesUtility.validateNote(createNoteDto);

		Note note = noteFactory.getNoteFromCreateNoteDto(createNoteDto);

		Date createdAt = new Date();
		note.setCreatedAt(createdAt);
		note.setUpdatedAt(createdAt);

		User owner = userTemplateRepository.findById(userId).get();

		note.setOwner(owner);
		NoteExtras extras = note.getNoteExtras().get(0);
		extras.setOwner(owner);

		noteTemplateRepository.save(note);

		extras.setLabels(new HashSet<>());
		if (createNoteDto.getLabels() != null) {
			for (long labelId : createNoteDto.getLabels()) {
				Optional<Label> optionalLabel = labelRepository.findById(labelId);

				Label label = optionalLabel.get();

				if (label.getOwner().getId() == userId) {
					extras.getLabels().add(label);
				}
			}
		}
		
		noteExtrasRepository.save(extras);

		List<UserDto> collaborators = new LinkedList<>();
		
		if (createNoteDto.getCollaborators() != null) {
			createNoteDto.getCollaborators().forEach(id -> {
				User user = userTemplateRepository.findById(id).get();
				NoteExtras extra = noteFactory.getDefaultNoteExtrasFromNoteAndUser(note, user);
				
				noteExtrasRepository.save(extra);
				
				UserDto dto = userFactory.getUserDtoFromUser(user);
				collaborators.add(dto);
			});	
		}


		NoteDto noteDto = noteFactory.getNoteDtoFromNoteAndExtras(note, extras);
		
		UserDto userDto =userFactory.getUserDtoFromUser(owner);
		
		noteDto.setOwner(userDto);
		noteDto.setCollaborators(collaborators);

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

		if(noteDto.getTitle() != null) {
			note.setTitle(noteDto.getTitle().trim());
		}
		if(noteDto.getBody() != null) {
			note.setBody(noteDto.getBody().trim());
		}
		
		note.setUpdatedAt(new Date());

		noteRepository.save(note);
	}

	@Override
	public void deleteNote(long noteId, long userId) {
		Optional<Note> optionalNote = noteRepository.findById(noteId);

		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Cannot find note with id " + noteId);
		}

		Note note = optionalNote.get();

		for (NoteExtras noteExtras : note.getNoteExtras()) {
			noteExtrasRepository.delete(noteExtras);
		}
		
		noteRepository.delete(note);
	}

	
	@Override
	public List<NoteDto> getAllNotes(long userId) {
		User owner = context.getBean(User.class);
		owner.setId(userId);
		
		List<NoteExtras> extras = noteExtrasRepository.findByOwner(owner);
		
		List<NoteDto> noteDtos = new LinkedList<>();
		for (NoteExtras noteExtras : extras) {
			NoteDto noteDto = noteFactory.getNoteDtoFromNoteAndExtras(noteExtras.getNote(), noteExtras);
			noteDto.setCollaborators(new ArrayList<>());
			noteExtras.getNote().getNoteExtras().forEach(noteExtra -> {
				if (noteExtra.getOwner().getId() != noteExtras.getNote().getOwner().getId()) {
					UserDto userDto = userFactory.getUserDtoFromUser(noteExtra.getOwner());
					noteDto.getCollaborators().add(userDto);
				}
			});
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
	public String saveImage(MultipartFile image) {
		String ext = image.getOriginalFilename();
		ext = ext.substring(ext.lastIndexOf('.'));
		File file = new File("images/" + UUID.randomUUID().toString() + ext);
		try (InputStream inputStream = image.getInputStream(); OutputStream outputStream = new FileOutputStream(file)) {
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

	@Override
	public String saveImageToNote(MultipartFile image, long id, String url, long userId) {
		Optional<Note> optionalNote = noteRepository.findById(id);

		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Cannot find note with id " + id);
		}

		Note note = optionalNote.get();

		if (note.getOwner().getId() != userId) {
			throw new UnAuthorizedException("User does not own the note");
		}
		
		String link = saveImage(image);
		link = url + link;
		
		note.getImageUrls().add(link);
		note.setUpdatedAt(new Date());
		
		noteRepository.save(note);
		
		return link;
	}

	@Override
	public void addReminder(long noteId, long time, long userId) {
		Note note = context.getBean(Note.class);
		note.setId(noteId);

		User owner = context.getBean(User.class);
		owner.setId(userId);

		NoteExtras noteExtras = noteExtrasRepository.findByNoteAndOwner(note, owner);

		if (noteExtras == null) {
			throw new NoteNotFoundException("Either user doesn't own the note or note doesn't exist");
		}

		noteExtras.setReminder(new Date(time * 1000));

		noteExtrasRepository.save(noteExtras);
	}

	@Override
	public void removeReminnder(long noteId, long userId) {
		Note note = context.getBean(Note.class);
		note.setId(noteId);

		User owner = context.getBean(User.class);
		owner.setId(userId);

		NoteExtras noteExtras = noteExtrasRepository.findByNoteAndOwner(note, owner);

		if (noteExtras == null) {
			throw new NoteNotFoundException("Either user doesn't own the note or note doesn't exist");
		}

		noteExtras.setReminder(null);

		noteExtrasRepository.save(noteExtras);
	}

	@Override
	public void deleteImage(String imagename) throws NoteIdRequredException, ImageDeletionException {
//		Optional<Note> optionalNote = noteRepository.getByImageUrl(imagename);
//		
//		if (optionalNote.isPresent()) {
//			throw new NoteIdRequredException("Image is attached to a note, please provide note id");
//		}
		
		File file = new File("images/" + imagename.substring(imagename.lastIndexOf('/') + 1, imagename.length()));
		
		if (!file.delete()) {
			throw new ImageDeletionException("Image could not be deleted");
		}
	}

	@Override
	public void changeColor(long noteId, String color, long userId) {
		Note note = context.getBean(Note.class);
		note.setId(noteId);

		User owner = context.getBean(User.class);
		owner.setId(userId);

		NoteExtras noteExtras = noteExtrasRepository.findByNoteAndOwner(note, owner);

		if (noteExtras == null) {
			throw new NoteNotFoundException("Either user doesn't own the note or note doesn't exist");
		}
		
		noteExtras.setColor(color);

		noteExtrasRepository.save(noteExtras);
	}

	@Override
	public UserDto collaborate(long noteId, String email, long userId) throws UserNotFoundException, CollaborationException {
		Optional<Note> optionalNote = noteRepository.findById(noteId);
		
		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Note does not exist");
		}
		
		Note note = optionalNote.get();
		
		if (note.getOwner().getId() != userId) {
			throw new UnAuthorizedException("User does not own the note");
		}
		
		Optional<User> optionalUser = userTemplateRepository.findByEmail(email);
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException(String.format("User with email '%s' does not exist", email));
		}
		
		User user = optionalUser.get();
		
		NoteExtras noteExtras = noteExtrasRepository.findByNoteAndOwner(note, user);
		if (noteExtras != null) {
			throw new CollaborationException("User already collaborated");
		}
		
		NoteExtras extra = noteFactory.getDefaultNoteExtrasFromNoteAndUser(note, user);
		
		noteExtrasRepository.save(extra);
		
		return userFactory.getUserDtoFromUser(user);
	}
	
}
