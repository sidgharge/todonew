package com.bridgelabz.todo.note.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgelabz.todo.note.models.Note;
import com.bridgelabz.todo.user.models.User;

public interface NoteRepository extends JpaRepository<Note, Long> {

	List<Note> findByOwner(User owner);

	Optional<Note> getByImageUrl(String imagename);

}
