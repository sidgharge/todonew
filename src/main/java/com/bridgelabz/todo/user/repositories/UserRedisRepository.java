package com.bridgelabz.todo.user.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRedisRepository {
	
	@Value("${redis.user.token.key}")
	private String userTokenKey;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	public void save(String token, String userId) {
		redisTemplate.opsForHash().put(userTokenKey, token, userId);
	}
	
	public String get(String token) {
		String userId = null;
		
		Object userIdObject = redisTemplate.opsForHash().get(userTokenKey, token);
		
		if (userIdObject != null) {
			userId = (String) userIdObject;
		}
		
		return userId;
	}

	public void delete(String token) {
		redisTemplate.opsForHash().delete(userTokenKey, token);
	}
}
