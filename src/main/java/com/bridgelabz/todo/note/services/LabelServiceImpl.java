package com.bridgelabz.todo.note.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.note.exceptions.LabelNameNotUniqueException;
import com.bridgelabz.todo.note.exceptions.LabelNotFoundException;
import com.bridgelabz.todo.note.exceptions.NoteNotFoundException;
import com.bridgelabz.todo.note.exceptions.UnAuthorizedException;
import com.bridgelabz.todo.note.factories.NoteFactory;
import com.bridgelabz.todo.note.models.Label;
import com.bridgelabz.todo.note.models.LabelDto;
import com.bridgelabz.todo.note.models.Note;
import com.bridgelabz.todo.note.models.NoteExtras;
import com.bridgelabz.todo.note.repositories.LabelRepository;
import com.bridgelabz.todo.note.repositories.NoteExtrasRepository;
import com.bridgelabz.todo.user.models.User;

@Service
public class LabelServiceImpl implements LabelService {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private LabelRepository labelRepository;

	@Autowired
	private NoteFactory noteFactory;
	
	@Autowired
	private NoteExtrasRepository noteExtrasRepository;

	@Override
	public LabelDto createLabel(String name, long userId) {
		Label label = new Label();
		label.setName(name);

		User owner = context.getBean(User.class);
		owner.setId(userId);

		label.setOwner(owner);

		labelRepository.save(label);

		return noteFactory.getLabelDtoFromLabel(label);
	}

	@Override
	public void updateLabel(LabelDto labelDto, long userId) throws LabelNotFoundException, LabelNameNotUniqueException {
		Optional<Label> optionalLabel = labelRepository.findById(labelDto.getId());

		if (!optionalLabel.isPresent()) {
			throw new LabelNotFoundException("Label with id " + labelDto.getId() + " not found");
		}

		Label label = optionalLabel.get();
		if (label.getOwner().getId() != userId) {
			throw new UnAuthorizedException("User does not own the label");
		}

		User owner = context.getBean(User.class);
		owner.setId(userId);
		Optional<Label> oldOptionalLabel = labelRepository.findByNameAndOwner(labelDto.getName(), owner);

		if (oldOptionalLabel.isPresent()) {
			throw new LabelNameNotUniqueException("Label with name " + labelDto.getName() + " already exists");
		}

		label.setName(labelDto.getName());

		labelRepository.save(label);
	}

	@Override
	public List<LabelDto> getLabels(long userId) {
		User owner = context.getBean(User.class);
		owner.setId(userId);

		List<Label> labels = labelRepository.findByOwner(owner);

		List<LabelDto> dtos = new LinkedList<>();

		for (Label label : labels) {
			LabelDto dto = noteFactory.getLabelDtoFromLabel(label);
			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	public void deleteLabel(long id, long userId) throws LabelNotFoundException {
		Optional<Label> optionalLabel = labelRepository.findById(id);

		if (!optionalLabel.isPresent()) {
			throw new LabelNotFoundException("Label with id " + id + " does not exist");
		}

		Label label = optionalLabel.get();

		if (label.getOwner().getId() != userId) {
			throw new UnAuthorizedException("User does not own the label");
		}

		labelRepository.delete(label);
	}

	@Override
	public void addLabel(long labelId, long noteId, long userId) throws LabelNotFoundException {
		Optional<Label> optionalLabel = labelRepository.findById(labelId);

		if (!optionalLabel.isPresent()) {
			throw new LabelNotFoundException("Label with id " + labelId + " does not exist");
		}

		Label label = optionalLabel.get();

		if (label.getOwner().getId() != userId) {
			throw new UnAuthorizedException("User does not own the label");
		}
		
		Note note = context.getBean(Note.class);
		note.setId(noteId);
		
		User owner = context.getBean(User.class);
		owner.setId(userId);
		
		NoteExtras extras = noteExtrasRepository.findByNoteAndOwner(note, owner);
		
		if (extras == null) {
			throw new NoteNotFoundException("Either user doesn't own the note or note doesn't exist");
		}
		
		extras.getLabels().add(label);
		
		noteExtrasRepository.save(extras);
	}

	@Override
	public void removeLabel(long labelId, long noteId, long userId) throws LabelNotFoundException {
		Optional<Label> optionalLabel = labelRepository.findById(labelId);

		if (!optionalLabel.isPresent()) {
			throw new LabelNotFoundException("Label with id " + labelId + " does not exist");
		}

		Label label = optionalLabel.get();

		if (label.getOwner().getId() != userId) {
			throw new UnAuthorizedException("User does not own the label");
		}
		
		Note note = context.getBean(Note.class);
		note.setId(noteId);
		
		User owner = context.getBean(User.class);
		owner.setId(userId);
		
		NoteExtras extras = noteExtrasRepository.findByNoteAndOwner(note, owner);
		
		if (extras == null) {
			throw new NoteNotFoundException("Either user doesn't own the note or note doesn't exist");
		}
		
		extras.getLabels().remove(label);
		
		noteExtrasRepository.save(extras);
	}

}
