package com.bridgelabz.todo.note.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
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
import com.bridgelabz.todo.note.repositories.LabelTemplateRepository;
import com.bridgelabz.todo.note.repositories.NoteExtrasTemplateRepository;
import com.bridgelabz.todo.note.repositories.NoteTemplateRepository;
import com.bridgelabz.todo.note.utils.NotesUtility;
import com.bridgelabz.todo.user.exceptions.UserNotFoundException;
import com.bridgelabz.todo.user.factories.UserFactory;
import com.bridgelabz.todo.user.models.User;
import com.bridgelabz.todo.user.models.UserDto;
import com.bridgelabz.todo.user.repositories.UserTemplateRepository;

@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	private NoteFactory noteFactory;

	@Autowired
	private LabelTemplateRepository labelTemplateRepository;

	@Autowired
	private UserFactory userFactory;

	@Autowired
	private UserTemplateRepository userTemplateRepository;

	@Autowired
	private NoteTemplateRepository noteTemplateRepository;

	@Autowired
	private NoteExtrasTemplateRepository noteExtrasTemplateRepository;

	@Autowired
	private AsyncService asyncService;

	@Value("${collaborate.template.path}")
	private String collaborateTemplatePath;

	@Override
	public NoteDto createNote(CreateNoteDto createNoteDto, long userId, String origin)
			throws LabelNotFoundException, IOException, MessagingException {
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

		for (long labelId : createNoteDto.getLabels()) {
			Optional<Label> optionalLabel = labelTemplateRepository.findById(labelId);

			Label label = optionalLabel.get();

			if (label.getOwner().getId() == userId) {
				extras.getLabels().add(label);
			}
		}

		noteExtrasTemplateRepository.save(extras);

		List<UserDto> collaborators = new LinkedList<>();

		for (Long id : createNoteDto.getCollaborators()) {
			User user = userTemplateRepository.findById(id).get();
			NoteExtras extra = noteFactory.getDefaultNoteExtrasFromNoteAndUser(note, user);

			noteExtrasTemplateRepository.save(extra);

			UserDto dto = userFactory.getUserDtoFromUser(user);
			collaborators.add(dto);

			asyncService.sendEmailToCollaborator(owner, note, origin, user.getEmail());

		}

		NoteDto noteDto = noteFactory.getNoteDtoFromNoteAndExtras(note, extras);

		UserDto userDto = userFactory.getUserDtoFromUser(owner);

		noteDto.setOwner(userDto);
		noteDto.setCollaborators(collaborators);

		return noteDto;
	}

	@Override
	public void updateNote(UpdateNoteDto noteDto, long userId) {
		Optional<Note> optionalNote = noteTemplateRepository.findById(noteDto.getId());

		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Cannot find note with id " + noteDto.getId());
		}

		Note note = optionalNote.get();

		if (note.getOwner().getId() != userId) {
			boolean exists = noteExtrasTemplateRepository.checkIfExistsByNoteIdAndUserId(noteDto.getId(), userId);
			if(!exists) {
				throw new UnAuthorizedException("User does not own the note");
			}
		}

		if (noteDto.getTitle() != null) {
			note.setTitle(noteDto.getTitle().trim());
		}
		if (noteDto.getBody() != null) {
			note.setBody(noteDto.getBody().trim());
		}

		note.setUpdatedAt(new Date());

		noteTemplateRepository.save(note);
	}

	@Override
	public void deleteNote(long noteId, long userId) {
		Optional<Note> optionalNote = noteTemplateRepository.findById(noteId);

		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Cannot find note with id " + noteId);
		}

		Note note = optionalNote.get();

		if (note.getOwner().getId() != userId) {
			throw new UnAuthorizedException("User does not own the note");
		}

		noteExtrasTemplateRepository.deleteAllByNote(note);

		noteTemplateRepository.delete(note);
	}

	@Override
	public List<NoteDto> getAllNotes(long userId) {
		return noteTemplateRepository.getAllUserNotes(userId);
	}

	@Override
	public void changePinStatus(long noteId, boolean status, long userId) {

		Optional<NoteExtras> optionalNoteExtras = noteExtrasTemplateRepository.findByNoteIdAndOwnerId(noteId, userId);

		if (!optionalNoteExtras.isPresent()) {
			throw new NoteNotFoundException("Either user doesn't own the note or note doesn't exist");
		}

		NoteExtras noteExtras = optionalNoteExtras.get();

		if (status) {
			noteExtras.setArchived(false);
		}

		noteExtras.setPinned(status);

		noteExtrasTemplateRepository.save(noteExtras);
	}

	@Override
	public void changeArchiveStatus(long noteId, boolean status, long userId) {
		Optional<NoteExtras> optionalNoteExtras = noteExtrasTemplateRepository.findByNoteIdAndOwnerId(noteId, userId);

		if (!optionalNoteExtras.isPresent()) {
			throw new NoteNotFoundException("Either user doesn't own the note or note doesn't exist");
		}

		NoteExtras noteExtras = optionalNoteExtras.get();

		if (status) {
			noteExtras.setPinned(false);
		}

		noteExtras.setArchived(status);

		noteExtrasTemplateRepository.save(noteExtras);
	}

	@Override
	public void changeTrashStatus(long noteId, boolean status, long userId) {
		Optional<NoteExtras> optionalNoteExtras = noteExtrasTemplateRepository.findByNoteIdAndOwnerId(noteId, userId);

		if (!optionalNoteExtras.isPresent()) {
			throw new NoteNotFoundException("Either user doesn't own the note or note doesn't exist");
		}

		NoteExtras noteExtras = optionalNoteExtras.get();

		if (status) {
			noteExtras.setPinned(false);
		}

		noteExtras.setTrashed(status);

		noteExtrasTemplateRepository.save(noteExtras);
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
		Optional<Note> optionalNote = noteTemplateRepository.findById(id);

		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Cannot find note with id " + id);
		}

		Note note = optionalNote.get();

		if (note.getOwner().getId() != userId) {
			boolean exists = noteExtrasTemplateRepository.checkIfExistsByNoteIdAndUserId(id, userId);
			if(!exists) {
				throw new UnAuthorizedException("User does not own the note");
			}
		}

		String link = saveImage(image);
		link = url + link;

		// note.getImageUrls().add(link);
		note.setUpdatedAt(new Date());

		// noteRepository.save(note);
		noteTemplateRepository.save(note);
		noteTemplateRepository.addImage(note.getId(), link);

		return link;
	}

	@Override
	public void addReminder(long noteId, long time, long userId) {
		Optional<NoteExtras> optionalNoteExtras = noteExtrasTemplateRepository.findByNoteIdAndOwnerId(noteId, userId);

		if (!optionalNoteExtras.isPresent()) {
			throw new NoteNotFoundException("Either user doesn't own the note or note doesn't exist");
		}

		NoteExtras noteExtras = optionalNoteExtras.get();

		noteExtras.setReminder(new Date(time * 1000));

		noteExtrasTemplateRepository.save(noteExtras);
	}

	@Override
	public void removeReminnder(long noteId, long userId) {
		Optional<NoteExtras> optionalNoteExtras = noteExtrasTemplateRepository.findByNoteIdAndOwnerId(noteId, userId);

		if (!optionalNoteExtras.isPresent()) {
			throw new NoteNotFoundException("Either user doesn't own the note or note doesn't exist");
		}

		NoteExtras noteExtras = optionalNoteExtras.get();

		noteExtras.setReminder(null);

		noteExtrasTemplateRepository.save(noteExtras);
	}

	@Override
	public void deleteImage(String imagename) throws NoteIdRequredException, ImageDeletionException {
		// Optional<Note> optionalNote = noteRepository.getByImageUrl(imagename);
		//
		// if (optionalNote.isPresent()) {
		// throw new NoteIdRequredException("Image is attached to a note, please provide
		// note id");
		// }

		File file = new File("images/" + imagename.substring(imagename.lastIndexOf('/') + 1, imagename.length()));

		if (!file.delete()) {
			throw new ImageDeletionException("Image could not be deleted");
		}
	}
	
	@Override
	public void deleteImage(String imagename, long id, long userId) throws NoteIdRequredException, ImageDeletionException {
		Optional<Note> optionalNote = noteTemplateRepository.findById(id);

		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Cannot find note with id " + id);
		}

		Note note = optionalNote.get();

		if (note.getOwner().getId() != userId) {
			boolean exists = noteExtrasTemplateRepository.checkIfExistsByNoteIdAndUserId(id, userId);
			if(!exists) {
				throw new UnAuthorizedException("User does not own the note");
			}
		}
		
		deleteImage(imagename);
		
		noteTemplateRepository.deleteImage(id, imagename);
	}

	@Override
	public void changeColor(long noteId, String color, long userId) {
		Optional<NoteExtras> optionalNoteExtras = noteExtrasTemplateRepository.findByNoteIdAndOwnerId(noteId, userId);

		if (!optionalNoteExtras.isPresent()) {
			throw new NoteNotFoundException("Either user doesn't own the note or note doesn't exist");
		}

		NoteExtras noteExtras = optionalNoteExtras.get();

		noteExtras.setColor(color);

		noteExtrasTemplateRepository.save(noteExtras);
	}

	@Override
	public UserDto collaborate(long noteId, String emailId, long userId, String origin)
			throws UserNotFoundException, CollaborationException, MessagingException, IOException {
		Optional<Note> optionalNote = noteTemplateRepository.findById(noteId);

		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException("Note does not exist");
		}

		Note note = optionalNote.get();

		if (note.getOwner().getId() != userId) {
			boolean exists = noteExtrasTemplateRepository.checkIfExistsByNoteIdAndUserId(noteId, userId);
			if(!exists) {
				throw new UnAuthorizedException("User does not own the note");
			}
		}

		Optional<User> optionalUser = userTemplateRepository.findByEmail(emailId);
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException(String.format("User with email '%s' does not exist", emailId));
		}

		User user = optionalUser.get();

		Optional<NoteExtras> optionalNoteExtras = noteExtrasTemplateRepository.findByNoteIdAndOwnerId(noteId,
				user.getId());
		if (optionalNoteExtras.isPresent()) {
			throw new CollaborationException("User already collaborated");
		}

		NoteExtras extra = noteFactory.getDefaultNoteExtrasFromNoteAndUser(note, user);

		noteExtrasTemplateRepository.save(extra);

		User owner = userTemplateRepository.findById(userId).get();

		asyncService.sendEmailToCollaborator(owner, note, origin, emailId);

		return userFactory.getUserDtoFromUser(user);
	}
	
	@Override
	public void removeCollaborator(long noteId, long parseLong, long collaboratorId) {
		Optional<NoteExtras> optionalExtras = noteExtrasTemplateRepository.findByNoteIdAndOwnerId(noteId, collaboratorId);
		if(optionalExtras.isPresent()) {
			NoteExtras extras = optionalExtras.get();
			noteExtrasTemplateRepository.delete(extras.getId());
		}
	}

}
