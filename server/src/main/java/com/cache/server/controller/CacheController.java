package com.cache.server.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cache.server.model.CacheResp;
import com.cache.server.service.CacheService;

@RestController
public class CacheController {

	@Autowired
	private CacheService cacheService;

	@GetMapping("/get")
	public Object get(@RequestHeader("collection") String collection, @RequestHeader("key") String key) {

		return this.cacheService.get(collection, key);
	}

	@PutMapping("/put")
	public CacheResp put(@RequestHeader("collection") String collection, @RequestHeader("key") String key,
			@Valid @RequestBody Object value) {

		this.cacheService.put(collection, key, value);

		return new CacheResp("OK");
	}

	@DeleteMapping("/delete")
	public CacheResp delete(@RequestHeader("collection") String collection, @RequestHeader("key") String key) {

		this.cacheService.delete(collection, key);

		return new CacheResp("OK");
	}
}
