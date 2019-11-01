package com.cache.server.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cache.server.model.CacheObject;

@Configuration
public class CacheConfig {

	@Bean
	public ConcurrentMap<String, Map<String, CacheObject>> getConcurrentMap() {

		return new ConcurrentHashMap<>();
	}
}
