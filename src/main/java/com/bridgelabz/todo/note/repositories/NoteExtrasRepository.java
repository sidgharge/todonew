package com.bridgelabz.todo.note.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgelabz.todo.note.models.NoteExtras;
import com.bridgelabz.todo.user.models.User;

public interface NoteExtrasRepository extends JpaRepository<NoteExtras, Long> {

	Optional<NoteExtras> findByOwner(User owner);
}
