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

import com.bridgelabz.todo.note.models.Note;
import com.bridgelabz.todo.user.models.User;

@Repository
public class NoteTemplateRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	private NoteMapper noteMapper;
	
	

	public Note save(Note note) {
		KeyHolder holder = new GeneratedKeyHolder();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("title", note.getTitle());
		paramMap.addValue("body", note.getBody());
		paramMap.addValue("created_at", dateFormat.format(note.getCreatedAt()));
		paramMap.addValue("updated_at", dateFormat.format(note.getUpdatedAt()));
		paramMap.addValue("owner_id", note.getOwner().getId());

		if(note.getId() == 0) {
			jdbcTemplate.update(NoteQueries.INSERT, paramMap, holder);

			note.setId(holder.getKey().longValue());
			
			for (String imageUrl : note.getImageUrls()) {
				addImage(note.getId(), imageUrl);
			}
		} else {
			paramMap.addValue("id", note.getId());
			
			jdbcTemplate.update(NoteQueries.UPDATE, paramMap);
		}
		return note;
	}
	
	public void addImage(long noteId, String imageUrl) {
		MapSqlParameterSource imageParamMap = new MapSqlParameterSource();
		imageParamMap.addValue("note_id", noteId);
		imageParamMap.addValue("image_urls", imageUrl);
		
		jdbcTemplate.update(NoteQueries.INSERT_IMAGE_URL, imageParamMap);
	}
	
	public void delete(Note note) {
		deleteById(note.getId());
	}
	
	public void deleteById(long id) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("id", id);
		
		jdbcTemplate.update(NoteQueries.DELETE_BY_ID, paramMap);
	}
	
	
	public Optional<Note> findById(long id) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("id", id);

		return queryForObject(NoteQueries.FETCH_BY_ID, paramMap);
	}
	
	private Optional<Note> queryForObject(String sql, MapSqlParameterSource paramMap) {
		Note note = null;
		try {
			note = jdbcTemplate.queryForObject(sql, paramMap, noteMapper);
		} catch (EmptyResultDataAccessException e) {

		}
		return Optional.ofNullable(note);
	}
}

@Component
class NoteMapper implements RowMapper<Note> {

	@Override
	public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Note note = new Note();
		
		note.setId(rs.getInt("id"));
		note.setTitle(rs.getString("title"));
		note.setBody(rs.getString("body"));
		
		try {
			note.setCreatedAt(dateFormat.parse(rs.getString("created_at")));
			note.setUpdatedAt(dateFormat.parse(rs.getString("updated_at")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		User owner = new User();
		owner.setId(rs.getInt("owner_id"));
		
		note.setOwner(owner);
		return note;
	}
	
}
