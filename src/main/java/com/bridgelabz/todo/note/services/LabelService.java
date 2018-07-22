package com.bridgelabz.todo.note.services;

import java.util.List;

import com.bridgelabz.todo.note.exceptions.LabelNameNotUniqueException;
import com.bridgelabz.todo.note.exceptions.LabelNotFoundException;
import com.bridgelabz.todo.note.models.CreateLabelDto;
import com.bridgelabz.todo.note.models.LabelDto;

public interface LabelService {
	
	LabelDto createLabel(CreateLabelDto createLabelDto, long userId) throws LabelNameNotUniqueException;

	void updateLabel(LabelDto labelDto, long userId) throws LabelNotFoundException, LabelNameNotUniqueException;

	List<LabelDto> getLabels(long userId);
	
	void deleteLabel(long id, long userId) throws LabelNotFoundException;

	void addLabel(long labelId, long noteId, long userId) throws LabelNotFoundException;

	void removeLabel(long labelId, long noteId, long userId) throws LabelNotFoundException;

}
