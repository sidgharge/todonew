package com.bridgelabz.todo.user.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.bridgelabz.todo.user.models.User;

@Repository
public class UserTemplateRepository {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	private UserMapper userMapper;

	public User save(User user) {
		KeyHolder holder = new GeneratedKeyHolder();

		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("firstname", user.getFirstname());
		paramMap.addValue("lastname", user.getLastname());
		paramMap.addValue("contact", user.getContact());
		paramMap.addValue("email", user.getEmail());
		paramMap.addValue("is_activated", user.isActivated());
		paramMap.addValue("password", user.getPassword());
		paramMap.addValue("role", user.getRole());
		paramMap.addValue("profile_url", user.getProfileUrl());

		if(user.getId() == 0) {
			jdbcTemplate.update(UserQueries.INSERT, paramMap, holder);

			user.setId(holder.getKey().longValue());
		} else {
			paramMap.addValue("id", user.getId());
			
			jdbcTemplate.update(UserQueries.UPDATE, paramMap);
		}
		return user;
	}

	public Optional<User> findByEmail(String email) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("email", email);

		return queryForObject(UserQueries.FETCH_BY_EMAIL, paramMap);
	}

	public Optional<User> findById(long id) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("id", id);

		return queryForObject(UserQueries.FETCH_BY_ID, paramMap);
	}

	private Optional<User> queryForObject(String sql, MapSqlParameterSource paramMap) {
		User user = null;
		try {
			user = jdbcTemplate.queryForObject(sql, paramMap, userMapper);
		} catch (EmptyResultDataAccessException e) {

		}
		return Optional.ofNullable(user);
	}
}

@Component
class UserMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();

		user.setId(rs.getInt("id"));
		user.setFirstname(rs.getString("firstname"));
		user.setLastname(rs.getString("lastname"));
		user.setContact(rs.getString("contact"));
		user.setEmail(rs.getString("email"));
		user.setActivated(rs.getBoolean("is_activated"));
		user.setPassword(rs.getString("password"));
		user.setRole(rs.getString("role"));
		user.setProfileUrl(rs.getString("profile_url"));

		return user;
	}

}
