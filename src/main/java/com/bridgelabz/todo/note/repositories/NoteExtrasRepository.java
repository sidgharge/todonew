package com.bridgelabz.todo.note.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgelabz.todo.note.models.NoteExtras;

public interface NoteExtrasRepository extends JpaRepository<NoteExtras, Long> {

}
