package com.bridgelabz.todo.user.repositories;

public interface UserQueries {

	String INSERT = "INSERT INTO user (firstname, lastname, contact, email, is_activated, password, role, profile_url) VALUES (:firstname, :lastname, :contact, :email, :is_activated, :password, :role, :profile_url)";
	
	String FETCH_BY_EMAIL = "SELECT * FROM user where email = :email";
	
	String FETCH_BY_ID = "SELECT * FROM user where id = :id";

	String UPDATE = "UPDATE user SET firstname = :firstname, lastname = :lastname, contact = :contact, email = :email, is_activated = :is_activated, password = :password, role = :role, profile_url = :profile_url WHERE id = :id";

}
