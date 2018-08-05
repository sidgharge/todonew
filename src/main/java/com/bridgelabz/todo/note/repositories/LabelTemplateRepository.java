package com.bridgelabz.todo.note.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
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

import com.bridgelabz.todo.note.models.Label;
import com.bridgelabz.todo.user.models.User;

@Repository
public class LabelTemplateRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	private LabelMapper labelMapper;

	public Label save(Label label) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("name", label.getName());
		paramMap.addValue("owner_id", label.getOwner().getId());
		
		if(label.getId() == 0) {
			KeyHolder holder = new GeneratedKeyHolder();
			
			jdbcTemplate.update(LabelQueries.INSERT, paramMap, holder);

			label.setId(holder.getKey().longValue());
		} else {
			paramMap.addValue("id", label.getId());
			
			jdbcTemplate.update(LabelQueries.UPDATE, paramMap);
		}
		return label;
	}
	
	public Optional<Label> findByNameAndOwnerId(String name, long ownerId) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("name", name);
		paramMap.addValue("owner_id", ownerId);

		return queryForObject(LabelQueries.FETCH_BY_NAME_AND_OWNER, paramMap);
	}
	
	public Optional<Label> findById(long id) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("id", id);

		return queryForObject(LabelQueries.FETCH_BY_ID, paramMap);
	}
	
	private Optional<Label> queryForObject(String sql, MapSqlParameterSource paramMap) {
		Label label = null;
		try {
			label = jdbcTemplate.queryForObject(sql, paramMap, labelMapper);
		} catch (EmptyResultDataAccessException e) {
			
		}
		return Optional.ofNullable(label);
	}
	
	public List<Label> findByOwnerId(long ownerId) {
		
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("owner_id", ownerId);
		
		return jdbcTemplate.query(LabelQueries.FETCH_BY_OWNER, paramMap, 
				new ResultSetExtractor<List<Label>>() {

			List<Label> labels = new LinkedList<>();
			
			@Override
			public List<Label> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()) {
					Label label = new Label();
					
					label.setId(rs.getInt("id"));
					label.setName(rs.getString("name"));
					
					labels.add(label);
				}
				
				return labels;
			}
		});
	}
	
	public List<Label> findByOwner(User owner) {
		return findByOwnerId(owner.getId());
	}

	public void delete(Label label) {
		deleteById(label.getId());
	}
	
	public void deleteById(long id) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("id", id);
		
		jdbcTemplate.update(LabelQueries.DELETE_BY_ID, paramMap);
		
		MapSqlParameterSource paramMap2 = new MapSqlParameterSource();
		paramMap2.addValue("label_id", id);
		
		jdbcTemplate.update(LabelQueries.DELETE_JOIN_BY_ID, paramMap2);
	}
}

@Component
class LabelMapper implements RowMapper<Label> {

	@Override
	public Label mapRow(ResultSet rs, int rowNum) throws SQLException {
		Label label = new Label();
		
		label.setId(rs.getInt("id"));
		label.setName(rs.getString("name"));
		
		User owner = new User();
		owner.setId(rs.getInt("owner_id"));
		label.setOwner(owner);
		
		return label;
	}
	
}
