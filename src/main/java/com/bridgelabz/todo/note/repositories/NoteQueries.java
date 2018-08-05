package com.bridgelabz.todo.note.repositories;

public interface NoteQueries {

	String INSERT = "INSERT INTO note (title, body, created_at, updated_at, owner_id) VALUES (:title, :body, :created_at, :updated_at, :owner_id)";
	
	String UPDATE = "UPDATE note SET title = :title, body = :body, created_at = :created_at, updated_at = :updated_at, owner_id = :owner_id WHERE id = :id";
	
	String INSERT_IMAGE_URL = "INSERT INTO note_image_urls (note_id, image_urls) VALUES (:note_id, :image_urls)";

	String FETCH_BY_ID = "SELECT * FROM note WHERE id = :id";
	
	String DELETE_BY_ID = "DELETE FROM note WHERE id = :id";
	
	String FETCH_ALL_BY_USER_ID = "SELECT note.id, note.title, note.body, note.created_at, note.updated_at, note_extras.color, note_extras.is_archived, note_extras.is_pinned, note_extras.is_trashed, note_extras.reminder, note_image_urls.image_urls, label.id as l_id, label.name as l_name, user.id as o_id, user.firstname as o_firstname, user.lastname as o_lastname, user.email as o_email, user.profile_url as o_profile_url, c.id as c_id, c.firstname as c_firstname, c.lastname as c_lastname, c.email as c_email, c.profile_url as c_profile_url from note LEFT JOIN note_extras on note.owner_id = note_extras.owner_id AND note.id = note_extras.note_id LEFT JOIN note_image_urls ON note.id = note_image_urls.note_id LEFT JOIN note_label ON note_extras.id = note_label.neid LEFT JOIN label ON label.id = note_label.label_id LEFT JOIN user ON note.owner_id = user.id LEFT JOIN user as c ON note_extras.owner_id = user.id  WHERE note.owner_id = :owner_id;";

}
