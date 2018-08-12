package com.bridgelabz.todo.note.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.bridgelabz.todo.note.models.LabelDto;
import com.bridgelabz.todo.note.models.Note;
import com.bridgelabz.todo.note.models.NoteDto;
import com.bridgelabz.todo.user.models.User;
import com.bridgelabz.todo.user.models.UserDto;

@Repository
public class NoteTemplateRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	private NoteMapper noteMapper;
	
	

	public Note save(Note note) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("title", note.getTitle());
		paramMap.addValue("body", note.getBody());
		paramMap.addValue("created_at", dateFormat.format(note.getCreatedAt()));
		paramMap.addValue("updated_at", dateFormat.format(note.getUpdatedAt()));
		paramMap.addValue("owner_id", note.getOwner().getId());

		if(note.getId() == 0) {
			KeyHolder holder = new GeneratedKeyHolder();
			
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
	
	public void deleteImage(long id, String url) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("note_id", id);
		paramMap.addValue("image_urls", url);
		
		jdbcTemplate.update(NoteQueries.DELETE_NOTE_IMAGE, paramMap);
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
	
	public List<NoteDto> getAllUserNotes(long userId) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("owner_id", userId);
		
		return jdbcTemplate.query(NoteQueries.FETCH_ALL_BY_USER_ID, paramMap, new ResultSetExtractor<List<NoteDto>>() {

			Map<Long, NoteDto> noteMap = new HashMap<>();
			
			@Override
			public List<NoteDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				while(rs.next()) {
					NoteDto note = noteMap.get(rs.getLong("id"));
					
					if(note == null) {
						note = new NoteDto();
						
						note.setId(rs.getLong("id"));
						note.setTitle(rs.getString("title"));
						note.setBody(rs.getString("body"));
						try {
							if(rs.getString("created_at") != null) {
								note.setCreatedAt(dateFormat.parse(rs.getString("created_at")));
							}
							if(rs.getString("updated_at") != null) {
								note.setUpdatedAt(dateFormat.parse(rs.getString("updated_at")));
							}
							if(rs.getString("reminder") != null) {
								note.setReminder(dateFormat.parse(rs.getString("reminder")));
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
						
						UserDto owner = new UserDto();
						owner.setId(rs.getLong("o_id"));
						owner.setFirstname(rs.getString("o_firstname"));
						owner.setLastname(rs.getString("o_lastname"));
						owner.setEmail(rs.getString("o_email"));
						owner.setProfileUrl(rs.getString("o_profile_url"));
						note.setOwner(owner);
						
						note.setColor(rs.getString("color"));
						note.setArchived(rs.getBoolean("is_archived"));
						note.setPinned(rs.getBoolean("is_pinned"));
						note.setTrashed(rs.getBoolean("is_trashed"));
						
						noteMap.put(note.getId(), note);
						
					}
					
					if(!note.getImageUrls().contains(rs.getString("image_urls")) && (rs.getString("image_urls") != null)) {
						note.getImageUrls().add(rs.getString("image_urls"));
					}
					
					if(rs.getString("c_id") != null) {
						long collabId = rs.getLong("c_id");
						boolean collab = note.getCollaborators().stream().anyMatch(user -> user.getId() == collabId);
						
						if(!collab) {
							UserDto dto = new UserDto();
							dto.setId(rs.getLong("c_id"));
							dto.setFirstname(rs.getString("c_firstname"));
							dto.setLastname(rs.getString("c_lastname"));
							dto.setEmail(rs.getString("c_email"));
							dto.setProfileUrl(rs.getString("c_profile_url"));
							
							note.getCollaborators().add(dto);
						}
					}
					
					if(rs.getString("l_id") != null) {
						long labelId = rs.getLong("l_id");
						boolean lab = note.getLabels().stream().anyMatch(label -> label.getId() == labelId);
						
						if(!lab) {
							LabelDto dto = new LabelDto();
							
							dto.setId(rs.getLong("l_id"));
							dto.setName(rs.getString("l_name"));
							
							note.getLabels().add(dto);
						}
					}
					
				}
				return new ArrayList<>(noteMap.values());
			}
		});
	}
}

@Component
class NoteMapper implements RowMapper<Note> {

	@Override
	public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Note note = new Note();
		
		note.setId(rs.getLong("id"));
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
