package com.cache.server.scheadular;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cache.server.model.CacheObject;

@Component
public class CacheEvictionScheadular {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheEvictionScheadular.class);

	@Value("${cache.expiration.hours}")
	private int hours;

	@Autowired
	ConcurrentMap<String, Map<String, CacheObject>> twoLevelMap;

	// Runs every 1 minutes
	@Scheduled(fixedDelay = 1 * 60 * 1000)
	public void execute() {

		Set<String> collectionKeySet = twoLevelMap.keySet();

		for (String collectionKey : collectionKeySet) {

			Map<String, CacheObject> levelOneMap = twoLevelMap.get(collectionKey);

			Set<String> levelOneKeySet = levelOneMap.keySet();

			for (String key : levelOneKeySet) {

				CacheObject cacheObject = levelOneMap.get(key);

				if (cacheObject.getCreatedAt().plusHours(hours).isBefore(LocalDateTime.now())) {
					levelOneMap.remove(key);

					LOGGER.info("Key {} has been removed from collection {} by CacheEvictionScheadular", key,
							collectionKey);
				}

			}

		}
	}
}
