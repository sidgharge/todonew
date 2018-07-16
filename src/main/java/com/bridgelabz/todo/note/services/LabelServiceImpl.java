package com.bridgelabz.todo.note.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.todo.note.exceptions.LabelNameNotUniqueException;
import com.bridgelabz.todo.note.exceptions.LabelNotFoundException;
import com.bridgelabz.todo.note.factories.NoteFactory;
import com.bridgelabz.todo.note.models.Label;
import com.bridgelabz.todo.note.repositories.LabelDto;
import com.bridgelabz.todo.note.repositories.LabelRepository;
import com.bridgelabz.todo.user.models.User;

@Service
public class LabelServiceImpl implements LabelService {
	
	@Autowired
	private WebApplicationContext context;

	@Autowired
	private LabelRepository labelRepository;
	
	@Autowired
	private NoteFactory noteFactory;
	
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
		
		User owner = context.getBean(User.class);
		owner.setId(userId);
		Optional<Label> oldOptionalLabel = labelRepository.findByNameAndOwner(labelDto.getName(), owner);
		
		if (oldOptionalLabel.isPresent()) {
			throw new LabelNameNotUniqueException("Label with name " + labelDto.getName() + " already exists");
		}
		
		Label label = optionalLabel.get();
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

}
