package com.cache.server.model;

import java.time.LocalDateTime;

public class CacheObject {

	private Object value;

	private LocalDateTime createdAt;

	public CacheObject() {
		super();
	}

	public CacheObject(Object value, LocalDateTime createdAt) {
		super();
		this.value = value;
		this.createdAt = createdAt;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
