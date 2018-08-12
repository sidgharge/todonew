//package com.bridgelabz.todo.note.repositories;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.bridgelabz.todo.note.models.Note;
//import com.bridgelabz.todo.note.models.NoteExtras;
//import com.bridgelabz.todo.user.models.User;
//
//public interface NoteExtrasRepository extends JpaRepository<NoteExtras, Long> {
//
//	NoteExtras findByNoteAndOwner(Note note, User owner);
//
//	List<NoteExtras> findByOwner(User owner);
//}
