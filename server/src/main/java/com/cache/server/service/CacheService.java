package com.cache.server.service;

public interface CacheService {

	public Object get(String collection, String key);

	public boolean put(String collection, String key, Object value);

	public boolean delete(String collection, String key);
}
