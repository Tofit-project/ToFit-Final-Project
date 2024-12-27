package com.tofit.mvc.model.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

	private final RedisTemplate<String, Object> redisTemplate;

	public RedisService(RedisTemplate<String, Object> redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
	}
	
	public void setCode(String email, String code) {
		ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
		
		// 만료시간 3분
		valOperations.set(email, code, 180, TimeUnit.SECONDS);
	}
	
	public String getCode(String email) {
		ValueOperations<String, Object> valOperations = redisTemplate.opsForValue();
		Object code = valOperations.get(email);
		
		if(code == null) {
			return null;
		}
		
		return code.toString();
	}
}
