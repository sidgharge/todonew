package com.bridgelabz.todo.note.repositories;

public interface NoteExtrasQueries {

	String DELETE_BY_NOTE_ID = "DELETE FROM note_extras WHERE note_id = :note_id";
	
	String FETCH_BY_NOTE_AND_OWNER = "SELECT * FROM note_extras WHERE note_id = :note_id AND owner_id = :owner_id";

	String INSERT = "INSERT INTO note_extras (color, is_archived, is_pinned, is_trashed, reminder, note_id, owner_id) VALUES (:color, :is_archived, :is_pinned, :is_trashed, :reminder, :note_id, :owner_id)";

	String UPDATE = "UPDATE note_extras SET color = :color, is_archived = :is_archived, is_pinned = :is_pinned, is_trashed = :is_trashed, reminder = :reminder, note_id = :note_id, owner_id = :owner_id WHERE id = :id";

	String ADD_LABEL = "INSERT INTO note_label (neid, label_id) VALUES (:neid, :label_id)";

	String REMOVE_LABEL = "DELETE FROM note_label WHERE neid = :neid AND label_id = :label_id";
}
