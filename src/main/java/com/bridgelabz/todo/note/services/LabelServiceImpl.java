package com.bridgelabz.todo.note.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.note.exceptions.LabelNameNotUniqueException;
import com.bridgelabz.todo.note.exceptions.LabelNotFoundException;
import com.bridgelabz.todo.note.exceptions.NoteNotFoundException;
import com.bridgelabz.todo.note.exceptions.UnAuthorizedException;
import com.bridgelabz.todo.note.factories.NoteFactory;
import com.bridgelabz.todo.note.models.CreateLabelDto;
import com.bridgelabz.todo.note.models.Label;
import com.bridgelabz.todo.note.models.LabelDto;
import com.bridgelabz.todo.note.models.NoteExtras;
import com.bridgelabz.todo.note.repositories.LabelTemplateRepository;
import com.bridgelabz.todo.note.repositories.NoteExtrasTemplateRepository;
import com.bridgelabz.todo.user.models.User;

@Service
public class LabelServiceImpl implements LabelService {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private NoteFactory noteFactory;
	
	@Autowired
	private LabelTemplateRepository labelTemplateRepository;
	
	@Autowired
	private NoteExtrasTemplateRepository noteExtrasTemplateRepository;

	@Override
	public LabelDto createLabel(CreateLabelDto createLabelDto, long userId) throws LabelNameNotUniqueException {
		User owner = context.getBean(User.class);
		owner.setId(userId);
		
		Optional<Label> optionalLabel = labelTemplateRepository.findByNameAndOwnerId(createLabelDto.getName(), owner.getId());
		
		if(optionalLabel.isPresent()) {
			throw new LabelNameNotUniqueException(String.format("Label with name '%s' already exists", createLabelDto.getName()));
		}
		
		Label label = new Label();
		label.setName(createLabelDto.getName());

		label.setOwner(owner);

		labelTemplateRepository.save(label);

		return noteFactory.getLabelDtoFromLabel(label);
	}

	@Override
	public void updateLabel(LabelDto labelDto, long userId) throws LabelNotFoundException, LabelNameNotUniqueException {
		Optional<Label> optionalLabel = labelTemplateRepository.findById(labelDto.getId());

		if (!optionalLabel.isPresent()) {
			throw new LabelNotFoundException("Label with id " + labelDto.getId() + " not found");
		}

		Label label = optionalLabel.get();
		if (label.getOwner().getId() != userId) {
			throw new UnAuthorizedException("User does not own the label");
		}

		
		Optional<Label> oldOptionalLabel = labelTemplateRepository.findByNameAndOwnerId(labelDto.getName(), userId);

		if(oldOptionalLabel.isPresent()) {
			throw new LabelNameNotUniqueException(String.format("Label with name '%s' already exists", labelDto.getName()));
		}

		label.setName(labelDto.getName());

		labelTemplateRepository.save(label);
	}

	@Override
	public List<LabelDto> getLabels(long userId) {
		List<Label> labels = labelTemplateRepository.findByOwnerId(userId);
		
		List<LabelDto> dtos = labels.stream().map(label -> noteFactory.getLabelDtoFromLabel(label)).collect(Collectors.toList());

		return dtos;
	}

	@Override
	public void deleteLabel(long id, long userId) throws LabelNotFoundException {
		Optional<Label> optionalLabel = labelTemplateRepository.findById(id);

		if (!optionalLabel.isPresent()) {
			throw new LabelNotFoundException("Label with id " + id + " does not exist");
		}

		Label label = optionalLabel.get();

		if (label.getOwner().getId() != userId) {
			throw new UnAuthorizedException("User does not own the label");
		}

		labelTemplateRepository.delete(label);
	}

	@Override
	public void addLabel(long labelId, long noteId, long userId) throws LabelNotFoundException {
		Optional<Label> optionalLabel = labelTemplateRepository.findById(labelId);

		if (!optionalLabel.isPresent()) {
			throw new LabelNotFoundException("Label with id " + labelId + " does not exist");
		}

		Label label = optionalLabel.get();

		if (label.getOwner().getId() != userId) {
			throw new UnAuthorizedException("User does not own the label");
		}
		
		Optional<NoteExtras> optionalExtras = noteExtrasTemplateRepository.findByNoteIdAndOwnerId(noteId, userId);
		
		if (!optionalExtras.isPresent()) {
			throw new NoteNotFoundException("Either user doesn't own the note or note doesn't exist");
		}
		
		noteExtrasTemplateRepository.addLabel(optionalExtras.get().getId(), labelId);
	}

	@Override
	public void removeLabel(long labelId, long noteId, long userId) throws LabelNotFoundException {
		Optional<Label> optionalLabel = labelTemplateRepository.findById(labelId);

		if (!optionalLabel.isPresent()) {
			throw new LabelNotFoundException("Label with id " + labelId + " does not exist");
		}

		Label label = optionalLabel.get();

		if (label.getOwner().getId() != userId) {
			throw new UnAuthorizedException("User does not own the label");
		}
		
		Optional<NoteExtras> optionalExtras = noteExtrasTemplateRepository.findByNoteIdAndOwnerId(noteId, userId);
		
		if (!optionalExtras.isPresent()) {
			throw new NoteNotFoundException("Either user doesn't own the note or note doesn't exist");
		}
		
		NoteExtras extras = optionalExtras.get();
		
		noteExtrasTemplateRepository.removeLabel(extras.getId(), labelId);
	}

}
