package com.cache.server.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.cache.server.exception.AppException;
import com.cache.server.model.CacheObject;
import com.cache.server.model.CacheResp;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class CacheServiceUnitTest {

	@Mock
	private ConcurrentMap<String, Map<String, CacheObject>> map;

	@InjectMocks
	private CacheServiceImpl cacheService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCacheGetHappyPath() {
		lenient().when(this.map.containsKey(any(String.class))).thenReturn(true);

		Map<String, CacheObject> levelOneMap = new HashMap<>();
		levelOneMap.put("/add", new CacheObject("value", LocalDateTime.now()));
		lenient().when(this.map.get(any(String.class))).thenReturn(levelOneMap);

		lenient().when(this.map.get(any(String.class))).thenReturn(levelOneMap);

		assertNotNull(this.cacheService.get("Regisration", "/add"));
	}

	@Test(expected = AppException.class)
	public void testCacheGetUnappyPath() {
		lenient().when(this.map.containsKey(any(String.class))).thenReturn(true);
		lenient().when(this.map.get(any(String.class))).thenReturn(null);

		assertNotNull(this.cacheService.get("Regisration", "/add"));
	}

	@Test
	public void testCachePutHappyPath() {
		lenient().when(this.map.containsKey(any(String.class))).thenReturn(true);

		Map<String, CacheObject> levelOneMap = new HashMap<>();
		levelOneMap.put("/add", new CacheObject("value", LocalDateTime.now()));
		lenient().when(this.map.get(any(String.class))).thenReturn(levelOneMap);

		Set<String> set = new HashSet<>();
		set.add("Regisration");
		lenient().when(this.map.keySet()).thenReturn(set);

		boolean result = this.cacheService.put("Regisration", "/add", new CacheResp("OK"));
		assertEquals(true, result);
	}

	@Test(expected = AppException.class)
	public void testCacheDeleteUnHappyPath() {
		lenient().when(this.map.containsKey(any(String.class))).thenReturn(false);

		this.cacheService.delete("Regisration", "/add");

	}

	@Test
	public void testCacheDeleteHappyPath() {
		lenient().when(this.map.containsKey(any(String.class))).thenReturn(true);

		Map<String, CacheObject> levelOneMap = new HashMap<>();
		levelOneMap.put("/add", new CacheObject("value", LocalDateTime.now()));
		lenient().when(this.map.get(any(String.class))).thenReturn(levelOneMap);

		Set<String> set = new HashSet<>();
		set.add("Regisration");
		lenient().when(this.map.keySet()).thenReturn(set);

		boolean result = this.cacheService.delete("Regisration", "/add");

		assertEquals(true, result);

	}

}
