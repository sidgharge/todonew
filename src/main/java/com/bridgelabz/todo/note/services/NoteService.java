package com.bridgelabz.todo.note.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.todo.note.exceptions.ImageDeletionException;
import com.bridgelabz.todo.note.exceptions.LabelNotFoundException;
import com.bridgelabz.todo.note.exceptions.NoteIdRequredException;
import com.bridgelabz.todo.note.models.CreateNoteDto;
import com.bridgelabz.todo.note.models.NoteDto;
import com.bridgelabz.todo.note.models.UpdateNoteDto;

public interface NoteService {

	NoteDto createNote(CreateNoteDto noteDto, long userId) throws LabelNotFoundException;

	void updateNote(UpdateNoteDto noteDto, long userId);

	void deleteNote(long noteId, long userId);

	List<NoteDto> getAllNotes(long userId);

	void changePinStatus(long noteId, boolean status, long userId);

	void changeArchiveStatus(long noteId, boolean status, long userId);

	void changeTrashStatus(long noteId, boolean status, long userId);

	String saveImage(MultipartFile image, long userId);

	void addReminder(long noteId, long time, long userId);

	void removeReminnder(long noteId, long userId);

	void deleteImage(String imagename) throws NoteIdRequredException, ImageDeletionException;

	void changeColor(long noteId, String color, long userId);
}
