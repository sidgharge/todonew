package com.bridgelabz.todo.note.services;

import java.util.List;

import com.bridgelabz.todo.note.exceptions.LabelNameNotUniqueException;
import com.bridgelabz.todo.note.exceptions.LabelNotFoundException;
import com.bridgelabz.todo.note.repositories.LabelDto;

public interface LabelService {
	
	LabelDto createLabel(String name, long userId);

	void updateLabel(LabelDto labelDto, long userId) throws LabelNotFoundException, LabelNameNotUniqueException;

	List<LabelDto> getLabels(long userId);

}
