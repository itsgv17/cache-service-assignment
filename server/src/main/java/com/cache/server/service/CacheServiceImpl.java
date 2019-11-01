package com.cache.server.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cache.server.constant.AppLevelErrorConstant;
import com.cache.server.exception.AppException;
import com.cache.server.exception.ErrorLevel;
import com.cache.server.exception.ErrorType;
import com.cache.server.exception.ExceptionResponse;
import com.cache.server.model.CacheObject;

@Service
public class CacheServiceImpl implements CacheService {

	@Autowired
	ConcurrentMap<String, Map<String, CacheObject>> twoLevelMap;

	@Value("${cache.max.element-count}")
	private int maxCacheSize = 1000;

	@Value("${cache.expiration.hours}")
	private int hours = 1;

	@Override
	public Object get(String collection, String key) {

		if (this.twoLevelMap.containsKey(collection)) {

			Map<String, CacheObject> levelOneMap = this.twoLevelMap.get(collection);
			if (null != levelOneMap && levelOneMap.containsKey(key)) {

				CacheObject cacheObject = levelOneMap.get(key);

				if (cacheObject.getCreatedAt().plusHours(hours).isBefore(LocalDateTime.now())) {
					levelOneMap.remove(key);
				} else {

					return cacheObject.getValue();
				}

			}
		}

		throw new AppException("NoContent", HttpStatus.NOT_FOUND,
				Arrays.asList(new ExceptionResponse.ExceptionResponseBuilder(AppLevelErrorConstant.NO_CONTENT,
						"Element Not Found in Cache").withErrorLevel(ErrorLevel.FUNCTIONAL)
								.withErrorType(ErrorType.INFO).build()));

	}

	@Override
	public boolean put(String collection, String key, Object value) {

		if (this.getMapValuesCount() >= maxCacheSize) {
			throw new AppException("CacheOverFlow", HttpStatus.FORBIDDEN,
					Arrays.asList(new ExceptionResponse.ExceptionResponseBuilder(AppLevelErrorConstant.FORBIDDEN,
							"Cache server is full").withErrorLevel(ErrorLevel.FUNCTIONAL).withErrorType(ErrorType.INFO)
									.build()));
		}

		CacheObject cacheObject = new CacheObject(value, LocalDateTime.now());

		if (twoLevelMap.containsKey(collection)) {

			Map<String, CacheObject> map = twoLevelMap.get(collection);

			if (null != map) {
				map.put(key, cacheObject);
			} else {

				Map<String, CacheObject> levelOneMap = new HashMap<>();

				levelOneMap.put(key, cacheObject);
			}
		} else {

			Map<String, CacheObject> levelOneMap = new HashMap<>();

			levelOneMap.put(key, cacheObject);

			twoLevelMap.put(collection, levelOneMap);

		}

		return true;

	}

	@Override
	public boolean delete(String collection, String key) {

		if (this.twoLevelMap.containsKey(collection)) {

			Map<String, CacheObject> levelOneMap = this.twoLevelMap.get(collection);

			if (null != levelOneMap && levelOneMap.containsKey(key)) {
				levelOneMap.remove(key);
				return true;
			}
		}

		throw new AppException("NoContent", HttpStatus.NOT_FOUND,
				Arrays.asList(new ExceptionResponse.ExceptionResponseBuilder(AppLevelErrorConstant.NO_CONTENT,
						"Element Not Found in Cache").withErrorLevel(ErrorLevel.FUNCTIONAL)
								.withErrorType(ErrorType.INFO).build()));

	}

	private int getMapValuesCount() {

		int count = 0;

		Set<String> collectionKeySet = this.twoLevelMap.keySet();

		for (String collectionKey : collectionKeySet) {
			Map<String, CacheObject> levelOneMap = this.twoLevelMap.get(collectionKey);
			Set<String> keySetLevelOne = levelOneMap.keySet();

			count += keySetLevelOne.size();

		}

		return count;
	}
}
