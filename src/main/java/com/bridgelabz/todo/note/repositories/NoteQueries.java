package com.bridgelabz.todo.note.repositories;

public interface NoteQueries {

	String INSERT = "INSERT INTO note (title, body, created_at, updated_at, owner_id) VALUES (:title, :body, :created_at, :updated_at, :owner_id)";
	
	String UPDATE = "UPDATE note SET title = :title, body = :body, created_at = :created_at, updated_at = :updated_at, owner_id = :owner_id WHERE id = :id";
	
	String INSERT_IMAGE_URL = "INSERT INTO note_image_urls (note_id, image_urls) VALUES (:note_id, :image_urls)";

	String FETCH_BY_ID = "SELECT * FROM note WHERE id = :id";
	
	String DELETE_BY_ID = "DELETE FROM note WHERE id = :id";

}
