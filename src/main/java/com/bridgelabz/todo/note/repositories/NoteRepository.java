package com.bridgelabz.todo.note.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgelabz.todo.note.models.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

}
