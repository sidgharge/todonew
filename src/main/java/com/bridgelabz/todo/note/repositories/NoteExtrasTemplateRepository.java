package com.bridgelabz.todo.note.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.bridgelabz.todo.note.models.Label;
import com.bridgelabz.todo.note.models.Note;
import com.bridgelabz.todo.note.models.NoteExtras;
import com.bridgelabz.todo.user.models.User;

@Repository
public class NoteExtrasTemplateRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	private ExtrasMapper extrasMapper;
	

	public NoteExtras save(NoteExtras extras) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("color", extras.getColor());
		paramMap.addValue("is_archived", extras.isArchived());
		paramMap.addValue("is_pinned", extras.isPinned());
		paramMap.addValue("is_trashed", extras.isTrashed());
		
		if(extras.getReminder() != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			paramMap.addValue("reminder", dateFormat.format(extras.getReminder()));
		} else {
			paramMap.addValue("reminder", null);
		}
		
		paramMap.addValue("note_id", extras.getNote().getId());
		paramMap.addValue("owner_id", extras.getOwner().getId());

		if(extras.getId() == 0) {
			KeyHolder holder = new GeneratedKeyHolder();
			
			jdbcTemplate.update(NoteExtrasQueries.INSERT, paramMap, holder);

			extras.setId(holder.getKey().longValue());
			
			for (Label label : extras.getLabels()) {
				addLabel(extras.getId(), label.getId());
			}
		} else {
			paramMap.addValue("id", extras.getId());
			
			jdbcTemplate.update(NoteExtrasQueries.UPDATE, paramMap);
		}
		
		return extras;
	}
	
	public void deleteAllByNote(Note note) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("note_id", note.getId());
		
		jdbcTemplate.update(NoteExtrasQueries.DELETE_BY_NOTE_ID, paramMap);
	}
	
	public Optional<NoteExtras> findByNoteIdAndOwnerId(long noteId, long ownerId) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("note_id", noteId);
		paramMap.addValue("owner_id", ownerId);

		return queryForObject(NoteExtrasQueries.FETCH_BY_NOTE_AND_OWNER, paramMap);
	}
	
	public Optional<NoteExtras> findById(long id) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("id", id);

		return queryForObject(NoteExtrasQueries.FIND_BY_ID, paramMap);
	}
	
	private Optional<NoteExtras> queryForObject(String sql, MapSqlParameterSource paramMap) {
		NoteExtras extras = null;
		try {
			extras = jdbcTemplate.queryForObject(sql, paramMap, extrasMapper);
		} catch (EmptyResultDataAccessException e) {

		}
		return Optional.ofNullable(extras);
	}
	
	public void addLabel(long extrasId, long labelId) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("neid", extrasId);
		paramMap.addValue("label_id", labelId);
		
		jdbcTemplate.update(NoteExtrasQueries.ADD_LABEL, paramMap);
	}
	
	public void removeLabel(long extrasId, long labelId) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("neid", extrasId);
		paramMap.addValue("label_id", labelId);
		
		jdbcTemplate.update(NoteExtrasQueries.REMOVE_LABEL, paramMap);
	}
	
	public void delete(long id) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("id", id);
		
		jdbcTemplate.update(NoteExtrasQueries.DELETE_BY_ID, paramMap);
	}
}

@Component
class ExtrasMapper implements RowMapper<NoteExtras> {

	@Override
	public NoteExtras mapRow(ResultSet rs, int rowNum) throws SQLException {
		NoteExtras extras = new NoteExtras();
		
		extras.setId(rs.getInt("id"));
		extras.setColor(rs.getString("color"));
		extras.setArchived(rs.getBoolean("is_archived"));
		extras.setPinned(rs.getBoolean("is_pinned"));
		extras.setTrashed(rs.getBoolean("is_trashed"));
		
		if(rs.getString("reminder") != null) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				extras.setReminder(dateFormat.parse(rs.getString("reminder")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		Note note = new Note();
		note.setId(rs.getInt("note_id"));
		extras.setNote(note);
		
		User owner = new User();
		owner.setId(rs.getInt("owner_id"));
		extras.setOwner(owner);
		
		return extras;
	}
	
}
