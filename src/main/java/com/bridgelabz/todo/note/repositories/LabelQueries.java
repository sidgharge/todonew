package com.bridgelabz.todo.note.repositories;

public interface LabelQueries {

	String INSERT = "INSERT INTO label (name, owner_id) VALUES (:name, :owner_id)";
	
	String UPDATE = "UPDATE label SET name = :name WHERE id = :id";
	
	String FETCH_BY_ID = "SELECT * FROM label WHERE id = :id";

	String FETCH_BY_NAME_AND_OWNER = "SELECT * FROM label WHERE name = :name AND owner_id = :owner_id";
	
	String FETCH_BY_OWNER = "SELECT * FROM label WHERE owner_id = :owner_id";

	String DELETE_BY_ID = "DELETE FROM label WHERE id = :id";

	String DELETE_JOIN_BY_ID = "DELETE FROM note_label WHERE label_id = :label_id";
}
