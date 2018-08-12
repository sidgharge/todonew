//package com.bridgelabz.todo.note.repositories;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.bridgelabz.todo.note.models.Label;
//import com.bridgelabz.todo.user.models.User;
//
//public interface LabelRepository extends JpaRepository<Label, Long> {
//
//	Optional<Label> findByNameAndOwner(String name, User owner);
//
//	List<Label> findByOwner(User owner);
//
//}
