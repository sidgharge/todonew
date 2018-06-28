package com.bridgelabz.todo.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgelabz.todo.user.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
}
